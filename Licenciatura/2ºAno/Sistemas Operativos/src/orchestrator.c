#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/select.h>
#include <sys/time.h>
#include "../includes/task.h"

Task tasks_in_execution[MAX_TASKS];
int pos = 0;

Task wait_list[MAX_TASKS];
int waitpos = 0;

int i = 0;

void addToExecutionList(Task *t){

    if (pos >= MAX_TASKS){
        fprintf(stderr, "Número de tasks excedida\n");
        _exit(1);
    }
    pos++;
    tasks_in_execution[pos] = *t;
}



void writeLog(Task t) {
    char file_name[] = "../logs/terminated_tasks.txt"; // File name remains constant for all tasks
    int time = 0;
    struct timeval end;
    gettimeofday(&end, NULL);
    time = (end.tv_sec) * 1000 + (end.tv_usec) / 1000 - t.real_time;
    t.real_time = time;

    int file = open(file_name, O_RDWR | O_CREAT | O_APPEND, 0666); // Open file in append mode
    if (file == -1) {
        perror("Error opening file");
        return;
    }

    char log[1024];
    if (t.number_commands == 1) {
        snprintf(log, sizeof(log), "%d %s %s%dms\n", t.pid, t.name, t.args, t.real_time);
    } else {
        char commands[512] = "";
        for (int i = 0; i < t.number_commands; i++) {
            strcat(commands, t.commands[i]);
            if (i != t.number_commands - 1) {
                strcat(commands, " | ");
            }
        }
        snprintf(log, sizeof(log), "%d %s %dms\n", t.pid, commands, t.real_time);
    }

    ssize_t bytes_written = write(file, log, strlen(log));
    if (bytes_written == -1) {
        perror("Error writing to file");
    }

    if (close(file) == -1) {
        perror("Error closing file");
    }
}


void removeFromExecutionList(pid_t pid) {
    int found = 0;
    for (int i = 0; i <= pos; i++) {
        if (tasks_in_execution[i].pid == pid) {
            found = 1;
            for (int j = i; j < pos - 1; j++) {
                tasks_in_execution[j] = tasks_in_execution[j + 1];
            }
            // Clear the last element to avoid duplicates
            memset(&tasks_in_execution[pos - 1], 0, sizeof(Task));
            break;
        }
    }
    if (found) {
        pos--;
    }
}

int execute_command(Task task, const char *result_path) {
    // Construct the full result path: results/pid
    char full_path[256];
    snprintf(full_path, sizeof(full_path), "%s/%d", result_path, task.pid);

    // Open the result file
    int result_fd = open(full_path, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (result_fd == -1) {
        perror("open");
        return EXIT_FAILURE;
    }

    pid_t pid = fork();
    if (pid == -1) {
        perror("fork");
        close(result_fd);
        return EXIT_FAILURE;
    }

    if (pid == 0) { // Child process
        // Redirect stdout and stderr to the result file
        dup2(result_fd, STDOUT_FILENO);
        dup2(result_fd, STDERR_FILENO);
        close(result_fd);

        char *args[MAX_ARGS + 2]; // Extra space for program name and NULL terminator
        args[0] = task.name;
        char *token = strtok(task.args, " ");
        int i = 1;
        while (token != NULL && i < MAX_ARGS + 1) {
            args[i++] = token;
            token = strtok(NULL, " ");
        }
        args[i] = NULL; // NULL terminator

        execvp(task.name, args);

        // If execvp returns, there was an error
        perror("execvp");
        exit(EXIT_FAILURE); // Exit child process with failure
    } else { // Parent process
        // Wait for child process to finish
        int status;
        if (waitpid(pid, &status, 0) == -1) {
            perror("waitpid");
            close(result_fd);
            return EXIT_FAILURE;
        }

        // Close the result file
        close(result_fd);


        // Check if child process exited normally
        if (WIFEXITED(status)) {
            if (WEXITSTATUS(status) == EXIT_SUCCESS) {
                // Child process exited successfully
                printf("Command execution succeeded. Result written to: %s\n", full_path);
            } else {
                printf("Command execution failed. Error written to: %s\n", full_path);
            }
        } else {
            printf("Command execution failed.\n");
        }
    }
    return EXIT_SUCCESS;
}

int exec_command_pipe(char* arg, int output_fd) {
    char *exec_args[MAX_ARGS];

    char *string;    
    int exec_ret = 0;
    int i=0;

    char* command = strdup(arg);

    string=strtok(command," ");
    
    while(string!=NULL){
        exec_args[i]=string;
        string=strtok(NULL," ");
        i++;
    }

    exec_args[i]=NULL;

    // Redirect output to the provided file descriptor
    if (dup2(output_fd, STDOUT_FILENO) == -1) {
        perror("dup2 failed");
        exit(EXIT_FAILURE);
    }

    exec_ret=execvp(exec_args[0], exec_args);
    
    return exec_ret;
}

int execute_pipe(Task request, char* result_path) {
    int pipes[request.number_commands - 1][2];
    int success = 1; // Flag to track overall success

    char full_path[256];
    snprintf(full_path, sizeof(full_path), "%s/%d", result_path, request.pid);

    // Creating pipes
    for (int i = 0; i < request.number_commands - 1; i++) {
        if (pipe(pipes[i]) == -1) {
            perror("Pipe failed");
            exit(EXIT_FAILURE);
        }
    }

    // Forking processes
    for (int i = 0; i < request.number_commands; i++) {
        pid_t pid = fork();
        if (pid == -1) {
            perror("fork failed");
            exit(EXIT_FAILURE);
        }
        if (pid == 0) { // Child process
            // Redirect input
            if (i > 0) {
                if (dup2(pipes[i - 1][0], STDIN_FILENO) == -1) {
                    perror("dup2-stdin failed");
                    exit(EXIT_FAILURE);
                }
            }
            // Redirect output
            if (i < request.number_commands - 1) {
                if (dup2(pipes[i][1], STDOUT_FILENO) == -1) {
                    perror("dup2-stdout failed");
                    exit(EXIT_FAILURE);
                }
            } else { // Last command
                int fd = open(full_path, O_WRONLY | O_CREAT | O_TRUNC, 0666);
                if (fd == -1) {
                    perror("open failed");
                    exit(EXIT_FAILURE);
                }
                if (dup2(fd, STDOUT_FILENO) == -1 || dup2(fd, STDERR_FILENO) == -1) {
                    perror("dup2-stdout/stderr failed");
                    exit(EXIT_FAILURE);
                }
                close(fd);
            }
            // Close pipes
            for (int j = 0; j < request.number_commands - 1; j++) {
                close(pipes[j][0]);
                close(pipes[j][1]);
            }
            // Execute command
            int exec_result = exec_command_pipe(request.commands[i], STDOUT_FILENO);
            if (exec_result == -1) {
                perror("exec_command_pipe failed");
                exit(EXIT_FAILURE);
            }
            exit(EXIT_SUCCESS); // Success
        }
    }

    // Close unused pipe ends
    for (int i = 0; i < request.number_commands - 1; i++) {
        close(pipes[i][0]);
        close(pipes[i][1]);
    }

    // Wait for all child processes to finish
    for (int i = 0; i < request.number_commands; i++) {
        int status;
        if (wait(&status) == -1) {
            perror("wait failed");
            exit(EXIT_FAILURE);
        }
        // Check if child process failed
        if (!WIFEXITED(status) || WEXITSTATUS(status) != EXIT_SUCCESS) {
            success = 0;
        }
    }

    if (success) {
        printf("Command execution succeeded. Result written to: %s\n", full_path);
    } else {
        printf("Command execution failed. Error written to: %s\n", full_path);
    }

    return 0;
}




void removeFirstElement(Task arr[], int size) {
    
    // Shift elements to the left
    for (int i = 0; i < size - 1; i++) {
        arr[i] = arr[i + 1];
    }
}


void processRequest(Task *request, char* resultpath) {
    if(request->number_commands == 1){
       execute_command(*request, resultpath);
    }
    else{
        execute_pipe(*request, resultpath);
    }
    writeLog(*request);
}

int write_commands_to_buffer(char *buffer, size_t buffer_size, char commands[MAX_COMMANDS][MAX_COMMAND_LENGTH], int num_commands) {
    size_t buffer_pos = 0; // Position within the buffer

    for (int i = 0; i < num_commands; i++) {
        if (i == num_commands - 1) {
            // Last command, write it directly to the buffer
            size_t command_length = strlen(commands[i]);
            if (buffer_pos + command_length >= buffer_size) {
                // Buffer overflow, return -1
                return -1;
            }
            strcpy(buffer + buffer_pos, commands[i]);
            buffer_pos += command_length;
        } else {
            // Not the last command, format it with " | " and write to buffer
            size_t command_length = strlen(commands[i]);
            if (buffer_pos + command_length + 3 >= buffer_size) {
                // Buffer overflow, return -1
                return -1;
            }
            snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "%s | ", commands[i]);
            buffer_pos += command_length + 3; // Include space for " | "
        }
    }

     // Add a newline character after the last command
    if (buffer_pos < buffer_size) {
        buffer[buffer_pos++] = '\n';
    } else {
        // Buffer overflow, return -1
        return -1;
    }

    // Null-terminate the buffer
    if (buffer_pos < buffer_size) {
        buffer[buffer_pos] = '\0';
    } else {
        // Buffer overflow, return -1
        return -1;
    }

    // Return the number of bytes written to the buffer
    return buffer_pos;
}

void write_status_to_buffer(char *buffer, size_t buffer_size, char* logs_file_path) {
    // Initialize buffer position
    size_t buffer_pos = 0;

    // Write tasks in the wait list to the buffer as "Scheduled" section
    buffer_pos += snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "Scheduled:\n");
    for (int i = 1; i < waitpos+1; i++) {
        if (wait_list[i].pid != 0) {
            if (wait_list[i].number_commands == 1) {
                buffer_pos += snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "%d %s\n", wait_list[i].pid, wait_list[i].name);
            } else {
                buffer_pos += snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "%d ", wait_list[i].pid);
                buffer_pos += write_commands_to_buffer(buffer + buffer_pos, buffer_size - buffer_pos, wait_list[i].commands, wait_list[i].number_commands);
            }
        }
    }

    // Write tasks in the execution list to the buffer as "Executing" section
    buffer_pos += snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "\n\nExecuting:\n");
    for (int i = 0; i < pos+1; i++) {
        if (tasks_in_execution[i].pid != 0) {
            if (tasks_in_execution[i].number_commands == 1) {
                buffer_pos += snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "%d %s\n", tasks_in_execution[i].pid, tasks_in_execution[i].name);
            } else {
                buffer_pos += snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "%d ", tasks_in_execution[i].pid);
                buffer_pos += write_commands_to_buffer(buffer + buffer_pos, buffer_size - buffer_pos, tasks_in_execution[i].commands, tasks_in_execution[i].number_commands);
            }
        }
    }

    // Open the logs file
    int logs_fd = open(logs_file_path, O_RDONLY);
    if (logs_fd == -1) {
        perror("Error opening logs file");
        return;
    }

    // Write tasks from the logs file to the buffer as "Finished" section
    buffer_pos += snprintf(buffer + buffer_pos, buffer_size - buffer_pos, "\n\nFinished:\n");
    ssize_t bytes_read;
    while ((bytes_read = read(logs_fd, buffer + buffer_pos, buffer_size - buffer_pos)) > 0) {
        buffer_pos += bytes_read;
    }

    close(logs_fd);

    // Null-terminate the buffer
    if (buffer_pos < buffer_size) {
        buffer[buffer_pos] = '\0';
    }
}

// Function to save the count to a file
void saveCountToFile(int count) {
    int fd = open(COUNT_FILE, O_WRONLY | O_CREAT | O_TRUNC, 0666);
    if (fd == -1) {
        perror("Error opening count file for writing");
        exit(EXIT_FAILURE);
    }
    char count_str[20];
    int len = snprintf(count_str, sizeof(count_str), "%d", count);
    if (write(fd, count_str, len) == -1) {
        perror("Error writing count to file");
        exit(EXIT_FAILURE);
    }
    close(fd);
}

// Function to read the count from a file
int readCountFromFile() {
    int fd = open(COUNT_FILE, O_RDONLY);
    if (fd == -1) {
        // If the file doesn't exist, return 0
        return 0;
    }
    char count_str[20];
    int bytes_read = read(fd, count_str, sizeof(count_str) - 1);
    if (bytes_read == -1) {
        perror("Error reading count from file");
        exit(EXIT_FAILURE);
    }
    count_str[bytes_read] = '\0';
    close(fd);
    return atoi(count_str);
}


int main(int argc, char* argv[]) {
    if (argc < 4) {
        printf("Modo de utilização:\n");
        printf("./orchestrator result_path parallel-tasks sched_policy(1-FCFS || 2-SJF)\n");
        return 1;
    }

    char* resultpath = argv[1];
    int max_parallel = atoi(argv[2]);
    int sched_policy = atoi(argv[3]);
    if((sched_policy != 1) && sched_policy != 2){
        printf("Sched_policy inválida\n");
        return 1;
    }


    int parallel = 0;
    mkdir("../logs", 0777);
    int count = readCountFromFile();


    unlink(SERVER);
    if (mkfifo(SERVER, 0666) == -1) {
        perror("Error creating server fifo");
        exit(EXIT_FAILURE);
    }

    int running = 1;
    while (running) {
        // Check for new tasks
        int fd = open(SERVER, O_RDONLY | O_NONBLOCK);
        if (fd == -1) {
            perror("Error opening server fifo");
            exit(EXIT_FAILURE);
        }
        
        fd_set read_fds;
        FD_ZERO(&read_fds);
        FD_SET(fd, &read_fds);

        struct timeval timeout;
        timeout.tv_sec = 1; // Timeout of 1 second
        timeout.tv_usec = 0;

        int ready = select(fd + 1, &read_fds, NULL, NULL, &timeout);

        if (ready != 0) {
            ssize_t r;
            Task rcvd;
            while ((r = read(fd, &rcvd, sizeof(Task))) > 0) {
                if (rcvd.type == 1) {
                    count++;
                    int pid = rcvd.pid;
                    rcvd.pid = count;
                    
                    //FCFS
                    if(sched_policy == 1){
                        wait_list[waitpos] = rcvd;
                    } 
                    //SJF
                    else{
                        int insert_pos = 0;
                        while (insert_pos < waitpos && rcvd.estimated_time > wait_list[insert_pos].estimated_time) {
                            insert_pos++;
                        }

                        // Shift
                        for (int i = waitpos; i > insert_pos; i--) {
                            wait_list[i] = wait_list[i - 1];
                        }

                        wait_list[insert_pos] = rcvd;
                                
                    }
                    waitpos++;

                    char str[64];
                    snprintf(str, sizeof(str), "Task: %d received.\n", count);
                    size_t len = strlen(str);

                    write(1, str, len);

                    char fifoname[20];
                    snprintf(fifoname, sizeof(fifoname), CLIENT "%d", pid);
                    int fd_write = open(fifoname, O_WRONLY);
                    if (fd_write == -1) {
                        perror("Error opening client fifo for writing");
                        exit(EXIT_FAILURE);
                    }

                    if (write(fd_write, str, len) == -1) {
                        perror("Error writing to client fifo");
                        close(fd_write);
                        exit(EXIT_FAILURE);
                    }
                    close(fd_write);
                }
                else {
                    char fifoname[20];
                    snprintf(fifoname, sizeof(fifoname), CLIENT "%d", rcvd.pid);
                    int fd_write = open(fifoname, O_WRONLY);
                    if (fd_write == -1) {
                        perror("Error opening client fifo for writing");
                        exit(EXIT_FAILURE);
                    }
                    ssize_t s = MAX_BUFFER_SIZE;
                    char buffer[s];
                    write_status_to_buffer(buffer, s, "../logs/terminated_tasks.txt");

                    write(fd_write, buffer, sizeof(buffer));
                    close(fd_write);
                }
            } 
        }  

        // Execute tasks
        while (waitpos > 0 && parallel < max_parallel) {
            Task ct = wait_list[0];
            removeFirstElement(wait_list, waitpos);
            waitpos--;
            addToExecutionList(&ct);

            pid_t pid = fork();
            if (pid == -1) {
                perror("Erro ao criar processo filho");
                exit(EXIT_FAILURE);
            } else if (pid == 0) { // Child process
                if (ct.type == 1) {
                    processRequest(&ct, resultpath);
                }
                exit(ct.pid);
            } else { // Parent process
                parallel++;
            }
        }

         // Check if any child processes have exited
        int status;
        pid_t child_pid;
        while ((child_pid = waitpid(-1, &status, WNOHANG)) > 0) {
            if (WIFEXITED(status)) {
                int exit_status = WEXITSTATUS(status);
                removeFromExecutionList(exit_status);
            } else if (WIFSIGNALED(status)) {
                // Handle termination by signal if needed
                removeFromExecutionList(WTERMSIG(status));
            }
            // Decrement the number of parallel tasks
            parallel--;
        }
        saveCountToFile(count);
    }

    exit(EXIT_SUCCESS);
}
