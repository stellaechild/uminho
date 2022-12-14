# MIEI - Laboratórios de Informática II - PL01 - (Grupo nº1) - Ano 19/20                                                    
Autores:                                                                                                                    
a93290 - Joana Maia Teixeira Alves                                                 
a93264 - Maria Eugénia Bessa Cunha                                                                                          
a93296 - Vicente Gonçalves Moreira                                                                                          
                                                                                                                       
                                                                                                                  
                                                                                                                
**Semana 9-13 Março:**                                                                              
***GERAL***: Início do projeto, criamos os vários ficheiros .c/.h para os vários "módulos". Copiamos também as indicações e funções dadas no guião para os respetivos locais.                                                
***PROGRESSOS***: Desenvolvemos as funções "Mostrar tabuleiro","Incializar tabuleiro", "Jogar", "Obter número de jogadas","Obter jogador atual" até ao limite pedido no guião.                                                              
***DIFICULDADES/DÚVIDAS***: Entendimento do funcionamento de algumas estruturas, assim como includes e ficheiros header e o desenvolvimente em paralelo.


**Semana 16-20 Março:**          
***GERAL***: Rearranjamos vários nomes de ficheiros, ajustamos os includes necessários e fizemos a integração de trabalho por Clion. Adicionamos também a documentação ao projeto através do Doxygen.                             
***PROGRESSOS***: Desenvolvemos a função "jogar" completamente, esta não só verifica jogadas válidas como verifica o fim do jogo, devolvendo o vencedor. (Função "jogo_terminado","verifica_a_volta","verifica_alvo")                             
***DIFICULDADES/DÚVIDAS***: Trabalhar com a função "interpretador" e acrescentar novos comandos. Não implementamos comandos de gravar ou ler.  


**Semana 23-27 Março:**        
***GERAL***: Começamos por desenvolver os comandos "gr" e "lr" ao intrepretador de forma simples. Também desenvolvemos um função "imprime_lista_jogadas" que devolve a lista de jogadas.          
***PROGRESSOS***:Introdução à função "gravar" e "ler", estes gravam o tabuleiro num formato específico.  
***DIFICULDADES/DÚVIDAS***: Formatação do ficheiro gravado e problemas ao utilizar a função "fscanf".                               

**Semana 30 Março-3 Abril:**     
***GERAL***: Completamos os comandos gr e lr, adapatando estes aos formato pretendido. Comando "jogs" também já operacionável para o formato. Implementamos também um novo comando ao interpretador, o "pos" e arranjos no código.    
***PROGRESSOS***: Desenvolvemos a função "pos", esta retrocede o jogo para um dado número de jogada. Também foram excluídos espaços desnecessários do código.      
***DIFICULDADES/DÚVIDAS***:Dúvidas menores.                                                                                
                                                                                                                            
                                                                                                                             
**Semanas 6-17 Abril:**   
***GERAL***:Implementação de sistemas de listas e desenvolvimento inicial do bot (comando jog).                            
***PROGRESSOS***:O comando jog(o bot) segue um algoritmo simples, recorrendo apenas à distância menor possível.            
***DIFICULDADES/DÚVIDAS***:Interpretação do guião sobre o sistema de listas e a sua implementação. Dificuldade no desenvolvimento destas.                                                                                                                                                                                                                             

**Semana 20-24 Abril:**                                                                  
***GERAL***:Organização dos ficheiros e pastas do projeto. Alteração da função gravar.                          
***PROGRESSOS***:Início do desenvolvimento de um novo bot (um bot avançado).Recolha de ideias. Alteramos a função gravar e ler para corresponder ao formato requerido para o torneio (tinhamos espaços invisíveis).                                     
***DIFICULDADES/DÚVIDAS***: Implementação das várias ideias. 


**Semanas 27 Abril-1 Maio:**   
***GERAL***:Desenvolvimento e conclusão do bot avançado. Atualização de toda a documentação. Criação de novo módulo "gestaodeficheiros".                            
***PROGRESSOS***:Desenvolvimento de uma nova heurística para o bot. Esta seleciona os caminhos mais curtos, tendo em conta até 3 futuras jogadas. Esta heurística escolhe o caminho que no futuro o poẽ mais perto da vitória. O módulo "gestaodeficheiros" foi criado com a intenção de conter as funções dos comandos gravar,"gr", e ler ,"lr", e as suas respetivas auxiliares.             
***DIFICULDADES/DÚVIDAS***:Implementação do bot, em geral.   
  
