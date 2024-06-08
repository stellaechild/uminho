#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <string.h>
#include <sys/time.h>
#include <sys/stat.h>
#include "../includes/task.h"



Task* parse_command(char* command) {
    Task* t = (Task*) malloc(sizeof(Task));
    if (t == NULL) {
        printf("Error: Memory allocation failed.\n");
        return NULL;
    }

    t->number_commands= 1;

    // Parse estimated time
    char* estimated_time_str = strtok(command, " ");
    if (estimated_time_str == NULL) {
        printf("Error: Empty line.\n");
        free(t);
        return NULL;
    }
    t->estimated_time = atoi(estimated_time_str); // Convert string to integer

    // Parse task name
    char* token = strtok(NULL, " ");
    if (token == NULL) {
        printf("Error: Task name not found.\n");
        free(t);
        return NULL;
    }
    strncpy(t->name, token, MAX_NAME_LENGTH - 1);
    t->name[MAX_NAME_LENGTH - 1] = '\0'; // Ensure null-termination

    // Parse task arguments
    token = strtok(NULL, ""); // Get the rest of the command string as arguments
    if (token != NULL) {
        strncpy(t->args, token, sizeof(t->args) - 1);
        t->args[sizeof(t->args) - 1] = '\0'; // Ensure null-termination
    } else {
        t->args[0] = '\0'; // No arguments
    }

    return t;
}

void parse_pipe(const char* commandString, char command_list[MAX_COMMANDS][MAX_COMMAND_LENGTH], int* num) {
    int count = 0;
    char* token = strtok((char*)commandString, "|"); // Cast away const qualifier

    while (token != NULL && count < MAX_COMMANDS) {
        char command[MAX_COMMAND_LENGTH] = "";
        int i = 0;

        // Skip leading whitespaces
        while (*token == ' ')
            token++;

        // Check if the token starts with double quotes
        if (*token == '\"') {
            // Copy characters until closing double quotes
            while (*token != '\0') {
                if (*token == '\"')
                    break;
                command[i++] = *token++;
            }
        } else {
            // Copy characters until next pipe or end of string
            while (*token != '\0' && *token != '|') {
                command[i++] = *token++;
            }
        }

        // Trim trailing whitespaces
        while (i > 0 && command[i - 1] == ' ')
            i--;
        command[i] = '\0';

        // Add command to the list
        if (*command != '\0') {
            strcpy(command_list[count], command);
            count++;
        }

        // Move to the next token
        token = strtok(NULL, "|");
    }
    
    *num = count;
}



int main(int argc, char *argv[]) {
    int i;

    if (argc == 1) {
        printf("Erro. Não há argumentos.\n");
        _exit(1);
    }

    char fifoname[20];
    pid_t pid = getpid();
    snprintf(fifoname, 20, CLIENT "%d", pid);
    if (mkfifo(fifoname, 0600) == -1) {
        perror("error creating client fifo");
        return 1;
    }


    if (strcmp(argv[1], "execute") == 0) {
        if (argc < 4) {
            printf("Erro. Argumentos insuficientes para executar.\n");
            _exit(1);
        }

        int begin_time = 0;
        struct timeval end;
        gettimeofday(&end, NULL);
        begin_time = (end.tv_sec) * 1000 + (end.tv_usec) / 1000;


        if (strcmp(argv[3], "-u") == 0) {

                struct timeval begin;
                gettimeofday(&begin, NULL);
                begin_time = (begin.tv_sec) * 1000 + (begin.tv_usec) / 1000;


                char command[MAX_COMMAND_LENGTH] = "";
                strcat(command, argv[2]);
                strcat(command, " ");
                for (i = 4; i < argc; i++) {
                    strcat(command, argv[i]);
                    strcat(command, " ");
                }

                Task* t = parse_command(command);

                if (t == NULL){
                printf("Erro ao fazer parse do comando.\n");
                _exit(1);
                }

                int new_pid = getpid();
                t->pid = new_pid;
                t->real_time = begin_time;
                t->type = 1;

                // Abrir fifo do server para escrita
                int fd_write = open(SERVER, O_WRONLY);
                if (fd_write == -1) {
                    perror("error opening server fifo");
                    return 1;   
                }

                if(write(fd_write, t, sizeof(Task)) == -1){
                    perror("Erro a escrever no server_fifo");
                    close(fd_write);
                    return 1;
                }

                close(fd_write);

                // Abrir client-fifo para leitura
                int fd_read = open(fifoname, O_RDONLY);
                if (fd_read == -1) {
                    perror("error opening client fifo for reading");
                    return 1;
                }

                char str[64];
                ssize_t bytes_read = read(fd_read, str, sizeof(str) - 1);
                if (bytes_read == -1) {
                    perror("Error reading from client fifo");
                    close(fd_read);
                    exit(1);
                }
                str[bytes_read] = '\0'; // Null-terminate the string

                write(1,str, bytes_read);

                // fechar descritor
                close(fd_read);
                
                free(t);

                unlink(fifoname);
            }
    
            else if (strcmp(argv[3], "-p") == 0) {
                struct timeval begin;
                gettimeofday(&begin, NULL);
                begin_time = (begin.tv_sec) * 1000 + (begin.tv_usec) / 1000;

                int estimated_time = atoi(argv[2]);
                if (estimated_time <= 0) {
                    printf("Erro. Tempo inválido especificado.\n");
                    _exit(1);
                }

                // Parse the commands, handling quotes
                int number_of_commands;
                char command_list[MAX_COMMANDS][MAX_COMMAND_LENGTH];
                if (argc > 5 && argv[4][0] == '"') {
                    // Commands are encapsulated in quotes, treat as a single argument
                    parse_pipe(argv[4], command_list, &number_of_commands);
                } else {
                        // Concatenate the commands starting from index 5
                        char mltpl_commands[MAX_COMMAND_LENGTH] = "";
                        for (i = 4; i < argc; i++) {
                            strcat(mltpl_commands, argv[i]);
                            strcat(mltpl_commands, " ");
                        }
                        parse_pipe(mltpl_commands, command_list, &number_of_commands);
                }

                Task* t = malloc(sizeof(Task));
                if (t == NULL) {
                    perror("Error allocating memory for task");
                    exit(1);
                }

                // Copy the commands
                for (int j = 0; j < number_of_commands; j++) {
                    strcpy(t->commands[j], command_list[j]);
                }

                int new_pid = getpid();
                t->pid = new_pid;
                t->estimated_time = atoi(argv[2]);
                t->real_time = begin_time;
                t->type = 1;
                t->number_commands = number_of_commands;

                // Abrir fifo do server para escrita
                int fd_write = open(SERVER, O_WRONLY);
                if (fd_write == -1) {
                    perror("error opening server fifo");
                    exit(1);
                }

                if (write(fd_write, t, sizeof(Task)) == -1) {
                    perror("Error writing to server fifo");
                    close(fd_write);
                    exit(1);
                }

                close(fd_write);

                // Abrir client-fifo para leitura
                int fd_read = open(fifoname, O_RDONLY);
                if (fd_read == -1) {
                    perror("Error opening client fifo for reading");
                    exit(1);
                }


                char str[64];
                ssize_t bytes_read = read(fd_read, str, sizeof(str) - 1);
                if (bytes_read == -1) {
                    perror("Error reading from client fifo");
                    close(fd_read);
                    exit(1);
                }
                str[bytes_read] = '\0'; // Null-terminate the string

                write(1,str, bytes_read);

                // Fechar descritor e del fifo
                close(fd_read);
                free(t);
                unlink(fifoname);
            }

            else{
                // Caso a flag não seja "-u" nem "-p"
                printf("Comando inválido.\n");
                _exit(1);
            }
    } else if (strcmp(argv[1], "status") == 0) {
                Task* t = malloc(sizeof(Task));
                t->type = 2;
                t->pid = pid;
                 // Abrir fifo do server para escrita
                int fd_write = open(SERVER, O_WRONLY);
                if (fd_write == -1) {
                    perror("error opening server fifo");
                    exit(1);
                }

                if (write(fd_write, t, sizeof(Task)) == -1) {
                    perror("Error writing to server fifo");
                    close(fd_write);
                    exit(1);
                }

                close(fd_write);

                // Abrir client-fifo para leitura
                int fd_read = open(fifoname, O_RDONLY);
                if (fd_read == -1) {
                    perror("Error opening client fifo for reading");
                    exit(1);
                }

                char buffer[MAX_BUFFER_SIZE];
                ssize_t bytes_read;
                while ((bytes_read = read(fd_read, buffer, sizeof(buffer))) > 0) {
                    // Print the received data
                    printf("%.*s", (int)bytes_read, buffer);
                }

                // Fechar descritor e del fifo
                close(fd_read);
                free(t);
                unlink(fifoname);
        
    } else {
        // Caso o comando não seja "execute" nem "status"
        printf("Comando inválido.\n");
        _exit(1);
    }

    _exit(0);
}
