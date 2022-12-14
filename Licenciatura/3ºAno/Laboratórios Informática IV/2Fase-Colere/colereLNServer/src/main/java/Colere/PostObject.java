package Colere;

/**
 * Classe auxiliar para leitura de pedidos POST.
 */
public class PostObject {

    private String comando = "";
    private String nomeLocal = "";
    private String ip = "";
    private String classificacao = "";
    private String email = "";
    private String password = "";
    private String nomeEvento = "";
    private String data = "";
    private String descricao = "";
    private String nomeEventoAntes = "";
    private String nomeEventoDepois = "";
    private String horaAbertura = "";
    private String horaFecho = "";
    private String website = "";

    public String getComando() {
        return comando;
    }
    public String getNomeLocal() {
        return nomeLocal;
    }
    public String getIp() {
        return ip;
    }
    public String getClassificacao() {
        return classificacao;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getNomeEvento() {
        return nomeEvento;
    }
    public String getData() {
        return data;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getNomeEventoAntes() {
        return nomeEventoAntes;
    }
    public String getNomeEventoDepois() {
        return nomeEventoDepois;
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
}
