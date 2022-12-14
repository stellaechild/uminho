# source files.
SRC = shafa.c interface.c comum.c
MF = modulo_f/modulo_f.c modulo_f/functions.c modulo_f/functions_freq.c modulo_f/functions_rle.c
MT = modulo_t/modulo_t.c modulo_t/algoritmo_shanon_fanon.c modulo_t/t_estruturas.c modulo_t/tratamento_file_cod.c modulo_t/tratamento_file_freq.c
MC = modulo_c/modulo_c.c modulo_c/terminal.c modulo_c/interpreta_codificacao.c modulo_c/estruturas.c
MD = modulo_d/modulo_d.c

#All files
OBJ = $(SRC) $(MT) $(MC) $(MD) $(MF)

#executable name
OUT = shafa

# C compiler flags
CCFLAGS = -O2 -pedantic -std=c99 -Wall -Wno-unused-variable -Wno-unused-but-set-variable -Wextra -pedantic-errors

# compiler
CCC = gcc



# libraries
LIBS = -lm

$(OUT): $(OBJ)
	$(CCC) -o $(OUT) $(CCFLAGS) $(OBJ) $(LIBS)


depend:  dep
#
#dep:
#	makedepend -- $(CFLAGS) -- $(INCLUDES) $(SRC)

clean:
	rm -f *.o .a *~ Makefile.bak $(OUT)

