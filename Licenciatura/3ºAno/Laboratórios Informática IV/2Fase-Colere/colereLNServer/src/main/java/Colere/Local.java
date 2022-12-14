package Colere;
import Colere.Exceptions.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável pela representação de Locais.
 * Contém toda a informação pertinente ao mesmo, incluindo as suas classficações
 * e a chave do Gestor associado.
 */
public class Local {

    /** Nome do Local */
    private String nome;
    /** Localização do Local */
    private Localizacao localizacao;
    /** Descrição do Local */
    private String descricao;
    /** Hora de Abertura do Local */
    private String horaAbertura;
    /** Hora de Fecho do Local */
    private String horaFecho;
    /** Website do Local */
    private String website;
    /** Map de Eventos do Local */
    private Map<String,Evento> eventos;
    /** Classificações do Local */
    private Classificacao classificacoes;
    /** Chave do Gestor do Local */
    private String gestor;


    /**
     * Construtor da Classe Local.
     * @param nome Nome do Local
     * @param endereco Rua do Local.
     * @param latitude Latitude da Localização do Local.
     * @param longitude Longitude da Localização do Local.
     * @param descricao Descrição do Local
     * @param horaAbertura Hora de Abertura do Local
     * @param horaFecho Hora de Fecho do Local
     * @param website Website do Local
     * @param gestor Chave do Gestor do Local
     * @throws CoordenadasInvalidas
     */
    public Local(String nome,String endereco,double latitude,double longitude,String descricao,String horaAbertura,String horaFecho,String website,String gestor) throws CoordenadasInvalidas{
        this.nome = nome;
        this.localizacao = new Localizacao(endereco,latitude,longitude);
        this.descricao = descricao;
        this.horaAbertura = horaAbertura;
        this.horaFecho = horaFecho;
        this.website = website;
        this.eventos = new HashMap<>();
        this.classificacoes = new Classificacao();
        this.gestor = gestor;
    }

    // -------------------------------- MÉTODOS GET ---------------------------------
    public String getNome() {
        return nome;
    }
    public Localizacao getLocalizacao() {
        return localizacao;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getHoraAbertura() {
        return horaAbertura;
    }
    public String getHoraFecho() {
        return horaFecho;
    }
    public String getWebsite() {
        return website;
    }
    public Map<String, Evento> getEventos() {
        return eventos;
    }
    public double getClassificacoes() {
        return classificacoes.obterClassificacaoMedia();
    }
    public String getGestor() {
        return gestor;
    }

    /**
     * Método que retorna a Localização do Local.
     * @return Localização do Local.
     */
    public Localizacao obterLocalizacao(){
        return this.localizacao;
    }

    /**
     * Método que retorna a classificação média do Local,
     * com base nas avaliações dos utilizadores.
     * @return média das classificações.
     */
    public double obterClassificacao(){
        return this.classificacoes.obterClassificacaoMedia();
    }

    /**
     * Método que adiciona uma classificação de um utilizador associado ao seu IP.
     * @param ip IP do utilizador.
     * @param novaClassifcacao Avaliação do utilizador.
     */
    public void adicionaClassificacao(String ip,int novaClassifcacao) {
        this.classificacoes.adicionarClassificacao(ip,novaClassifcacao);
    }

    /**
     * Método que adiciona um Evento ao Local, criando-o a partir dos parâmetros
     * passados e colocamdo-o no map de Eventos do Local.
     * @param nome Nome do Evento.
     * @param data DataHora do Evento.
     * @param descricao Descrição do Evento.
     */
    public void putEvento(String nome,LocalDateTime data,String descricao){
        Evento evento = new Evento(nome,data,descricao);
        this.eventos.put(nome,evento);
    }

    /**
     * Método responsável pela procura de um Evento (através da sua chave),
     * no map de Eventos do Local. Lança uma Exceção caso não exista.
     * @param nome Chave do Evento a procurar.
     * @return Evento procurado.
     * @throws EventoInexistente
     */
    public Evento procuraEvento(String nome) throws EventoInexistente{
        if(!this.eventos.containsKey(nome))
            throw new EventoInexistente("Evento "+nome+" não existe");

        return this.eventos.get(nome);
    }

    /**
     * Método que remove um Evento do Map de Eventos do Local.
     * Caso o Evento não exista, lança uma exceção.
     * @param nome Chave do Evento a remover.
     * @throws EventoInexistente
     */
    public void removerEvento(String nome) throws EventoInexistente{
        if(!this.eventos.containsKey(nome))
            throw new EventoInexistente("Evento "+nome+" não existe");

        this.eventos.remove(nome);
    }

    /**
     * Método que remove Eventos terminados, comparando a sua data com a
     * data fornecida como argumento.
     * @param dataAtual Data de comparação.
     */
    public void removerEventosTerminados(LocalDateTime dataAtual){
        for(Evento ev : this.eventos.values()){
            if (ev.getDataHora().isBefore(dataAtual)){
                this.eventos.remove(ev.getNome());
            }
        }
    }

    /**
     * Método que edita o Local através dos parâmetros fornecidos como argumentos.
     * @param descricao Nova Descrição do Local.
     * @param horaAbertura Nova Hora de Abertura do Local.
     * @param horaFecho Nova Hora de Fecho do Local.
     * @param website Novo website do Local.
     */
    public void editar(String descricao,String horaAbertura,String horaFecho,String website){
        this.descricao = descricao;
        this.horaAbertura = horaAbertura;
        this.horaFecho = horaFecho;
        this.website = website;
    }

}
