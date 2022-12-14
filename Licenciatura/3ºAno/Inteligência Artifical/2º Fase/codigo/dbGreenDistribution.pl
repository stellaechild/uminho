% TRABALHO PRÁTICO DE INTELIGÊNCIA ARTIFICIAL 2021/2022.
%
% Base de Dados para a Companhia GreenDistribution.
% Esta contém todas as entregas, clientes e estafetas, assim como cidades e freguesias onde ocorreram.


% Freguesias: Freguesias onde são entregues encomendas.
% freguesia: #IdFreguesia, Freguesia -> {V,F}
freguesia(1, 'Gualtar'    ).
freguesia(2, 'Lomar'      ).
freguesia(3, 'São Vítor'  ).
freguesia(4, 'São Vicente').
freguesia(5, 'Sé'         ).
freguesia(6, 'São Lázaro' ).
freguesia(7, 'Ferreiros'  ).
freguesia(8, 'Lamaçães'   ).

% cliente: Clientes que realizaram pedidos.
% CLIENTE TEM DE TER MAIS DE 18 ANOS
% cliente: #IdCliente, Nome , Idade -> {V,F}
cliente(1, 'Carlos',    20).
cliente(2, 'André',     26).
cliente(3, 'Joana',     19).
cliente(4, 'Vitor',     42).
cliente(5, 'Glória',    46).
cliente(6, 'Joãozinho', 12).
cliente(7, 'Francisco', 33).
cliente(8, 'Beatriz',   29).
cliente(9, 'Maria',     34).
cliente(10,'Rita',      22).
cliente(11,'David',     31).
cliente(12,'Marco',     46).


% estafeta: Estafetas que realizam as entregas, cada um "pertence" a uma cidade.
% ESTAFETA TEM DE TER MAIS DE 18 ANOS E PODE UTILIZAR QUALQUER VEICULO DISPONIVEL
% estafeta: #IdEstafeta, Nome, Idade , #IdCidade -> {V,F}
estafeta(1, 'Cláudio', 30).
estafeta(2, 'Rita',    26).
estafeta(3, 'Samuel',  22).
estafeta(4, 'António', 31).
estafeta(5, 'Rúben',   27).
estafeta(6, 'Pedro',   25).
estafeta(7, 'Catarina',17).
estafeta(8, 'Mariana', 26).


% entrega: Informação relacionada às entregas. Esta apenas tem os Ids dos atores envolvidos e a sua localização. Consultar "info(ID,..)" para obter mais informação acerca da entrega.
% entrega: #IdEntrega, #IdEstafeta, #IdCliente -> {V,F}
entrega(1 ,7,4 ).
entrega(2 ,8,6 ).
entrega(3 ,4,4 ).
entrega(4 ,8,4 ).
entrega(5 ,5,10).
entrega(6 ,2,7 ).
entrega(7 ,5,4 ).
entrega(8 ,6,1 ).
entrega(9 ,2,10).
entrega(10,4,3 ).
entrega(11,1,6 ).
entrega(12,4,12).
entrega(13,4,9 ).
entrega(14,5,6 ).
entrega(15,3,6 ).
entrega(16,7,12).
entrega(17,5,1 ).
entrega(18,6,4 ).
entrega(19,7,4 ).
entrega(20,1,3 ).
entrega(21,6,5 ).
entrega(22,6,11).
entrega(23,5,4 ).
entrega(24,3,2 ).
entrega(25,1,4 ).
entrega(26,4,8 ).
entrega(27,2,2 ).
entrega(28,6,3 ).
entrega(29,6,7 ).
entrega(30,2,3 ).
entrega(31,3,10).
entrega(32,8,10).
entrega(33,4,9 ).
entrega(34,8,2 ).
entrega(35,3,12).
entrega(36,6,9 ).
entrega(37,2,10).
entrega(38,6,2 ).
entrega(39,1,5 ).
entrega(40,3,1 ).
entrega(41,8,4 ).
entrega(42,3,1 ).
entrega(43,1,4 ).
entrega(44,1,3 ).
entrega(45,4,11).
entrega(46,5,4 ).
entrega(47,1,6 ).
entrega(48,6,12).
entrega(49,7,10).
entrega(50,8,10).




% info: Informação adicional acerca da entrega. O Id da infomação terá que corresponder ao Id da Entrega.
% info: #IdEntrega, #IdVeiculo,TempoDeEntrega(m), Preço(€), DataDeEntrega([A,M,D,H,m]), Satisfacção(0-5), #IdEstado, #IdFreguesia, Rua, Peso(kg), Volume(dm3) -> {V,F}
info(1 ,7,94 ,11,[2021,8 ,2 ,1 ,3 ],1.6,0,4,ruaSaoVicente         ,12,10).
info(2 ,1,68 ,6 ,[2021,10,4 ,21,30],2.3,0,8,ruaDaIgreja              ,3 ,3 ).
info(3 ,1,66 ,7 ,[2021,4 ,6 ,19,56],1.4,0,2,ruaDaMouta               ,5 ,5 ).
info(4 ,4,36 ,5 ,[2021,11,1 ,0 ,23],2.7,0,2,largoSaoPedro         ,1 ,1 ).
info(5 ,7,31 ,7 ,[2021,3 ,18,1 ,35],4.3,0,8,avenidaDomJoaoII        ,5 ,5 ).
info(6 ,3,80 ,6 ,[2021,7 ,23,6 ,8 ],4.9,0,5,ruaDaBoavista           ,3 ,3 ).
info(7 ,8,7  ,10,[2021,6 ,5 ,22,32],4.4,2,7,ruaAntonioFerreiraRito  ,10,8 ).
info(8 ,3,100,5 ,[2021,11,14,4 ,13],4.2,2,5,ruaDaBoavista            ,1 ,1 ).
info(9 ,8,94 ,9 ,[2021,11,18,12,33],0.5,0,6,ruaConselheiroLobato     ,9 ,10).
info(10,8,6  ,11,[2021,2 ,2 ,2 ,10],3.7,0,7,ruaAntonioFerreiraRito  ,13,14).
info(11,6,81 ,7 ,[2021,6 ,14,6 ,46],1.3,0,8,ruaDaIgreja               ,5 ,5 ).
info(12,3,25 ,7 ,[2021,2 ,12,1 ,16],2.7,2,6,ruaConselheiroLobato       ,5 ,5 ).
info(13,9,51 ,12,[2021,12,12,21,4 ],0.9,0,2,largoSaoPedro         ,14,90).
info(14,2,82 ,7 ,[2021,11,3 ,18,13],4.0,0,8,ruaDaIgreja              ,5 ,5 ).
info(15,4,11 ,7 ,[2021,4 ,27,0 ,18],0.1,2,2,ruaDaMouta               ,4 ,4 ).
info(16,9,47 ,13,[2021,5 ,4 ,17,30],0.7,0,6,ruaConselheiroLobato       ,16,16).
info(17,6,117,46,[2021,10,31,14,9 ],1.0,0,1,ruaDaUniversidade      ,83,80).
info(18,8,34 ,11,[2021,7 ,22,20,0 ],5.0,0,3,avenidaPadreJulioFragata,13,12).
info(19,3,48 ,6 ,[2021,9 ,26,3 ,16],4.0,0,6,ruaConselheiroLobato       ,2 ,2 ).
info(20,4,61 ,7 ,[2021,10,7 ,22,21],3.8,0,1,ruaDaLage               ,5 ,5 ).
info(21,9,27 ,13,[2021,8 ,18,0 ,1 ],4.4,0,5,ruaDaBoavista             ,17,18).
info(22,5,119,51,[2021,4 ,1 ,7 ,45],2.6,0,4,ruaConselheiroJanuario,92,92).
info(23,7,47 ,13,[2021,3 ,8 ,19,26],0.8,0,2,ruaDaMouta               ,17,20).
info(24,5,32 ,22,[2021,9 ,13,9 ,40],5.0,0,6,ruaConselheiroLobato       ,34,39).
info(25,5,18 ,10,[2021,4 ,8 ,1 ,15],5.0,0,5,campoDasHortas          ,10,11).
info(26,5,49 ,12,[2021,11,13,2 ,25],0.3,0,6,ruaConselheiroLobato       ,14,15).
info(27,9,105,10,[2021,11,11,5 ,44],3.7,0,1,ruaDaLage                ,10,11).
info(28,2,107,6 ,[2021,10,25,22,45],4.5,0,5,campoDasHortas            ,3 ,3 ).
info(29,1,28 ,6 ,[2021,1 ,18,16,59],2.6,0,1,ruaDaLage                ,2 ,2 ).
info(30,7,92 ,6 ,[2021,8 ,22,2 ,10],1.2,0,3,avenidaPadreJulioFragata,3 ,3 ).
info(31,4,96 ,7 ,[2021,2 ,6 ,0 ,33],5.0,0,6,avenidaDaLiberdade       ,4 ,4 ).
info(32,1,15 ,6 ,[2021,11,16,9 ,52],1.3,0,4, ruaSaoVicente        ,3 ,3 ).
info(33,3,64 ,7 ,[2021,5 ,17,1 ,39],4.9,0,1,ruaDaUniversidade        ,5 ,5 ).
info(34,8,120,11,[2021,7 ,14,1 ,37],4.2,1,7,ruaFreiJoseVilaca       ,13,12).
info(35,3,50 ,7 ,[2021,2 ,16,18,10],3.9,0,1,ruaDaUniversidade        ,4 ,4 ).
info(36,1,65 ,7 ,[2021,3 ,30,6 ,43],0.8,0,7,ruaFreiJoseVilaca         ,5 ,5 ).
info(37,6,40 ,22,[2021,2 ,17,18,42],5.0,0,3,avenidaPadreJulioFragata,35,32).
info(38,9,29 ,13,[2021,11,30,21,19],3.7,1,8,ruaDaIgreja               ,16,17).
info(39,8,62 ,9 ,[2021,1 ,5 ,23,1 ],4.2,0,2,ruaDaMouta             ,8 ,11).
info(40,6,76 ,43,[2021,5 ,12,18,31],4.6,0,1,ruaDaLage                ,77,78).
info(41,8,11 ,11,[2021,10,8 ,20,51],2.9,0,5,ruaDaBoavista             ,12,11).
info(42,1,33 ,6 ,[2021,8 ,31,7 ,44],2.9,2,8,ruaDaIgreja               ,2 ,2 ).
info(43,4,115,6 ,[2021,12,25,12,31],4.3,0,4,ruaSaoVicente        ,2 ,2 ).
info(44,7,99 ,12,[2021,1 ,12,23,51],3.3,0,4,ruaConselheiroJanuario  ,14,14).
info(45,4,87 ,5 ,[2021,11,13,0 ,46],0.0,0,4,ruaSaoVicente         ,1 ,1 ).
info(46,2,87 ,6 ,[2021,12,24,15,20],2.5,0,2,largoSaoPedro         ,3 ,3 ).
info(47,9,38 ,5 ,[2021,1 ,28,0 ,36],1.0,0,5,campoDasHortas            ,1 ,1 ).
info(48,7,83 ,10,[2021,12,3 ,21,0 ],3.3,0,6,avenidaDaLiberdade         ,11,13).
info(49,3,32 ,6 ,[2021,11,26,8 ,2 ],4.5,2,2,largoSaoPedro         ,2 ,2 ).
info(50,6,97 ,10,[2021,9 ,14,5 ,0 ],5.0,0,6,avenidaDaLiberdade         ,10,10).


% estado: Estado da encomenda
% estado: #IdEstado, Estado -> {V,F}
estado(0,'Entregue').
estado(1,'Cancelado').
estado(2,'Não Entregue').

% veiculo: veiculo utilizado
% veiculo: #IdVeiculo, Veiculo, Estado -> {V,F}
% 0 disponível, 1 indisponível para uso
veiculo(1, bicicleta, 0).
veiculo(2, bicicleta, 0).
veiculo(3, bicicleta, 0).
veiculo(4, bicicleta, 1).
veiculo(5, carro, 0).
veiculo(6, carro, 1).
veiculo(7, moto, 0).
veiculo(8, moto, 1).
veiculo(9, moto, 0).

% estafeta: #IdEstafeta, #IdVeiculo -> {V,F}
estafetaUtiliza(1,3).
estafetaUtiliza(2,4).
estafetaUtiliza(7,6).
estafetaUtiliza(8,7).
estafetaUtiliza(9,8).

% eco: Veiculo, Ecologia -> {V,F}
eco(bicicleta,100).
eco(carro,50).
eco(moto,45).

%peso: Veiculo, PesoMaximo -> {V,F}
peso(bicicleta, 5).
peso(carro, 100).
peso(moto, 20).

% velocidade: Veiculo,VelocidadeMedia -> {V,F}
velocidade(bicicleta, 10).
velocidade(carro, 25).
velocidade(moto, 35).
