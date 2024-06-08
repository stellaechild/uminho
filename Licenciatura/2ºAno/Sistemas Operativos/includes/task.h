#define SERVER "server_fifo"
#define CLIENT "client_fifo"
#define MAX_NAME_LENGTH 20
#define MAX_ARGS 100
#define MAX_COMMANDS 30
#define MAX_COMMAND_LENGTH 300
#define MAX_TASKS 1000
#define MAX_BUFFER_SIZE 364000
#define COUNT_FILE "count.txt"

typedef struct task{
    int pid;
    int estimated_time;
    int real_time;
    int number_commands;
    char commands[MAX_COMMANDS][MAX_COMMAND_LENGTH];
    char name[MAX_NAME_LENGTH];
    char args[MAX_COMMAND_LENGTH];
    int num_args;
    int type; //1 - start task | 2 - status
} Task;