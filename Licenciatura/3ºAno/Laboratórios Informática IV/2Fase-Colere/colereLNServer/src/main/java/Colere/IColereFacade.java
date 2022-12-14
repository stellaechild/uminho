package Colere;

import Colere.Exceptions.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Interface da Lógica de Negócio, contendo todos os métodos requeridos na especificação.
 */
public interface IColereFacade {

    /**
     * Método que pesquisa Locais a uma distância X da localização fornecida.
     *
     * @param localizacao Localização a comparar.
     * @param distancia Distância Máxima.
     * @return Lista de Locais compreendidos dentro do raio de procura.
     */
    public List<Local> pesquisarLocais(Localizacao localizacao,double distancia);

    /**
     * Método que procura os N Locais mais perto do utilizador.
     *
     * @param nLocais Número de Locais Máximo a apresentar.
     * @param localUser Localização do utilizador.
     * @return Lista de N Locais mais perto do utilizdor.
     */
    public List<Local> pedirPercursoDistancia(int nLocais, Localizacao localUser);

    /**
     * Método que procura os N Locais com avaliação mais alta.
     *
     * @param nLocais Número de Locais Máximo a apresentar.
     * @return Lista de N Locais com avaliações mais altas.
     */
    public List<Local> pedirPercursoAvaliacao(int nLocais);

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
    public void avaliarLocal(String nome,String ip,int novaClassific) throws LocalInexistente, SQLException;

    /**
     * Método que devolve um local dado a sua chave.
     * @param nome Nome do Local a consultar.
     * @return Local consultado.
     * @throws LocalInexistente Local não existe
     */
    public Local consultaLocal(String nome) throws LocalInexistente;

    /**
     * Método que autentica um Gestor dado o seu email e password.
     * Retorna true caso as informações coincidam com a a informação
     * armazenada e false caso contrário.
     *
     * @param email Email do Gestor.
     * @param pwd Password do Gestor.
     * @return Autenticação com Sucesso.
     */
    public boolean loginGestor(String email,String pwd);

    /**
     * Método que retorna o Local associado a um certo Gestor.
     * Lança uma exceção caso o Gestor/Local não exista.
     *
     * @param email Email do gestor.
     * @return Local associado ao Gestor.
     * @throws GestorInexistente Gestor não existe
     * @throws LocalInexistente Local não existe
     */
    public Local getLocalGestor(String email) throws GestorInexistente, LocalInexistente;

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
    public void adicionarEvento(String email, String nome, LocalDateTime data, String descricao)
            throws GestorInexistente,LocalInexistente,EventoJaExiste,SQLException;

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
    public void editarEvento(String email,String nomeAntes,String nomeDepois, LocalDateTime data, String descricao)
            throws GestorInexistente,LocalInexistente,EventoInexistente,EventoJaExiste,SQLException;

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
    public void removerEvento(String email, String nome)
            throws GestorInexistente,LocalInexistente,EventoInexistente,SQLException;

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
            throws GestorInexistente,LocalInexistente,SQLException;

}
