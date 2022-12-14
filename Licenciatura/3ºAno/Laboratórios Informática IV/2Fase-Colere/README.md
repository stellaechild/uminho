# **Colere** 
Projeto desenvolvido no âmbito da unidade curricular de Laboratórios de Informática IV.

<u>**Tema**</u>: Guia de Locais de Cultura.

#
## Autores:
- [A81667 - Daniel Faria](https://github.com/omiPacheco) 
- [A93290 - Joana Alves](https://github.com/marshaia) 
- [A93264 - Maria Cunha](https://github.com/stellaechild) 
- [A93296 - Vicente Moreira](https://github.com/VicShadow) 

#
## Caracterização do Projeto:
Este projeto apresenta três componentes principais:
- [Servidor Web (React App)](#ra)
- [Servidor Lógica de Negócio (Projeto Java)](#ln)
- [Servidor de Base de Dados (MySQL Workbench)](#bd)

Apresentamos de seguida a ordem de configuração destes três componentes, tendo esta de ser seguida para conseguir obter uma execução bem-sucedida.



#
### <a id="bd">  </a> **Base de Dados**
Para o bom funcionamento da aplicação, é necessário ter um servidor com a base de dados disponível. Para isso, utilize a ferramenta *MySQL Workbench* para criar um servidor local.

De seguida, carregue o modelo **'colere'** presente na diretoria '*BD*', sincronizando o servidor com os *schemas* presentes. 

Para importar os dados iniciais da base de dados, utilize a funcionalidade '*table data import wizard*', importando os dados de cada tabela através dos seus ficheiros correspondentes. <u>Note que a ordem de import tem de ser respeitada devido às *foreign keys constraints* presentes no modelo!</u>

1. Tabela Gestor -> "Gestores.csv"
2. Tabela Local -> "Locais.csv"
3. Tabela Evento -> "Eventos.csv"
4. Tabela Classifcação -> "Classificacoes.csv"

No final destas configurações, deverá obter um servidor MySQL atualizado com toda a informação, funcional e operacional, podendo avançar para o próximo passo.




#
### <a id="ln"> </a> **Lógica de Negócio** 
Para uma conexão bem sucedida da base de dados à lógica de negócio, é necessário a existência de uma driver de conexão. Assim, verifique que tem instalado o driver **ConnectorJ** do MySQL. Após a instalação, dirija-se ao ambiente de desenvolvimento IntelliJ e execute os seguintes passos:

1. File
2. Project Structure
3. Libraries
4. Selecione o botão '**+**'
5. Pesquise "mysql"
6. Escolha a versão mais recento do "mysql-connector-java"
7. Adicione ao projeto 
   
De seguida, é necessário alterar os campos de autenticação ao servidor MySQL no ficheiro *'ColereLnServerApplication.java'* presente na diretoria *colereLNServer/src/main/java/grupo19/ColereLnServer*.

Na linha **22**, nos argumentos do construtor da classe *ColereFacade*, digite o nome de utilizador (entre aspas) seguido da password do mesmo (também dentro de aspas). O resultado final deverá ser algo do género:

```java
private IColereFacade colereFacade = new ColereFacade("nomeUtilizador","Password");
```

**NOTA:** Após esta configuração, é necessário recompilar o código.

Para inicializar, a camada da lógica de negócio da aplicação, deverá iniciar o ficheiro executável da mesma. Para isso, navegue até à pasta *'colereLNServer/bin'* pelo terminal, escrevendo de seguida: 

    $ java -jar colereLNServer.jar 
A sua execução irá inicializar toda a camada da lógica de negócio, carregando toda a informação presente no momento na base de dados, assim como aceder aos pedidos da *API REST*.





#
### <a id="ra">  </a> **Interface Gráfica (React App)**
Por fim, para executar e interagir com a interface gráfica desenvolvida (através de uma aplicação *React*), terá de ter instalado [*node.js*](https://nodejs.org/en/) com uma versão igual ou superior a V16.

Após a sua instalação, abra o terminal e navegue até à diretoria 'WebApp/web-li4'. De seguida, digite os seguintes comandos sequencialmente e pela ordem apresentada:

```cmd
    $ npm install
      ...
    $ npm start
```
Estes comandos irão compilar a aplicação *React* assim como lançar o browser com a página inicial do projeto. A partir daí, estará a interagir com a aplicação **Colere** totalmente funcional.

**NOTA:** Caso o browser não seja iniciado, execute o mesmo colocando na barra de pesquisa:

```https
https://www.localhost:3000
``
