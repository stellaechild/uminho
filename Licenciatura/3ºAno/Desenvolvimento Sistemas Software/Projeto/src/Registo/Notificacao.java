import java.time.LocalDateTime;

public class Notificacao {

    private String idFuncBalcao;
    private LocalDateTime dataNotif;
    private String conteudo;

    /**
     * Construtor da classe Passo.
     * @param idFuncBalcao
     * @param conteudo
     */
    public Notificacao(String idFuncBalcao,String conteudo){
        this.idFuncBalcao = idFuncBalcao;
        this.conteudo = conteudo;
        this.dataNotif = LocalDateTime.now();
    }

}
