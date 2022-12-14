import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Registo {

    private String estado;
    private String nifCliente;
    private LocalDate dataEntrada;
    private String idRegisto;
    private String idFuncBalcao;
    private List<String> listaTecnicos = new ArrayList<>();
    private float tempoReal = 0;
    private LocalDate dataPrevista;
    private LocalDate dataEntrega;
    private List<Notificacao> notificacoes = new ArrayList<>();

    public enum Estados {
        PorOrcamentar,Orcamentado,PorReparar,Reparado,Abandonado, Terminado;
    }

    /**
     * Construtor de Registo
     * @param idReg
     * @param idFuncBalcao
     * @param nif
     */
    Registo(String idReg,String idFuncBalcao,String nif){
        this.idRegisto = idReg;
        this.nifCliente = nif;
        this.idFuncBalcao = idFuncBalcao;
        this.dataEntrada = LocalDate.now();
        this.estado = Estados.PorOrcamentar.toString();
    }

    public LocalDate getDataEntrada(){return this.dataEntrada;}
    public LocalDate getDataPrevista(){return this.dataPrevista;}
    public LocalDate getDataEntrega() {
        return this.dataEntrega;
    }
    public String getNifCliente() {
        return nifCliente;
    }
    public String getIdFuncBalcao() {
        return idFuncBalcao;
    }

    public boolean testaEArquivaRegisto(float dias){
        return false;
    }

    /**
     * Construtor que devolve um estado de um registo.
     * @param estado Este pode estar : PorOrcamentar, Orcamentado, PorReparar, Reparado, Terminado ou Abandonado.
     * @return
     */
    public boolean isEstado(String estado) {
        return this.estado.equals(estado);
    }

    /**
     * Metodo que regista notificacoes.
     * @param idFuncBalcao
     * @param conteudo
     */
    public void registarNotificacao(String idFuncBalcao,String conteudo){
        Notificacao not = new Notificacao(idFuncBalcao,conteudo);
        this.notificacoes.add(not);
    }

    /**
     * Metodo que altera o estado do registo.
     * @param estado
     */
    public void alteraEstado(String estado){
        this.estado = estado;
    }
    public float calculaCusto(){return 0;}
    public float tempoEsperado(){
        return 0;
    }
}
