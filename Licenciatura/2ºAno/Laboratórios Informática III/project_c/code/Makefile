FLAGS= -std=c99 -Wextra -O2 -D_GNU_SOURCE -pedantic -pedantic-errors -Wall -Wno-unused-variable -Wno-unused-but-set-variable 
PKGF= `pkg-config --cflags glib-2.0`
PKGL= `pkg-config --libs glib-2.0` -lm

COMPILER = gcc
OUT = program

INCLUDES = includes
SRC = src
OBJ = obj
MODL = $(SRC)/modelo
CTRL = $(SRC)/controlador
VISUAL = $(SRC)/visualizador

##--------------------------MODELO-------------
CODEMODL = $(MODL)/strUser.c $(MODL)/strBusiness.c $(MODL)/strReview.c $(MODL)/infoUser.c $(MODL)/infoBusiness.c $(MODL)/infoReview.c $(MODL)/stats.c $(MODL)/queryarg.c $(MODL)/sgr.c 
INCLMODL = $(INCLUDES)/strUser.h $(INCLUDES)/strBusiness.h $(INCLUDES)/strReview.h $(INCLUDES)/infoUser.h $(INCLUDES)/infoBusiness.h $(INCLUDES)/infoReview.h $(INCLUDES)/stats.h $(INCLUDES)/queryarg.h $(INCLUDES)/sgr.h 
OBJMODL = $(OBJ)/strUser.o $(OBJ)/strBusiness.o $(OBJ)/strReview.o $(OBJ)/infoUser.o $(OBJ)/infoBusiness.o $(OBJ)/infoReview.o $(OBJ)/stats.o $(OBJ)/queryarg.o $(OBJ)/sgr.o

##-------------------------CONTROLO---------------
CODECTRL = $(CTRL)/table.c $(CTRL)/interpretador.c $(CTRL)/variaveis.c $(SRC)/main.c
INCLCTRL = $(INCLUDES)/table.h $(INCLUDES)/interpretador.h $(INCLUDES)/variaveis.h
OBJCTRL = $(OBJ)/table.o $(OBJ)/interpretador.o $(OBJ)/variaveis.o $(OBJ)/main.o

##--------------------------VISUALIZADOR--------------
CODEVISUAL = $(VISUAL)/paginacao.c $(VISUAL)/visualizador.c
INCLVISUAL = $(INCLUDES)/paginacao.h $(INCLUDES)/visualizador.h
OBJVISUAL = $(OBJ)/paginacao.o $(OBJ)/visualizador.o

ALLOBJ = $(OBJMODL) $(OBJVISUAL) $(OBJCTRL) $(OBJ)/FuncAux.o
ALLINCLUDES = $(INCLMODL) $(INCLCTRL) $(INCLVISUAL) $(INCLUDES)/FuncAux.h


program:
	@echo Make Program:
	@make aux modelo controlador visualizador link
	@echo Done!


link: $(ALLINCLUDES) $(ALLOBJ)
	@echo Linking...
	@$(COMPILER) $(FLAGS) $(PKGF) $(ALLOBJ) $(PKGL) -o $(OUT)


modelo: $(CODEMODL) $(INCLMODL)
	@echo Compiling Modelo...
	@$(COMPILER) -c $(FLAGS) $(PKGF) $(CODEMODL) $(PKGL)
	@mv *.o $(OBJ)/


controlador: $(CODECTRL) $(INCLCTRL)
	@echo Compiling Controlador...
	@$(COMPILER) -c $(FLAGS) $(PKGF) $(CODECTRL) $(PKGL)
	@mv *.o $(OBJ)/


visualizador: $(CODEVISUAL) $(INCLVISUAL)
	@echo Compiling visualizador...
	@$(COMPILER) -c $(FLAGS) $(PKGF) $(CODEVISUAL) $(PKGL)
	@mv *.o $(OBJ)/


aux: $(SRC)/FuncAux.c
	@echo Compiling Aux...
	@$(COMPILER) -c $(FLAGS) $(PKGF) $(SRC)/FuncAux.c $(PKGL)
	@mv *.o $(OBJ)/

memcheck:
	valgrind --leak-check=full --show-leak-kinds=all -s ./$(OUT) 

clean:
	@echo All clean now!
	@rm -f $(OBJ)/*.o .a *~ Makefile.bak $(OUT)
