:-style_check(-singleton).
:-style_check(-discontiguous).

:-include('grafo.pl').
:-include('travessias.pl').
:-include('auxiliares-1fase.pl').
:-include('auxiliares-2fase.pl').


% ----------------------------  ALGORITMO DFS ------------------------
% calculaCaminhoDFS: IDEntrega -> {V,F}
calculaCaminhoDFS(IDEntrega) :-
    getPeso(IDEntrega,Peso),
    getTempoDeEntrega(IDEntrega, TempoMaximo),
    getRua(IDEntrega, Rua),

    write('\nPeso da Encomenda: '), write(Peso), write(' Kg.\n'),
    write('Tempo de Entrega Max: '), write(TempoMaximo), write(' minutos.\n'),
    write('Ponto de Entrega: '), write(Rua), write('.\n\n'),

    mainDFS(Rua,Peso,TempoMaximo).



% ----------------------------  ALGORITMO BFS ------------------------
% calculaCaminhoBFS: IDEntrega -> {V,F}
calculaCaminhoBFS(IDEntrega) :-
    getPeso(IDEntrega,Peso),
    getTempoDeEntrega(IDEntrega, TempoMaximo),
    getRua(IDEntrega, Rua),

    write('\nPeso da Encomenda: '), write(Peso), write(' Kg.\n'),
    write('Tempo de Entrega Max: '), write(TempoMaximo), write(' minutos.\n'),
    write('Ponto de Entrega: '), write(Rua), write('.\n\n'),

    mainBFS(Rua,Peso,TempoMaximo).



% ----------------------------  ALGORITMO A* ------------------------
% calculaCaminhoEstrela -> {V,F}
calculaCaminhoEstrela() :-
    getPeso(13,Peso),
    getTempoDeEntrega(13, TempoMaximo),
    getRua(13, Rua),

    write('\nID da Entrega: 13.\n'),
    write('Peso da Encomenda: '), write(Peso), write(' Kg.\n'),
    write('Tempo de Entrega Max: '), write(TempoMaximo), write(' minutos.\n'),
    write('Ponto de Entrega: '), write(Rua), write('.\n\n'),

    mainEstrela(Peso,TempoMaximo).



% ----------------------------  ALGORITMO GULOSA ------------------------
% calculaCaminhoGulosa -> {V,F}
calculaCaminhoGulosa() :-
    getPeso(13,Peso),
    getTempoDeEntrega(13, TempoMaximo),
    getRua(13, Rua),

    write('\nID da Entrega: 13.\n'),
    write('\nPeso da Encomenda: '), write(Peso), write(' Kg.\n'),
    write('Tempo de Entrega Max: '), write(TempoMaximo), write(' minutos.\n'),
    write('Ponto de Entrega: '), write(Rua), write('.\n\n'),

    mainGulosa(Peso,TempoMaximo).







% --------------------------------------------- EXECUTA ALGORTIMO AESTRELA -------------------------------------------------
% mainEstela: Peso, TempoMaximo -> {V,F}
mainEstrela( Peso, TempoMaximo) :- 
    defineTransporte(Peso, IDVeiculo, Veiculo), 
    write('IDVeiculo: '),write(IDVeiculo), write(' ('), write(Veiculo), write(')\n'),

    novaVelocidade(Veiculo, Peso, NovaVel),
    write('VelocidadeMedia: '), write(NovaVel),write(' km/h.\n\n'),

    goal(X),
    volta(Inicio),
    resolve_aestrela(Inicio,[H|T]/Custo), 
    resolve_aestrelaInverso(X, CaminhoVolta/CustoVolta),

    write('Caminhos: \n'),
    write('   Ida: '), write([H|T]), write('.\n'), 
    write('   Volta: '), write(CaminhoVolta), write('\n\n'),
    write('Custo Ida: '), write(Custo), write(' km.\n'),
    write('Custo Total: '), Soma is Custo+CustoVolta, write(Soma), write(' km.\n\n'),
    
    calculaTempo(Custo,NovaVel,Tempo),
    NewTempo is Tempo*60,
    novoTempoEntrega(TempoMaximo, NewTempo), 
    
    write('Tempo do Cliente: '), write(TempoMaximo), write(' minutos.\n'), 
    write('Tempo Real de Entrega: '), write(NewTempo), write(' minutos.\n').





% --------------------------------------------- EXECUTA ALGORTIMO GULOSA -------------------------------------------------
% mainGulosa: Peso, TempoMaximo -> {V,F}
mainGulosa(Peso, TempoMaximo) :- 
    defineTransporte(Peso, IDVeiculo, Veiculo), %aqui já define um veiculo
    write('IDVeiculo: '),write(IDVeiculo), write(' ('), write(Veiculo), write(')\n'),

    novaVelocidade(Veiculo, Peso, NovaVel),
    write('VelocidadeMedia: '), write(NovaVel), write(' km/h.\n\n'),

    goal(X),
    volta(Inicio),
    resolve_gulosa(Inicio, [H|T]/Custo),
    resolve_gulosaInverso(X, CaminhoVolta/CustoVolta),


    write('Caminhos: \n'),
    write('   Ida: '), write([H|T]), write('.\n'), 
    write('   Volta: '), write(CaminhoVolta), write('\n\n'),
    write('Custo Ida: '), write(Custo), write(' km.\n'),
    write('Custo Total: '), Soma is Custo+CustoVolta, write(Soma), write(' km.\n\n'),

    calculaTempo(Custo,NovaVel,Tempo),
    NewTempo is Tempo*60,
    novoTempoEntrega(TempoMaximo, NewTempo),
    
    write('Tempo do Cliente: '), write(TempoMaximo), write(' minutos.\n'), 
    write('Tempo Real de Entrega: '), write(NewTempo), write(' minutos.\n').






% --------------------------------------------- EXECUTA ALGORTIMO DFS -------------------------------------------------
% mainDFS: Rua, Peso, TempoMaximo -> {V,F}
mainDFS(Rua, Peso, TempoMaximo) :- 
    defineTransporte(Peso, IDVeiculo, Veiculo), %aqui já define um veiculo
    write('IDVeiculo: '),write(IDVeiculo), write(' ('), write(Veiculo), write(')\n'),

    novaVelocidade(Veiculo, Peso, NovaVel),
    write('VelocidadeMedia: '), write(NovaVel),write(' km/h.\n\n'),

    volta(Inverso),
    resolve_pp_c(Inverso,Rua, [H|T], Custo), 
    goal(Dest),
    dfsInverso(Rua,CaminhoVolta, CustoVolta),

    write('Caminhos: \n'),
    write('   Ida: '), write([H|T]), write('.\n'), 
    write('   Volta: '), write(CaminhoVolta), write('\n\n'),
    write('Custo Ida: '), write(Custo), write(' km.\n'),
    write('Custo Total: '), Soma is Custo+CustoVolta, write(Soma), write(' km.\n\n'),

    calculaTempo(Custo,NovaVel,Tempo),
    NewTempo is Tempo*60,
    novoTempoEntrega(TempoMaximo, NewTempo), %aqui verifica se o tempo dado pelo cliente é aceite ou não

    write('Tempo do Cliente: '), write(TempoMaximo), write(' minutos.\n'), 
    write('Tempo Real de Entrega: '), write(NewTempo), write(' minutos.\n').






% --------------------------------------------- EXECUTA ALGORTIMO BFS -------------------------------------------------
% mainBFS: Rua, Peso, TempoMaximo -> {V,F}
mainBFS(Rua,Peso, TempoMaximo) :- 
    defineTransporte(Peso, IDVeiculo, Veiculo), 
    write('IDVeiculo: '),write(IDVeiculo), write(' ('), write(Veiculo), write(')\n'),

    novaVelocidade(Veiculo, Peso, NovaVel),
    write('VelocidadeMedia: '), write(NovaVel),write(' km/h.\n\n'),

    volta(Inverso),
    bfs(Inverso, Rua, [H|T]),
    bfsInverso(Rua,Inverso,CaminhoVolta),

    calculaCusto([H|T], Custo),
    calculaCusto(CaminhoVolta,CustoVolta),
    
    write('Caminhos: \n'),
    write('   Ida: '), write([H|T]), write('.\n'), 
    write('   Volta: '), write(CaminhoVolta), write('\n\n'),
    write('Custo Ida: '), write(Custo), write(' km.\n'),
    write('Custo Total: '), Soma is Custo+CustoVolta, write(Soma), write(' km.\n\n'),

    calculaTempo(Custo, NovaVel,Tempo),
    NewTempo is Tempo*60,
    novoTempoEntrega(TempoMaximo, NewTempo), %aqui verifica se o tempo dado pelo cliente é aceite ou não

    write('Tempo do Cliente: '), write(TempoMaximo), write(' minutos.\n'), 
    write('Tempo Real de Entrega: '), write(NewTempo), write(' minutos.\n').










% ----------------------------------------- CÁLCULO DE ESTATÍSTICAS --------------------------------------

% mainMaiorNumEntregas -> {V,F}
% cálculo do circuito com maior numero de encomendas.
mainMaiorNumEntregas() :-
    maiorNumEntregasCircuito((ID,Quantas)),
    circuito(ID,Caminho),

    write('Circuito ID:  '), write(ID), write('.\n'), 
    write('Caminho: '), write(Caminho), write('.\n'),
    write('Numero de Entregas: '), write(Quantas),write('.\n').


% mainMaiorPesoEntregas -> {V,F}
% cálculo do circuito com maior peso de encomendas.
mainMaiorPesoEntregas() :-
    maiorPesoCircuito((ID,Quantas)),
    circuito(ID,Caminho),

    write('Circuito ID:  '), write(ID), write('.\n'), 
    write('Caminho: '), write(Caminho), write('.\n'),
    write('Peso Total das Entregas: '), write(Quantas),write(' Kg.\n').


% mainMaiorVolumeEntregas -> {V,F}
% cálculo do circuito com maior volume de encomendas.
mainMaiorVolumeEntregas() :-
    maiorVolumeCircuito((ID,Quantas)),
    circuito(ID,Caminho),

    write('Circuito ID:  '), write(ID), write('.\n'), 
    write('Caminho: '), write(Caminho), write('.\n'),
    write('Volume Total das Entregas: '), write(Quantas),write(' dm3.\n').







% -------------------------------- COMAPRAÇÃO DE CIRCUITOS -----------------------------------------

% mainComparaDistanciaCircuitos: ID1, ID2 -> {V,F}
% comparação entre dois circuitos em relação à sua extensão.
mainComparaDistanciaCircuitos(ID1,ID2) :-
    circuito(ID1, Caminho1), 
    circuito(ID2, Caminho2), 

    circuitoMaisCurto((ID1,Caminho1,D1), (ID2,Caminho2,D2), (ID,Distancia)),

    write('Caminho '), write(ID1), write(': '), write(Caminho1), write('.\n'),
    write('Distancia: '), write(D1), write(' km.\n\n'), 
    write('Caminho '), write(ID2), write(': '), write(Caminho2), write('.\n'),
    write('Distancia: '), write(D2), write(' km.\n\n'), 
    write('Caminho Mais Curto: '), write(ID), write(' ('),write(Distancia),write(' km).\n').


% mainComparaTempoCircuitos: ID1, ID2 -> {V,F}
% comparação entre dois circuitos em relação ao tempo de percorrer os mesmos.
mainComparaTempoCircuitos(ID1,ID2) :-
    circuito(ID1, Caminho1), 
    circuito(ID2, Caminho2), 

    circuitoMaisRapido((ID1,Caminho1,T1), (ID2,Caminho2,T2), (ID,Tempo)),

    write('Caminho '), write(ID1), write(': '), write(Caminho1), write('.\n'),
    write('Tempo: '), write(T1), write(' minutos.\n\n'),
    write('Caminho '), write(ID2), write(': '), write(Caminho2), write('.\n'),
    write('Tempo: '), write(T2), write(' minutos.\n\n'),
    write('Caminho Mais Rapido: '), write(ID), write(' ('), write(Tempo), write(' minutos).\n').