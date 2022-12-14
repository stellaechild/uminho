:- set_prolog_flag(single_var_warnings,off).

:- include('dbGreenDistribution.pl').

% maior: Numero1, Numero2, Maximo -> {V,F}
maior(X,Y,X) :- X>=Y.
maior(X,Y,Y) :- X<Y.


% menor: Numero1, Numero2, Maximo -> {V,F}
menor(X,Y,Y) :- X=<Y.
menor(X,Y,X) :- X>Y.


% -------------------------GET PARAMETROS DO INFO----------------------------
% Predicados para auxiliar na recolha de informação de uma dada entrega

getVeiculo(IDEntrega,IDVeiculo)    :- info(IDEntrega,IDVeiculo,_,_,_,_,_,_,_,_,_).
getTempoDeEntrega(IDEntrega,Tempo) :- info(IDEntrega,_,Tempo,_,_,_,_,_,_,_,_).
getPreco(IDEntrega,Preco)          :- info(IDEntrega,_,_,Preco,_,_,_,_,_,_,_).
getData(IDEntrega,Data)            :- info(IDEntrega,_,_,_,Data,_,_,_,_,_,_).
getSatisfacao(IDEntrega,Sat)       :- info(IDEntrega,_,_,_,_,Sat,_,_,_,_,_).
getEstado(IDEntrega,Estado)        :- info(IDEntrega,_,_,_,_,_,Estado,_,_,_,_).
getFreguesia(IDEntrega,Freg)       :- info(IDEntrega,_,_,_,_,_,_,Freg,_,_,_).
getRua(IDEntrega,Rua)              :- info(IDEntrega,_,_,_,_,_,_,_,Rua,_,_).
getPeso(IDEntrega,Peso)            :- info(IDEntrega,_,_,_,_,_,_,_,_,Peso,_).
getVolume(IDEntrega,Volume)        :- info(IDEntrega,_,_,_,_,_,_,_,_,_,Volume).


% getClientes: Lista de Id's de Clientes, Lista de Nomes de Clientes -> {V,F}
getClientes([],[]).
getClientes([ID|T1],[Nome|T2]) :- 
      cliente(ID,Nome,_),
      getClientes(T1,T2).


% getEstafeta: IdEstafeta, Nome Estafeta -> {V,F}
getEstafeta(ID,Nome) :- estafeta(ID,Nome,_). 


% remove_duplicates: La1, La2 -> {V,F}
remove_duplicates([],[]).
remove_duplicates([H | T], L) :-
     member(H, T),
     remove_duplicates( T, L).
remove_duplicates([H | T], [H|T1]) :- 
      \+member(H, T),
      remove_duplicates( T, T1).

% take: N, Lista, Lista resultado -> {V,F}
% Devolve nos N primeiros elementos da lista. False se N < 0.
take(0,_,[]).
take(Num,[],[]) :- maior(Num,0,Num).
take(N,[Elem|T1],[Elem|T2]) :-
      NNovo is N-1,
      take(NNovo,T1,T2).
      

% mediaLista: Lista, Media -> {V,F}
% Calcula a média de uma lista. Devolve 0 caso lista vazia
mediaLista(L,Media):- 
      sumlist(L,Somatorio),
      length(L,Tamanho), 
      (Tamanho > 0 -> Media is Somatorio/Tamanho; Media is 0).


% idMaximoEstafeta: Resultado -> {V,F}
% Coloca em IDMax o maior ID de estafeta presente na BD
idMaximoEstafeta(IDMax) :-
      findall((ID), entrega(_,ID,_), L),
      remove_duplicates(L,Res),
      max_list(Res,IDMax).


% idMaximoFreguesia: Resultado -> {V,F}
% Coloca em IDMax o maior ID de freguesia presente na BD
idMaximoFreguesia(IDMax) :-
      findall((ID), freguesia(ID,_), L),
      remove_duplicates(L,Res),
      max_list(Res,IDMax).


% getEcologiaEntrega: IdEntrega, Ecologia -> {V,F}
% Dado o Id da entrega, devolve o valor de ecologia do veiculo utilizado
getEcologiaEntrega(IdEntrega,Eco) :-
      getVeiculo(IdEntrega,Veic),
      veiculo(Veic,NomeVeic,_),
      eco(NomeVeic,Eco).


% getTipoVeiculoEntrega: IdEntrega,TipoVeiculo -> {V,F}
% Dado o Id da entrega, devolve o valor de ecologia do veiculo utilizado
getTipoVeiculoEntrega(IdEntrega,Tipo) :-
      getVeiculo(IdEntrega,Veic),
      veiculo(Veic,Tipo,_).


% maxPar: Lista de pares, Par maximo -> {V,F}
% Dado uma Lista de pares (Chave,Valor), devolve o par com maior valor.
maxPar(L,Max) :- maxPar(L,(0,0),Max).
maxPar([],Max,Max).
maxPar([(ID,Valor)|T], (P1,P2),Max) :- 
      VMax is max(Valor,P2), 
      (VMax =:= Valor -> maxPar(T,(ID,Valor),Max);maxPar(T,(P1,P2),Max)).


% entregasDeUmDia: Data, Lista de Id's de Entregas, Lista de Id's de Entregas filtrada -> {V,F}
%     Obtém o intervalo de Datas correspondente ao dia fornecido e testa a lista de entregas no dado intervalo.
entregasDeUmDia(Data,ListaEntregas,Res) :-
            getIntervaloUmDia(Data,DataInicio,DataFim),
            entregasNumIntervalo(DataInicio,DataFim,ListaEntregas,Res).


% getIntervaloUmDia: Data, DataInicio, DataFim -> {V,F}
% Devolve duas datas, correspondentes ao intervalo que o dia fornecido corresponde.
% EX: 2021-11-30 -----  ->  (2021-11-30 00:00 , 2021-11-30 23:59)
% EX: 2021-10-20 14:15  ->  (2021-10-20 00:00 , 2021-10-20 23:59)
getIntervaloUmDia(Data,DataInicio,DataFim) :-
      take(3,Data,DataDia), 
      append(DataDia,[0,0],DataInicio),
      append(DataDia,[23,59],DataFim).


% entregasNumIntervalo: DataInicio, DataFim, Lista de Id's de Entregas, Lista Filtrada -> {V,F}
% Devolve a lista de entregas filtradas pela data, dado duas datas como intervalo de tempo.
entregasNumIntervalo(_,_,[],[]).
entregasNumIntervalo(DataInicio,DataFim,[IdEntrega|T1],[IdEntrega|T2]) :-
            getData(IdEntrega,DataEntrega),
            testaDataNumIntervalo(DataInicio,DataFim,DataEntrega),
            entregasNumIntervalo(DataInicio,DataFim,T1,T2).
entregasNumIntervalo(DataInicio,DataFim,[_|T1],T2) :-
            entregasNumIntervalo(DataInicio,DataFim,T1,T2).


% testaDataNumIntervalo: DataInicio, DataFim, Data -> {V,F}
% Teste se uma Data está entre um intervalo de datas.
testaDataNumIntervalo(DataInicio,DataFim,Data) :-
            testaDataMaior(Data,DataInicio),
            testaDataMaior(DataFim,Data).


% testaDataMaior: Data X, Data Y -> {V,F}
% Avalia se a Data X é maior que a Data Y.
testaDataMaior([],[]).
testaDataMaior([ValMaior|TMaior],[ValMenor|TMenor]) :-
            (ValMaior =:= ValMenor -> testaDataMaior(TMaior,TMenor)
                                     ;maior(ValMaior,ValMenor,ValMaior)).      


% entregasDeUmEstafeta: Id Estafeta, La de entregas -> {V,F}
% Devolve a La de ID's de todas as entregas de um estafeta.
entregasDeUmEstafeta(IdEstafeta,Res) :- findall((IdEntrega),entrega(IdEntrega,IdEstafeta,_),Res).


% count: Lista, Elemento, Numero -> {V,F}
% Põe em N o número de vezes que E aparece em L.
count(L, E, N) :- include(=(E), L, L2), length(L2, N).


% volumePorFreguesia: IdFreguesia, Volume -> {V,F}
% Devolve o volume total de entregas de uma dada freguesia
volumePorFreguesia(Freg,Vol):-
	findall(IDEntrega, getFreguesia(IDEntrega,Freg),EntregasPorFreguesia),
	maplist(getVolume(),EntregasPorFreguesia,ListaVol),
      sumlist(ListaVol,Vol).