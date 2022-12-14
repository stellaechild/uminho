% Localização da Sede da Empresa GreenDistribution
volta(ruaDaUniversidade).


% objetivo das pesquisas informadas
goal(largoSaoPedro).



% distancia: Rua1, Rua2, Kms -> {V,F}
% ligações entre as várias ruas e os kms entre as mesmas.
distancia(ruaDaUniversidade, ruaDaLage,               2.6).
distancia(ruaDaUniversidade, avenidaDomJoaoII,        1.8). 
distancia(ruaDaUniversidade, ruaSaoVicente,           3.2).
distancia(ruaDaUniversidade, avenidaPadreJulioFragata,2.6).

distancia(ruaDaLage,ruaDaBoavista, 7.1).

distancia(avenidaDomJoaoII, ruaDaIgreja,          1.2).
distancia(avenidaDomJoaoII, ruaConselheiroLobato, 3.9).

distancia(ruaSaoVicente, ruaConselheiroLobato,  2.9 ).
distancia(ruaSaoVicente, ruaConselheiroJanuario,0.27).

distancia(avenidaPadreJulioFragata, ruaConselheiroJanuario, 1.8).

distancia(ruaConselheiroJanuario, avenidaDaLiberdade,1.6).
distancia(ruaConselheiroJanuario, ruaDaBoavista,     2.3).
distancia(ruaConselheiroJanuario, campoDasHortas,    2.7).

distancia(ruaDaIgreja, ruaDaMouta, 4).

distancia(ruaConselheiroLobato, ruaDaMouta,             2).
distancia(ruaConselheiroLobato, ruaAntonioFerreiraRito, 3.6).

distancia(ruaDaMouta, largoSaoPedro,            0.65).
distancia(ruaAntonioFerreiraRito, largoSaoPedro,3.1 ).
distancia(avenidaDaLiberdade, largoSaoPedro,    3.3 ).
distancia(campoDasHortas,largoSaoPedro,         3.1 ).
distancia(ruaDaBoavista, largoSaoPedro,         4.5 ).



% tempo: Rua, Tempo -> {V,F}
% Estimativa de tempo (em minutos) para a resolução de algoritmos de pesquisa informada.
% Tempo equivale ao tempo necessário para viajar desde o nodo em questão até ao nodo largoSaoPedro.
tempo(ruaDaUniversidade, 13).
tempo(avenidaDomJoaoII, 9). 
tempo(ruaSaoVicente, 12).
tempo(avenidaPadreJulioFragata, 7).
tempo(ruaDaLage, 12).
tempo(ruaDaIgreja, 9).
tempo(ruaConselheiroLobato, 5).
tempo(ruaConselheiroJanuario, 10).
tempo(ruaDaMouta, 1).
tempo(ruaAntonioFerreiraRito, 7).
tempo(avenidaDaLiberdade, 7).
tempo(campoDasHortas, 8).
tempo(ruaDaBoavista, 10).
tempo(largoSaoPedro, 0).





% circuito: ID, Circuito -> {V,F}
% Para cada ID associa um possível circuito de entrega.
circuito(1, [ruaDaUniversidade,ruaSaoVicente]). 
circuito(2, [ruaDaUniversidade,avenidaDomJoaoII]). 
circuito(3, [ruaDaUniversidade,ruaDaLage]). 
circuito(4, [ruaDaUniversidade,avenidaPadreJulioFragata]).

circuito(5, [ruaDaUniversidade,avenidaDomJoaoII,ruaDaIgreja]). 
circuito(6, [ruaDaUniversidade,avenidaDomJoaoII,ruaConselheiroLobato]). 

circuito(7, [ruaDaUniversidade,ruaSaoVicente,ruaConselheiroLobato]).
circuito(8, [ruaDaUniversidade,ruaSaoVicente,ruaConselheiroJanuario]). 

circuito(9, [ruaDaUniversidade,avenidaPadreJulioFragata,ruaConselheiroJanuario]).

circuito(10,[ruaDaUniversidade,avenidaDomJoaoII,ruaDaIgreja,ruaDaMouta]). 

circuito(11,[ruaDaUniversidade,avenidaDomJoaoII,ruaConselheiroLobato,ruaDaMouta]).
circuito(12,[ruaDaUniversidade,avenidaDomJoaoII,ruaConselheiroLobato,ruaAntonioFerreiraRito]).

circuito(13,[ruaDaUniversidade,avenidaPadreJulioFragata,ruaConselheiroJanuario,avenidaDaLiberdade]).
circuito(14,[ruaDaUniversidade,avenidaPadreJulioFragata,ruaConselheiroJanuario,campoDasHortas]).
circuito(15,[ruaDaUniversidade,avenidaPadreJulioFragata,ruaConselheiroJanuario,ruaDaBoavista]).

circuito(16,[ruaDaUniversidade,avenidaDomJoaoII,ruaDaIgreja,ruaDaMouta,largoSaoPedro]).
circuito(17,[ruaDaUniversidade,ruaSaoVicente,ruaConselheiroLobato,ruaAntonioFerreiraRito,largoSaoPedro]).
circuito(18,[ruaDaUniversidade,avenidaPadreJulioFragata,ruaConselheiroJanuario,avenidaDaLiberdade,largoSaoPedro]).
circuito(19,[ruaDaUniversidade,avenidaPadreJulioFragata,ruaConselheiroJanuario,campoDasHortas,largoSaoPedro]).




% circuitoEntrega: IDEntrega, IDCircuito -> {V,F}
% Para cada ID de entregas da base de conhecimento, associa um ID de circuito anteriormente definido.
circuitoEntrega(1,1).
circuitoEntrega(2,5).
circuitoEntrega(3,10).
circuitoEntrega(4,16).
circuitoEntrega(5,2).
circuitoEntrega(6,15).
circuitoEntrega(7,12).
circuitoEntrega(8,15).
circuitoEntrega(9,6).
circuitoEntrega(10,12).
circuitoEntrega(11,5).
circuitoEntrega(12,6).
circuitoEntrega(13,17).
circuitoEntrega(14,5).
circuitoEntrega(15,10).
circuitoEntrega(16,6).
circuitoEntrega(19,6).
circuitoEntrega(20,5).
circuitoEntrega(21,15).
circuitoEntrega(22,9).
circuitoEntrega(23,10).
circuitoEntrega(24,6).
circuitoEntrega(25,14).
circuitoEntrega(26,6).
circuitoEntrega(27,3).
circuitoEntrega(28,14).
circuitoEntrega(29,3).
circuitoEntrega(30,4).
circuitoEntrega(31,13).
circuitoEntrega(32,1).
circuitoEntrega(37,4).
circuitoEntrega(38,5).
circuitoEntrega(39,10).
circuitoEntrega(40,3).
circuitoEntrega(41,15).
circuitoEntrega(42,5).
circuitoEntrega(43,1).
circuitoEntrega(44,8).
circuitoEntrega(45,1).
circuitoEntrega(46,16).
circuitoEntrega(47,14).
circuitoEntrega(48,13).
circuitoEntrega(49,17).
circuitoEntrega(50,13).