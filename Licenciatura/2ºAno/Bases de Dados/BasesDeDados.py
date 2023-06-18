import mysql.connector

db = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "pmrfcmrf",
    database = "trabalhobd"
)

mycursor = db.cursor()
mycursor.execute("SELECT nome, stock, idFornecedor FROM produto WHERE stock < 5")

for x in mycursor:
    print(x)

mycursor1 = db.cursor()
mycursor1.execute("SELECT nome, dinheiroGasto,telemovel FROM cliente ORDER BY dinheiroGasto DESC LIMIT 3 ")    

for y in mycursor1:
    print(y)

mycursor2 = db.cursor()
mycursor2.execute("SELECT nome, prazoValidade FROM produto ORDER BY prazoValidade ASC")
for z in mycursor2:
    print(z)


mycursor3 = db.cursor()
mycursor3.execute("SELECT YEAR(dia) AS Year, MONTH(dia) AS Month, SUM(preco) AS preco FROM compra GROUP BY Year, Month ORDER BY preco DESC")
for a in mycursor3:
    print(a)
    

with open("exPythonBD.txt", "r") as arquivo:
    
    for linha in arquivo:
        mycursor = db.cursor()
        sepLinha = linha.split('#')
        tipoTabela = sepLinha[0]
        valoresTabela = sepLinha[1].split(',')
        
        if tipoTabela == "cliente":
            mycursor.execute("INSERT INTO cliente(idCliente,dinheiroGasto,numeroContribuinte,nome,telemovel,eMail) VALUES ("+valoresTabela[0]+","+valoresTabela[1]+",'"+valoresTabela[2]+"','"+valoresTabela[3]+"','"+valoresTabela[4]+"','"+valoresTabela[5]+"')")
        
        elif(tipoTabela == "compra"):
            mycursor.execute("INSERT INTO compra(idCompra,preco,hora,dia,desconto,idCliente) VALUES ("+valoresTabela[0]+","+valoresTabela[1]+",'"+valoresTabela[2]+"','"+valoresTabela[3]+"',"+valoresTabela[4]+","+valoresTabela[5]+")")
        
        elif(tipoTabela == "contato"):
            mycursor.execute("INSERT INTO contatos(fornecedor,contato) VALUES ("+valoresTabela[0]+",'"+valoresTabela[1]+"')")
        
        elif(tipoTabela == "entrega"):
            mycursor.execute("INSERT INTO entrega(idEntrega,data,preco,confirmaEntrega,cliente) VALUES ("+valoresTabela[0]+",'"+valoresTabela[1]+"',"+valoresTabela[2]+",'"+valoresTabela[3] +"',"+valoresTabela[4]+")")
        
        elif(tipoTabela == "fornecedor"):
            mycursor.execute("INSERT INTO fornecedor(idFornecedor,nomeEmpresa) VALUES ("+valoresTabela[0]+",'"+valoresTabela[1]+"')")

        elif(tipoTabela == "morada"):
            mycursor.execute("INSERT INTO morada(clienteID,morada) VALUES ("+valoresTabela[0]+",'"+valoresTabela[1]+"')")

        elif(tipoTabela == "produto"):
            mycursor.execute("INSERT INTO produto(idProduto,valor,stock,prazoValidade,idCompra,idEntrega,idFornecedor,nome) VALUES ("+valoresTabela[0]+","+valoresTabela[1]+","+valoresTabela[2]+",'"+valoresTabela[3]+"',"+valoresTabela[4]+","+valoresTabela[5]+","+valoresTabela[6]+",'"+valoresTabela[7]+"')")

        elif(tipoTabela == "tipo"):
            mycursor.execute("INSERT INTO tipo(idTipo,tipo,produto) VALUES ("+valoresTabela[0]+",'"+valoresTabela[1]+"',"+valoresTabela[2]+")")

        else:
            print("Nome da tabela inválido :(" + tipoTabela) 

        db.commit()                            