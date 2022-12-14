:-style_check(-singleton).
:-style_check(-discontiguous).

:-include('grafo.pl').
:-include('auxiliares-2fase.pl').
:-include('dbGreenDistribution.pl').



% ------------------------------------------ PESQUISA NÃO INFORMADA -------------------------------------------------
% ALGORTIMO DE PROCURA NÃO INFORMADA - DFS (depth-First)
resolve_pp_c(Nodo, Destino, [Nodo|Caminho],C) :-
    profundidadePrimeiro(Nodo,Destino,[Nodo],Caminho,C).

profundidadePrimeiro(Destino,Destino,_,[],0).
profundidadePrimeiro(Nodo,D,Historico,[ProxNodo|Caminho],C) :-
    adjacente(Nodo,ProxNodo,C1),
    not(member(ProxNodo,Historico)),
    profundidadePrimeiro(ProxNodo,D,[ProxNodo|Historico],Caminho,C2), C is C1+C2.


% procura os adjacentes ao nodo
adjacente(Nodo,ProxNodo,C) :- distancia(Nodo,ProxNodo,C).
adjacente(Nodo,ProxNodo,C) :- distancia(ProxNodo,Nodo,C).


melhor(Nodo,S,Custo) :- findall((SS,CC), resolve_pp_c(Nodo,SS,CC), L),
                        minimo(L,(S,Custo)).

minimo([(P,X)],(P,X)).
minimo([(Px,X)|L],(Py,Y)) :- minimo(L,(Py,Y)), X>Y.
minimo([(Px,X)|L],(Px,X)) :- minimo(L,(Py,Y)), X=<Y.


% caminho de volta para a empresa.
dfsInverso(Nodo, [Nodo|Caminho],C) :-
    profPrimeiro(Nodo, [Nodo], Caminho,C).

profPrimeiro(Nodo,_,[],0) :- volta(Nodo).
profPrimeiro(Nodo,Historico,[ProxNodo|Caminho],C) :-
    adjacenteInverso(Nodo,ProxNodo,C1),
    not(member(ProxNodo,Historico)),
    profPrimeiro(ProxNodo,[ProxNodo|Historico],Caminho,C2), C is C1+C2.

adjacenteInverso(Nodo,ProxNodo,C) :- distancia(ProxNodo,Nodo,C).
adjacenteInverso(Nodo,ProxNodo,C) :- distancia(Nodo,ProxNodo,C).







% ALGORTIMO DE PROCURA NÃO INFORMADA - BFS (Breadth-First)
bfs(Orig, Dest, Cam) :- bfs2(Dest,[[Orig]],Cam).

bfs2(Dest,[[Dest|T]|_],Cam) :- reverse([Dest|T],Cam).
bfs2(Dest,[LA|Outros],Cam) :- 
    LA = [Act|_],
    findall([X|LA], (Dest\==Act, distancia(Act,X,_), not(member(X,LA))),Novos),
    append(Outros,Novos,Todos),
    bfs2(Dest,Todos,Cam).


%caminho de volta para a empresa
bfsInverso(Orig, Dest, Cam) :- bfs2Inverso(Dest,[[Orig]],Cam).

bfs2Inverso(Dest,[[Dest|T]|_],Cam) :- reverse([Dest|T],Cam).
bfs2Inverso(Dest,[LA|Outros],Cam) :- 
    LA = [Act|_],
    findall([X|LA], (Dest\==Act, distancia(X,Act,_), not(member(X,LA))),Novos),
    append(Outros,Novos,Todos),
    bfs2Inverso(Dest,Todos,Cam).











% ------------------------------------------ PESQUISA INFORMADA -------------------------------------------------
% ALGORTIMO DE PROCURA INFORMADA - ESTRELA - minimiza o custo
resolve_aestrela(Nodo,Caminho/Custo) :-
    tempo(Nodo,Estima),
    aestrela([[Nodo]/0/Estima],InvCaminho/Custo/_), 
    reverse(InvCaminho,Caminho).

aestrela(Caminhos,Caminho) :- 
    obtem_melhor(Caminhos,Caminho),
    Caminho = [Nodo|_]/_/_,
    goal(Nodo).

aestrela(Caminhos,SolucaoCaminho) :-
    obtem_melhor(Caminhos,MelhorCaminho),
    seleciona(MelhorCaminho,Caminhos,OutrosCaminhos),
    expande_aestrela(MelhorCaminho,ExpCaminhos),
    append(OutrosCaminhos,ExpCaminhos,NovoCaminhos),
    aestrela(NovoCaminhos,SolucaoCaminho).

obtem_melhor([Caminho], Caminho) :- !.
obtem_melhor([Caminho1/Custo1/Est1,_/Custo2/Est2|Caminhos],MelhorCaminho) :-
    Custo1 + Est1 =< Custo2 + Est2, !,
    obtem_melhor([Caminho1/Custo1/Est1|Caminhos], MelhorCaminho).
obtem_melhor([_|Caminhos],MelhorCaminho) :-
    obtem_melhor(Caminhos,MelhorCaminho).

expande_aestrela(Caminho,ExpCaminhos) :-
    findall(NovoCaminho,adjacente2(Caminho,NovoCaminho),ExpCaminhos). 



%caminho de volta para a empresa
resolve_aestrelaInverso(Nodo,Caminho/Custo) :-
    tempo(Nodo,Estima),
    aestrelaInverso([[Nodo]/0/Estima],InvCaminho/Custo/_), 
    reverse(InvCaminho,Caminho).

aestrelaInverso(Caminhos,Caminho) :- 
    obtem_melhor(Caminhos,Caminho),
    Caminho = [Nodo|_]/_/_,
    volta(Nodo).

aestrelaInverso(Caminhos,SolucaoCaminho) :-
    obtem_melhor(Caminhos,MelhorCaminho),
    seleciona(MelhorCaminho,Caminhos,OutrosCaminhos),
    expande_aestrelaInverso(MelhorCaminho,ExpCaminhos),
    append(OutrosCaminhos,ExpCaminhos,NovoCaminhos),
    aestrelaInverso(NovoCaminhos,SolucaoCaminho).

expande_aestrelaInverso(Caminho,ExpCaminhos) :-
    findall(NovoCaminho,adjacente2Inverso(Caminho,NovoCaminho),ExpCaminhos). 









% algortimo de procura informada GULOSA - minimiza a soma do custo com a 
resolve_gulosa(Nodo,Caminho/Custo) :-
    tempo(Nodo,Estimativa),
    agulosa([[Nodo]/0/Estimativa], InvCaminho/Custo/_),
    reverse(InvCaminho, Caminho).

agulosa(Caminhos,Caminho) :-
    obtem_melhor_g(Caminhos,Caminho),
    Caminho = [Nodo|_]/_/_,
    goal(Nodo).

agulosa(Caminhos,SolucaoCaminho) :- 
    obtem_melhor_g(Caminhos,MelhorCaminho),
    seleciona(MelhorCaminho,Caminhos,OutrosCaminhos),
    expande_gulosa(MelhorCaminho, ExpCaminhos),
    append(OutrosCaminhos,ExpCaminhos,NovoCaminhos),
    agulosa(NovoCaminhos,SolucaoCaminho).

obtem_melhor_g([Caminho],Caminho) :- !.
obtem_melhor_g([Caminho1/Custo1/Est1,_/Custo2/Est2 | Caminhos], MelhorCaminho) :-
    Custo1 =< Custo2, !,
    obtem_melhor_g([Caminho1/Custo1/Est1 | Caminhos], MelhorCaminho).

obtem_melhor_g([_|Caminhos], MelhorCaminho) :-
    obtem_melhor_g(Caminhos,MelhorCaminho).

expande_gulosa(Caminho, ExpCaminho) :-
    findall(NovoCaminho, adjacente2(Caminho,NovoCaminho),ExpCaminho).



%caminho de volta para a empresa
resolve_gulosaInverso(Nodo,Caminho/Custo) :-
    tempo(Nodo,Estimativa),
    agulosaInverso([[Nodo]/0/Estimativa], InvCaminho/Custo/_),
    reverse(InvCaminho, Caminho).

agulosaInverso(Caminhos,Caminho) :-
    obtem_melhor_g(Caminhos,Caminho),
    Caminho = [Nodo|_]/_/_,
    volta(Nodo).

agulosaInverso(Caminhos,SolucaoCaminho) :- 
    obtem_melhor_g(Caminhos,MelhorCaminho),
    seleciona(MelhorCaminho,Caminhos,OutrosCaminhos),
    expande_gulosaInverso(MelhorCaminho, ExpCaminhos),
    append(OutrosCaminhos,ExpCaminhos,NovoCaminhos),
    agulosaInverso(NovoCaminhos,SolucaoCaminho).

expande_gulosaInverso(Caminho, ExpCaminho) :-
    findall(NovoCaminho, adjacente2Inverso(Caminho,NovoCaminho),ExpCaminho).








% auxiliares
adjacente2([Nodo|Caminho]/Custo/_, [ProxNodo,Nodo|Caminho]/NovoCusto/Est) :-
    distancia(Nodo,ProxNodo,PassoCusto),
    not(member(ProxNodo,Caminho)),
    NovoCusto is Custo+PassoCusto,
    tempo(ProxNodo,Est).

adjacente2Inverso([Nodo|Caminho]/Custo/_, [ProxNodo,Nodo|Caminho]/NovoCusto/Est) :-
    distancia(ProxNodo,Nodo,PassoCusto),
    not(member(ProxNodo,Caminho)),
    NovoCusto is Custo+PassoCusto,
    tempo(ProxNodo,Est).