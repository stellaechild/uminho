INSERT INTO Classificação (IP,Valor,Local_NomeLocal) VALUES ('192.168.1.11',2,'');

SELECT * FROM Local;
SELECT * FROM Gestor;
SELECT * FROM Classificação;
SELECT * FROM Evento;

SELECT * From Gestor Where Email = 'crivos@gmail.com';

SELECT * FROM Local Where Gestor_Email = 'crivos@gmail.com';

SELECT IP,Valor FROM Classificação WHERE Local_NomeLocal = 'Casa dos Crivos';

UPDATE Local SET Endereço = 'Braga Norte 920' Where NomeLocal = 'Palácio do Raio';