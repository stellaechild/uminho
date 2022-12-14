import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanoDeTrabalho {

    private LocalDate dataOrcamento;
    private Boolean emPausa = false;
    private String idTecAutor = "";
    private LocalDateTime dataCompleto;
    private int passosRealizados = 0;
    private Map<Integer, Passo> passos;

    public PlanoDeTrabalho(){}


    public LocalDate getDataOrcamento() {return this.dataOrcamento;}
    public String getIdTecAutor(){return this.idTecAutor;}
    public LocalDateTime getDataCompleto(){
        return this.dataCompleto;
    }

    /**
     * Metodo que pausa o plano de acao.
     */
    public void pausarPlano(){
        this.emPausa = true;
    }

    /**
     * Metodo que avanca com o plano em acaoo.
     * @param idPasso o passo atual.
     * @param tecnico o tecnico que efetua o plano.
     * @param tempoReal o tempo a que esta mudança e feita.
     * @param custoReal custo da operacao.
     * @return
     */
    public Boolean avancarPlano(int idPasso,String tecnico,float tempoReal, float custoReal){
        if(passos.containsKey(idPasso)){
            if(this.emPausa) this.emPausa = false;
            this.passos.get(idPasso).completarPasso(tecnico,tempoReal,custoReal);
            return !testaLimiteCusto();
        }
        return false;
    }

    /**
     * Metodo que retorna true caso este plano esteja concluido, false caso isto nao seja verificado.
     * @return
     */
    public boolean completarPlano() {
        if(this.passos.size() == this.passosRealizados){
            this.dataCompleto = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Metodo que testa o limite de custo.
     * @return
     */
    private boolean testaLimiteCusto(){
        float custoPrev = custoPrevistoPlano();
        float custoReal = custoRealPlano();
        if ((custoReal / custoPrev) >= 1.2) return true;
        return false;
    }

    /**
     * Metodo para a insercao de um novo passo no plano de trabalhos.
     * @param passos novo passo a ser registado.
     */
    public void registarPassos(List<Passo> passos) {
        this.passos = new HashMap<>();
        for(int i = 1; i <= passos.size(); i++){
            this.passos.put(i,passos.get(i-1));
        }
    }

    /**
     * Metodo que calcula o tempo previsto para a conclusao do plano.
     * @return
     */
    public float tempoPrevistoPlano(){
        return sumAllPassosCampo("tp");
    }
    /**
     * Metodo que calcula o tempo real do plano.
     * @return
     */
    public float tempoRealPlano(){
        return sumAllPassosCampo("tr");
    }
    /**
     * Metodo que calcula o custo previsto para a conclusao do plano.
     * @return
     */
    public float custoPrevistoPlano(){
        return sumAllPassosCampo("cp");
    }
    /**
     * Metodo que calcula o custo real do plano.
     * @return
     */
    public float custoRealPlano(){
        return sumAllPassosCampo("cr");
    }

    /**
     * Metodo que calcula os tempos e os custos.
     * @param cmp comando para saber se se quer o tempo previsto, tempo real, custo real ou custo previsto de um dado plano.
     * @return
     */
    private float sumAllPassosCampo(String cmp){
        float res = 0;
        for(Passo p: this.passos.values()){
            if(cmp.equals("tp")) res += p.getTempoPrevisto();
            else if (cmp.equals("tr")) res += p.getTempoReal();
            else if (cmp.equals("cp")) res += p.getCustoPrevisto();
            else if (cmp.equals("cr")) res += p.getCustoReal();
        }
        return res;
    }


}
