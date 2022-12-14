:-style_check(-singleton).


% minPar: Lista de pares, Par minimo -> {V,F}
% Dado uma Lista de pares (Chave,Valor), devolve o par com menor valor.
minPar(L,Min) :- minPar(L,(0,10000000000),Min).
minPar([],Min,Min).
minPar([(ID,Valor)|T], (P1,P2),Min) :- 
      VMin is min(Valor,P2), 
      (VMin =:= Valor -> minPar(T,(ID,Valor),Min);minPar(T,(P1,P2),Min)).




% novaVelocidade: Veiculo, Kg, NovaVelocidade -> {V,F}
% calcula a nova velocidade do veículo de acordo com o peso da encomenda.
novaVelocidade(Veiculo, Kg, NovaVelocidade) :-
    Kg>1,
    velocidade(Veiculo,V),
    velocidadeAux(Veiculo, Kg, Velocidade),
    NovaVelocidade is V+Velocidade.
    

% velocidadeAux: Veiculo, Kg, Soma -> {V,F}
% de acordo com o peso da encomenda vai subtraindo o valor certo.
velocidadeAux(Veiculo, Kg, 0) :- Kg<1.
velocidadeAux(carro, Kg, Minus) :-
    Kg>=1,
    NovoPeso is Kg-1,
    velocidadeAux(carro, NovoPeso,N), 
    Minus is (N-0.1).

velocidadeAux(mota, Kg, Minus) :-
    Kg>=1,
    NovoPeso is Kg-1,
    velocidadeAux(mota, NovoPeso,N),
    Minus is (N-0.5).

velocidadeAux(bicicleta, Kg, Minus) :-
    Kg>=1,
    NovoPeso is Kg-1,
    velocidadeAux(bicicleta, NovoPeso,N), 
    Minus is (N-0.7).



% novoTempoEntrega: TempoCliente, TempoEstimado -> {V,F}
% dado um tempo maximo de cliente, verifica se o tempo estimado está dentro do prazo.
novoTempoEntrega(TempoCliente, TempoEstimado) :- TempoCliente >= TempoEstimado.
novoTempoEntrega(_,_) :- write('Tempo de Entrega requerido impossivel!\n').



% defineTransporte: Peso, IDVeiculo, Veiculo -> {V,F}
% procura um veiculo disponivel cujo pesoMaximo não ultrapasse o peso passado como argumento.
defineTransporte(Peso, IDVeiculo, Veiculo) :- 
    veiculo(IDVeiculo,Veiculo,0),
    peso(Veiculo, PesoMax),
    Peso =< PesoMax.




% predicados auxiliares

inverso(Xs,Ys) :- inverso(Xs,[],Ys).
inverso([],Xs,Xs).
inverso([X|Xs],Ys,Zs) :- inverso(Xs,[X|Ys],Zs).

seleciona(E,[E|Xs],Xs).
seleciona(E,[X|XS],[X|Ys]) :- seleciona(E,Xs,Ys).



% calculaTempo: Distancia, Velocidade, Tempo -> {V,F}
% calcula o tempo que demora a percorrer a Distancia dada uma Velocidade.
calculaTempo(Distancia, Velocidade, Tempo) :- Tempo is Distancia/Velocidade.


% calculaCusto: Lista, Custo -> {V,F}
% calcula o custo de um dado caminho.
calculaCusto([X],0).
calculaCusto([H,Y|T], N) :-
    distancia(H,Y,Custo),
    calculaCusto([Y|T], Soma),
    N is Soma+Custo.
calculaCusto([H,Y|T], N) :-
    distancia(Y,H,Custo),
    calculaCusto([Y|T], Soma),
    N is Soma+Custo.


% calculaTempo: Lista, Tempo -> {V,F}
% calcula o tempo de um dado caminho.
calculaTempo([],0).
calculaTempo([H|T], N) :- calculaTempoAux(T,N).
calculaTempoAux([],0).
calculaTempoAux([H|T], N) :-
    tempo(H,Tempo),
    calculaTempoAux(T,X),
    N is X+Tempo.





% ------------------------------------- CIRCUITOS --------------------------------------

% maiorNumEntregasCircuito: (Id,Quantos) -> {V,F}
% descobre qual o circuito com o maior mumero de entregas associado.
maiorNumEntregasCircuito(Res) :-
    quantosCircuitos(Max),
    maiorNumEntregasAux(Max,0,Pares),
    maxPar(Pares,Res).

maiorNumEntregasAux(Max,Max,[]).
maiorNumEntregasAux(Max, Act, [(ID,Tamanho)|T]) :-
    ID is Act+1,
    findall((IDEntrega), circuitoEntrega(IDEntrega,ID), IDs),
    remove_duplicates(IDs,Res),
    length(Res, Tamanho),
    Next is Act+1,
    maiorNumEntregasAux(Max, Next, T).
    
    

%quantosCircuitos: Quantos -> {V,F}
% calcula quantos circuitos estão disponíveis na base de conhecimento.
quantosCircuitos(Quantos) :-
    findall((N), circuito(N,_), IDs),
    remove_duplicates(IDs,Res),
    max_list(Res,Quantos).



% maiorPesoCircuito: (Id,Peso) -> {V,F}
% calcula o circuito com as encomendas mais pesadas.
maiorPesoCircuito(Res) :-
    quantosCircuitos(Max),
    maiorPesoAux(Max,0,Pares),
    maxPar(Pares,Res).

maiorPesoAux(Max,Max,[]).
maiorPesoAux(Max, Act, [(ID,Soma)|T]) :-
    ID is Act+1,
    findall((IDEntrega), circuitoEntrega(IDEntrega,ID), IDs),
    remove_duplicates(IDs,Res),

    maplist(getPeso(),Res,ListaPesos),
    sumlist(ListaPesos,Soma),

    Next is Act+1,
    maiorPesoAux(Max, Next, T).



% maiorVolumeCircuito: (Id,Peso) -> {V,F}
% calcula o circuito com as encomendas mais volumosas.
maiorVolumeCircuito(Res) :-
    quantosCircuitos(Max),
    maiorVolumeAux(Max,0,Pares),
    maxPar(Pares,Res).

maiorVolumeAux(Max,Max,[]).
maiorVolumeAux(Max, Act, [(ID,Soma)|T]) :-
    ID is Act+1,
    findall((IDEntrega), circuitoEntrega(IDEntrega,ID), IDs),
    remove_duplicates(IDs,Res),

    maplist(getVolume(),Res,ListaVolumes),
    sumlist(ListaVolumes,Soma),

    Next is Act+1,
    maiorVolumeAux(Max, Next, T).





% circuitoMaisCurto: Circuito1, Circuito2, CircuitoMaisCurto -> {V,F}
% Dado dois circuitos, calcula o mais curto dos dois.
circuitoMaisCurto((ID1,Circuito1,Primeiro), (ID2,Circuito2,Segundo), (ID,Circuito)) :-
    calculaCusto(Circuito1, Primeiro),
    calculaCusto(Circuito2, Segundo),

    minPar([(ID1,Primeiro),(ID2,Segundo)], (ID,Circuito)).
    

% circuitoMaisRápido: Circuito1, Circuito2, CircuitoMaisRapido -> {V,F}
% Dado dois circuitos, calcula o mais rápido dos dois.
circuitoMaisRapido((ID1,Circuito1,Tempo1), (ID2,Circuito2,Tempo2), (ID,Circuito)) :-
    calculaTempo(Circuito1, Tempo1),
    calculaTempo(Circuito2, Tempo2),

    minPar([(ID1,Tempo1),(ID2,Tempo2)],(ID,Circuito)).