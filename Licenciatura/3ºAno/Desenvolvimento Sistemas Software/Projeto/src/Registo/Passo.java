public class Passo {

    private String descricao = "";
    private Boolean completo = false;
    private String idTecnico = "";
    private float tempoPrevisto = 0;
    private float tempoReal = 0;
    private float custoPrevisto = 0;
    private float custoReal = 0;

    /**
     * Construtor da classe Passo.
     * @param descricao
     * @param tempoPrevisto
     * @param custoPrevisto
     */
    public Passo(String descricao, float tempoPrevisto, float custoPrevisto){
        this.descricao = descricao;
        this.tempoPrevisto = tempoPrevisto;
        this.custoPrevisto = custoPrevisto;
    }

    public String getDescricao() {return descricao;}
    public float getTempoPrevisto() {return tempoPrevisto;}
    public float getTempoReal() {return tempoReal;}
    public float getCustoPrevisto() {return custoPrevisto;}
    public float getCustoReal() {return custoReal;}

    /**
     * Metodo que completa um passo.
     * @param idTecnico
     * @param tempoReal
     * @param custoReal
     */
    public void completarPasso(String idTecnico, float tempoReal, float custoReal){
        this.idTecnico = idTecnico;
        this.tempoReal = tempoReal;
        this.custoReal = custoReal;
        this.completo = true;
    }

}
