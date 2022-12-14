:- include('auxiliares-1fase.pl').

% Predicado de Teste
testeAll([P1,P2,P3,P4,P5,P6,P7,P8,P9,P10]) :-
      estafetaMaisEcologico(P1),
      entregasDoCliente(1,[17,40,21,42,90],P2),
      clientesDeUmEstafeta(1,P3),
      faturaUmDia([2021,2,12],P4),
      maiorVolumeFreguesia(P5),
      mediaSatisfacaoEstafeta(1,P6),
      totalEntregasPorTransporte([2021,1,1],[2021,12,30],P7),
      numeroEntregasIntervalo([2021,1,1],[2021,12,30],P8),
      estadoEntregasIntervalo([2021,1,1],[2021,12,30],P9),
      pesoBrutoDeUmEstafetaUmDia(1,[2021,6,14],P10).

% PREDICADO 1
% Devolve o estafeta mais ecológico.
%     Avalia o número máximo de estafetas na BD.
%     Devolve uma lista de pares de Estafetas e a sua média ecológica
%     Retira o par máximo da lista
%     Recupera o nome do estafeta.
estafetaMaisEcologico(Nome) :- 
      idMaximoEstafeta(Max),
      estafetaMaisEcologico(0,Max,Res),
      maxPar(Res,(ID,_)),
      getEstafeta(ID,Nome).

% PREDICADO 1 - AUXILIAR
% Gera a lista de pares de Estafetas e a sua média ecologica.
%     Recupera a lista de Entregas de um estafeta.
%     Devolve a lista de veiculos utilizados pelo estafeta.
%     Cálcula a sua média.
%     Repete para o próximo estafeta.
estafetaMaisEcologico(N,Max,_) :- N is Max+1.
estafetaMaisEcologico(Indice,Max,[(Indice,Media)|T]) :- 
      entregasDeUmEstafeta(Indice,Entregas),
      maplist(getEcologiaEntrega(),Entregas,Ecologias),
      mediaLista(Ecologias,Media),
      N is Indice+1,
      estafetaMaisEcologico(N,Max,T).



% PREDICADO 2
% Gera uma lista de entregas e os seus estafetas de um cliente.
%     Percorre a lista de Id's de Entregas.
%     Avalia se as entregas pertencem ao cliente.
%     Devolve o nome do estafeta, com o id da entrega correspondente.
entregasDoCliente(_,[],[]).
entregasDoCliente(IdCliente,[IdEntrega|Entregas],[(Nome,IdEntrega)|T]) :-
      entrega(IdEntrega,IdEstafeta,IdCliente),
      estafeta(IdEstafeta,Nome,_),
      entregasDoCliente(IdCliente,Entregas,T).
entregasDoCliente(IdCliente,[_|Entregas],T) :-
      entregasDoCliente(IdCliente,Entregas,T).      



% PREDICADO 3
% Devolve o nome dos clientes que um estafeta fez entregas
%     Recupera a lista de Ids de todos os clientes que o estafeta "serviu"
%     Remove duplicados
%     Devolve a lista de nomes dos clientes.
clientesDeUmEstafeta(IdEstafeta,Res) :-
      findall((Cliente), entrega(_,IdEstafeta,Cliente),Lista),
      remove_duplicates(Lista,L),
      getClientes(L,Res).



% PREDICADO 4
% Devolve o valor faturado pela empresa num dia
%     Devolve todas as entregas na BD.
%     Filtra a lista de entregas, correspondentes ao dia fornecido.
%     Obtém o preco das entregas filtradas.
%     Somatório final.
faturaUmDia(Data,Res) :-
      findall((IDEntrega), entrega(IDEntrega,_,_),AllEntregas),
      entregasDeUmDia(Data,AllEntregas,EntregasFiltradas),
      maplist(getPreco(),EntregasFiltradas,ListaPrecos),
      sumlist(ListaPrecos,Res).



% PREDICADO 5
% Devolve a freguesia com maior volume de entregas.
%     Lista os volumes de cada freguesia.
%     Obtém o maior par
%     Traduz o nome.
maiorVolumeFreguesia(Res) :-
      listaVolumesPorFreguesia(ListaPares),
      maxPar(ListaPares,(Id,_)),
      freguesia(Id,Res).

% PREDICADO 5 - AUXILIAR
% listaVolumesPorFreguesia: Lista de Pares (Freguesia, Volume) -> {V,F}
% Recupera a lista de de pares de todas as freguesias na BD, com os seus volumes.
%     Percorre de 0 a Max Freguesias.
%     Obtém o volume de cada freguesia.
%     Devolve uma lista de pares.
listaVolumesPorFreguesia(Lista) :-
      idMaximoFreguesia(Max),
      listaVolumesPorFreguesia(0,Max,Lista).
listaVolumesPorFreguesia(Max,Max,[]).
listaVolumesPorFreguesia(N,Max,[(Freg,Vol)|T]) :-
      Freg is N+1,
      volumePorFreguesia(Freg,Vol),
      listaVolumesPorFreguesia(Freg,Max,T).



% PREDICADO 6
% Calcula a média da satisfação geral dos clientes de um Estafeta.
%     Devolve todas as entregas de um Estafeta
%     Obtém a satisfação de todas as entregas
%     Calcula a média final.
mediaSatisfacaoEstafeta(IdEstafeta,Res) :-
      entregasDeUmEstafeta(IdEstafeta,ListaEntregas),
      maplist(getSatisfacao(),ListaEntregas,ListaSats),
      mediaLista(ListaSats,Res).



%PREDICADO 7
% Devolve uma lista com o número de entregas por tipo de transporte, num dado intervalo
%     Obtém a lista de todas as entregas.
%     Filtra a lista num intervalo.
%     Obtém a lista de tipos de veiculos usado.
%     Calcula os 3 tipos de transporte.
totalEntregasPorTransporte(DataInicio,DataFinal,[("bicicleta",Bic),("carro",Carro),("mota",Mota)]) :-
      findall((IDEntrega),entrega(IDEntrega,_,_),AllEntregas),
      entregasNumIntervalo(DataInicio,DataFinal,AllEntregas,Filtrado),
      maplist(getTipoVeiculoEntrega(),Filtrado,ListaTipos),
      count(ListaTipos,bicicleta,Bic),      
      count(ListaTipos,carro,Carro),
      count(ListaTipos,moto,Mota).



% PREDICADO 8
% Número de Entregas num intervalo de tempo.
%     Obtém a lista de todas as entregas.
%     Filtra a lista num intervalo.
%     Cálcula o número de entregas.
numeroEntregasIntervalo(DataIncio,DataFinal,Res) :-
      findall((IDEntrega),entrega(IDEntrega,_,_),AllEntregas),
      entregasNumIntervalo(DataIncio,DataFinal,AllEntregas,Filtro),
      length(Filtro,Res).



% PREDICADO 9
% Devolve uma lista com o número de entregas pelo seu estado, num dado intervalo
%     Obtém a lista de todas as entregas.
%     Filtra a lista num intervalo.
%     Obtém a lista dos estados das entregas.
%     Calcula os 3 tipos de estados.
estadoEntregasIntervalo(DataInicio,DataFinal,[("entregues",Entregue),("nao entregues",NaoEntregues)]) :-
      findall((IDEntrega),entrega(IDEntrega,_,_),AllEntregas),
      entregasNumIntervalo(DataInicio,DataFinal,AllEntregas,Filtro),
      maplist(getEstado(),Filtro,ListaEstados),
      count(ListaEstados,0,Entregue),
      count(ListaEstados,1,Cancelado),
      count(ListaEstados,2,NEntregues),
      NaoEntregues is Cancelado+NEntregues.



% PREDICADO 10
% Devolve o valor do peso bruto das entregas realizadas por um estafeta num dia.
%     Devolve a lista de entregas de um estafeta.
%     Filtra a lista de entregas, correspondentes ao dia fornecido.
%     Realiza o somatório da lista de entregas filtrada.
pesoBrutoDeUmEstafetaUmDia(IdEstafeta,Data,Res) :- 
      entregasDeUmEstafeta(IdEstafeta,Entregas),
      entregasDeUmDia(Data,Entregas,EntregasFinais),
      maplist(getPeso(),EntregasFinais,ListaPesos),
      sumlist(ListaPesos,Res).
