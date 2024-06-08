all: folders orchestrator client hello void

orchestrator: bin/orchestrator

client: bin/client

hello: bin/hello

void: bin/void

folders:
	@mkdir -p obj bin tmp includes results

bin/orchestrator: obj/orchestrator.o 
	gcc -g obj/orchestrator.o -o bin/orchestrator


bin/client: obj/client.o 
	gcc -g obj/client.o -o bin/client

bin/hello: obj/hello.o 
	gcc -g obj/hello.o -o bin/hello

bin/void: obj/void.o 
	gcc -g obj/void.o -o bin/void

obj/orchestrator.o: src/orchestrator.c includes
	gcc -Wall -g -c src/orchestrator.c -Iincludes -o obj/orchestrator.o

obj/client.o: src/client.c includes
	gcc -Wall -g -c src/client.c -Iincludes -o obj/client.o 

obj/hello.o: src/hello.c includes
	gcc -Wall -g -c src/hello.c -Iincludes -o obj/hello.o 

obj/void.o: src/void.c includes
	gcc -Wall -g -c src/void.c -Iincludes -o obj/void.o

bin:
	@mkdir -p bin

clean:
	rm -rf obj/* tmp/* logs/* results/*
	find bin/ -type f ! -name 'script.sh' ! -name 'script2.sh' ! -name 'script3.sh' -exec rm -f {} +

