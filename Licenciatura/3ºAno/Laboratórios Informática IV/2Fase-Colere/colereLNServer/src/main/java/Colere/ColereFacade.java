package Colere;

import Colere.Exceptions.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.*;

/**
 * Classe responsável pela implementação dos métodos requeridos na especificação,
 * implementando, por isso, a interface IColereFacade.
 */
public class ColereFacade implements IColereFacade {

    /** Map de Locais */
    private Map<String,Local> locais;
    /** Map de Gestores */
    private Map<String,Gestor> gestores;
    /** Conexão ao Servidor MySQL */
    private Connection con;


    /**
     * Construtor da Classe ColereFacade.
     * Recebe os dados de início de sessão no servidor MySQL, lendo para memória
     * os dados presentes na base de dados até ao momento, inserindo-os nos Maps
     * correspondentes.
     *
     * @param SQLuser Username MySQL Server.
     * @param SQLpassword Password MySQL Server.
     * @throws SQLException LoginError ou Erro de Query
     */
    public ColereFacade(String SQLuser,String SQLpassword) throws SQLException{
        locais = new HashMap<>();
        gestores = new HashMap<>();

        /** EFETUA A CONEXÃO COM A BD */
        String url = "jdbc:mysql://localhost:3306/colere";
        con = DriverManager.getConnection(url, SQLuser, SQLpassword);
        System.out.println("Database connection is successful !!!!");

        /** PESQUISA TODOS OS LOCAIS NA BD */
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Local");
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            /** PARSING DE TODOS OS CAMPOS */
            String nomeLocal = rs.getString("NomeLocal");
            String endereco = rs.getString("Endereço");
            double latitude = rs.getDouble("Latitude");
            double longitude = rs.getDouble("Longitude");
            String descricao = rs.getString("Descrição");
            String horaAB = rs.getString("HorárioAbertura");
            String horaFec = rs.getString("HorárioEncerramento");
            String website = rs.getString("Website");
            String emailGestor = rs.getString("Gestor_Email");
            try {
                Local local = new Local(nomeLocal, endereco, latitude, longitude, descricao, horaAB, horaFec, website, emailGestor);

                /** PROCURA E CRIA GESTOR ASSOCIADO */
                ps = con.prepareStatement("SELECT * From Gestor Where Email = '"+emailGestor+"';");
                ResultSet ges = ps.executeQuery();
                ges.next();
                String pwd = ges.getString("Password");
                Gestor gestor = new Gestor(emailGestor,pwd,nomeLocal);
                this.gestores.put(emailGestor,gestor);

                /** PROCURA E ADICIONA AS CLASSIFICACOES ASSOCIADAS */
                ps = con.prepareStatement("SELECT IP,Valor FROM Classificação WHERE Local_NomeLocal = '"+nomeLocal+"';");
                ResultSet clas = ps.executeQuery();
                while (clas.next()) {
                    String ip = clas.getString("Ip");
                    int val = clas.getInt("Valor");
                    local.adicionaClassificacao(ip,val);
                }

                /** PROCURA E ADICIONA OS EVENTOS ASSOCIADOS */
                ps = con.prepareStatement("SELECT * FROM Evento WHERE Local_NomeLocal = '"+nomeLocal+"';");
                ResultSet even = ps.executeQuery();
                while (even.next()) {
                    String nomeEvento = even.getString("nomeEvento");
                    String desc = even.getString("Descrição");
                    Date data = even.getDate("DatHora");
                    Time time = even.getTime("DatHora");

                    local.putEvento(nomeEvento,data.toLocalDate().atTime(time.toLocalTime()), desc);
                }

                this.locais.put(nomeLocal,local);

            } catch (CoordenadasInvalidas e){
                System.out.println("Error loading local "+nomeLocal);
            }
        }
        System.out.println("Locais carregados");
    }


    /**
     * Método que retorna um Local dado a sua chave.
     * Lança uma exceção caso este não exista.
     *
     * @param nome Chave do Local.
     * @return Local procurado.
     * @throws LocalInexistente Local não existe
     */
    public Local getLocal(String nome) throws LocalInexistente {
        if(!this.locais.containsKey(nome))
            throw new LocalInexistente("Local "+nome+" inexistente.");

        return this.locais.get(nome);
    }


    /**
     * Método que procura o Gestor dada a sua chave.
     * Lança uma exceção caso este não exista.
     *
     * @param email Chave do Gestor.
     * @return Gestor procurado.
     * @throws GestorInexistente Gestor não existe
     */
    public Gestor getGestor(String email) throws GestorInexistente{
        if(!this.gestores.containsKey(email))
            throw new GestorInexistente("Gestor "+email+" inválido.");

        return this.gestores.get(email);
    }


    /**
     * Método que pesquisa Locais a uma distância X da localização fornecida.
     *
     * @param localizacao Localização a comparar.
     * @param distancia Distância Máxima.
     * @return Lista de Locais compreendidos dentro do raio de procura.
     */
    public List<Local> pesquisarLocais(Localizacao localizacao, double distancia) {
        List<Local> res = new ArrayList<>();

        for(Local local : this.locais.values()){
            if(distancia >= localizacao.distanciaAte(local.obterLocalizacao())){
                res.add(local);
            }
        }

        return res;
    }


    /**
     * Método que procura os N Locais mais perto do utilizador.
     *
     * @param nLocais Número de Locais Máximo a apresentar.
     * @param localUser Localização do utilizador.
     * @return Lista de N Locais mais perto do utilizdor.
     */
    public List<Local> pedirPercursoDistancia(int nLocais, Localizacao localUser) {
        List<Local> res = new ArrayList<>();
        Map<Double,Local> locaisDistancia = new TreeMap<>();

        for(Local local : this.locais.values()){
            locaisDistancia.put(localUser.distanciaAte(local.obterLocalizacao()),local);
        }

        int n = 0;
        for(Local local : locaisDistancia.values()){;
            if(n<nLocais) {
                res.add(local);
                n++;
            }

        }

        return res;
    }


    /**
     * Método que procura os N Locais com avaliação mais alta.
     *
     * @param nLocais Número de Locais Máximo a apresentar.
     * @return Lista de N Locais com avaliações mais altas.
     */
    public List<Local> pedirPercursoAvaliacao(int nLocais) {
        List<Local> res = new ArrayList<>();
        Map<Double,Local> locaisAvaliacao = new TreeMap<>();

        for(Local local : this.locais.values()){
            locaisAvaliacao.put(local.obterClassificacao(),local);
        }

        int n = 0;
        for(Local local : locaisAvaliacao.values()){;
            if(n>=locaisAvaliacao.size()-nLocais) {
                res.add(local);

            }
            n++;
        }

        return res;
    }


    /**
     * Método que coloca uma avaliação de um utilizador no Local correspondente.
     * Lança uma exceção caso o Local não exista.
     *
     * @param nome Nome do Local a procurar.
     * @param ip IP do utilizador.
     * @param novaClassific Avaliação do utilizador.
     * @throws LocalInexistente Local não existe
     * @throws SQLException Erro na modificação da BD
     */
    public void avaliarLocal(String nome, String ip, int novaClassific) throws LocalInexistente,SQLException {
        if(!this.locais.containsKey(nome))
            throw new LocalInexistente("Local "+nome+" inválido.");

        con.prepareStatement("INSERT INTO Classificação (IP,Valor,Local_NomeLocal) VALUES ('"+ip+"',"+novaClassific+",'"+nome+"');").executeUpdate();

        Local local = this.locais.get(nome);
        local.adicionaClassificacao(ip,novaClassific);
    }


    /**
     * Método que devolve um local dado a sua chave.
     * @param nome Nome do Local a consultar.
     * @return Local consultado.
     * @throws LocalInexistente Local não existe
     */
    public Local consultaLocal(String nome) throws LocalInexistente {
        if(!this.locais.containsKey(nome))
            throw new LocalInexistente("Local "+nome+" inválido.");

        return this.locais.get(nome);
    }


    /**
     * Método que autentica um Gestor dado o seu email e password.
     * Retorna true caso as informações coincidam com a a informação
     * armazenada e false caso contrário.
     *
     * @param email Email do Gestor.
     * @param pwd Password do Gestor.
     * @return Autenticação com Sucesso.
     */
    public boolean loginGestor(String email, String pwd) {
        if(!this.gestores.containsKey(email))
            return false;

        return this.gestores.get(email).comparaPwd(pwd);
    }


    /**
     * Método que retorna o Local associado a um certo Gestor.
     * Lança uma exceção caso o Gestor/Local não exista.
     *
     * @param email Email do gestor.
     * @return Local associado ao Gestor.
     * @throws GestorInexistente Gestor não existe
     * @throws LocalInexistente Local não existe
     */
    public Local getLocalGestor(String email) throws GestorInexistente, LocalInexistente{
        return this.getLocal(this.getGestor(email).getLocal());
    }


    /**
     * Método que adiciona um Evento a um certo Local(associado ao email de Gestor fornecido).
     * Insere a nova informação tanto em memória como na base de dados MySQL associada.
     *
     * @param email Email do Gestor.
     * @param nome Nome do Evento.
     * @param data Data do Evento.
     * @param descricao Descrição do Evento.
     * @throws GestorInexistente Gestor não existe
     * @throws LocalInexistente Local não existe
     * @throws EventoJaExiste Evento já existe
     * @throws SQLException Erro na modificação da BD
     */
    public void adicionarEvento(String email,String nome,LocalDateTime data,String descricao)
            throws GestorInexistente,
                    LocalInexistente,
                    EventoJaExiste,
                    SQLException
    {
        Local local = this.getLocalGestor(email);

        try {
            local.procuraEvento(nome);
            throw new EventoJaExiste("Evento "+nome+" já existe");
        }catch (EventoInexistente e){
            String stringData = data.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
            con.prepareStatement("INSERT INTO Evento (nomeEvento,DatHora,Descrição,Local_NomeLocal) VALUES ('"+nome+"','"+stringData+"','"+descricao+"','"+local.getNome()+"');").executeUpdate();

            local.putEvento(nome,data,descricao);
        }
    }


    /**
     * Método que edita um Evento de um Local(associado ao email de Gestor fornecido).
     * Altera a informação tanto em memória como na base de dados MySQL associada.
     *
     * @param email Email do Gestor.
     * @param nomeAntes Nome Antigo do Evento.
     * @param nomeDepois Novo Nome do Evento.
     * @param data Nova Data do Evento.
     * @param descricao Nova Descrição do Evento.
     * @throws GestorInexistente Gestor não existe
     * @throws LocalInexistente Local não existe
     * @throws EventoInexistente Evento não existe
     * @throws EventoJaExiste Evento já existe
     * @throws SQLException Erro na modificação da BD
     */
    public void editarEvento(String email,String nomeAntes, String nomeDepois, LocalDateTime data, String descricao)
            throws GestorInexistente,
                    LocalInexistente,
                    EventoInexistente,
                    EventoJaExiste,
                    SQLException
    {
        Local local = this.getLocalGestor(email);

        try {
            if(nomeAntes.equals(nomeDepois))
                throw new EventoInexistente();
            local.procuraEvento(nomeDepois);
            throw new EventoJaExiste("Evento "+nomeDepois+" já existe");
        }catch (EventoInexistente e){
            String stringData = data.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));

            con.prepareStatement("UPDATE Evento SET " +
                    "nomeEvento = '"+nomeDepois+"'," +
                    "DatHora = '"+stringData+"'," +
                    "Descrição = '"+descricao+"'" +
                    "WHERE nomeEvento = '"+nomeAntes+"' and Local_NomeLocal = '"+local.getNome()+"'").executeUpdate();

            //con.prepareStatement("DELETE FROM Evento WHERE nomeEvento = '"+nomeAntes+"' and Local_NomeLocal = '"+local.getNome()+"'").executeUpdate();
            //con.prepareStatement("INSERT INTO Evento (nomeEvento,DatHora,Descrição,Local_NomeLocal) VALUES ('"+nomeDepois+"','"+stringData+"','"+descricao+"','"+local.getNome()+"');").executeUpdate();

            local.removerEvento(nomeAntes);
            local.putEvento(nomeDepois,data,descricao);
        }

    }


    /**
     * Método que remove um Evento de um Local(associado ao email de Gestor fornecido).
     * Retira a informação do mesmo tanto em memória como da base de dados MySQL associada.
     *
     * @param email
     * @param nome
     * @throws GestorInexistente Gestor não existe
     * @throws LocalInexistente Local não existe
     * @throws EventoInexistente Evento não existe
     * @throws SQLException Erro na modificação da BD
     */
    public void removerEvento(String email,String nome)
            throws GestorInexistente,
                    LocalInexistente,
                    EventoInexistente,
                    SQLException
    {
        Local local = this.getLocalGestor(email);
        con.prepareStatement("DELETE FROM Evento WHERE nomeEvento = '"+nome+"' and Local_NomeLocal = '"+local.getNome()+"'").executeUpdate();

        local.removerEvento(nome);
    }


    /**
     * Método que edita um Local(associado ao email de Gestor fornecido).
     * Altera a informação não só em memória como também na base de dados MySQL associada.
     *
     * @param email Email do Gestor.
     * @param descricao Nova Descrição do Local.
     * @param horaAbertura Nova Hora de Abertura do Local.
     * @param horaFecho Nova Hora de Fecho do Local.
     * @param website Noob Website do Local.
     * @throws GestorInexistente Gestor não existe
     * @throws LocalInexistente Local não existe
     * @throws SQLException Erro na modificação da BD
     */
    public void editarLocal(String email,String descricao,String horaAbertura,String horaFecho,String website)
            throws GestorInexistente,
                    LocalInexistente,
                    SQLException
    {
        Local local = this.getLocalGestor(email);
        con.prepareStatement("UPDATE Local SET " +
                "Descrição = '"+descricao+"'," +
                "HorárioAbertura = '"+horaAbertura+"'," +
                "HorárioEncerramento = '"+horaFecho+"'," +
                "Website = '"+website+"' " +
                "WHERE NomeLocal = '"+local.getNome()+"';").executeUpdate();

        local.editar(descricao,horaAbertura,horaFecho,website);
    }


}
