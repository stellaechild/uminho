## Create Network

* docker network create gestaocontasnet

## Deploy mysql

* docker run --name GestaoDeContasDB --net gestaocontasnet -p 3306:3306 -d -e MYSQL_USER=gestao -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=GestaoDeContasDB -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql:latest

* Para testes: docker exec -ti GestaoDeContasDB /bin/sh mysql -ugestao -ppassword -e 'show databases;'

* docker start GestaoDeContasDB | docker stop GestaoDeContasDB


## Deploy Gestão de Contas

* docker build -t gestaocontas .

* docker run --name GestaoDeContas --net gestaocontasnet -p 8000:8000 gestaocontas:latest