package Colere;
import java.time.LocalDateTime;


/**
 * Classe responsável pela representação de Evento de um certo Local.
 * Contém o nome do Evento, a sua DataHora de realização, assim como a descrição do mesmo.
 */
public class Evento {

    /** Nome do Evento */
    private String nome;
    /** DataHora do Evento */
    private LocalDateTime DataHora;
    /** Descrição do Evento */
    private String descricao;


    /**
     * Construtor da Classe Evento.
     * @param nome Nome do Evento.
     * @param data DataHora do Evento.
     * @param descricao Descrição do Evento.
     */
    public Evento(String nome,LocalDateTime data,String descricao){
        this.nome = nome;
        this.DataHora = data;
        this.descricao = descricao;
    }

    /**
     * Método GET da DataHora do Evento.
     * @return DataHora do Evento
     */
    public LocalDateTime getDataHora(){
        return this.DataHora;
    }

    /**
     * Método GET do Nome do Evento.
     * @return Nome do Evento
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método GET da Descrição do Evento.
     * @return Descrição do Evento
     */
    public String getDescricao() {
        return descricao;
    }
}
