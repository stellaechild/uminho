import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


/**
 * Classe Jogo
 *
 * @author Vicente Moreira
 * @version 20210327
 */

public class Jogo implements Serializable {

    public static final int AGENDADO = 0;
    public static final int PRONTO  = 1;
    public static final int DECORRER1PRT = 2;
    public static final int INTERVALO = 3;
    public static final int DECORRER2PRT = 4;
    public static final int FIM = 5;

    private LocalDateTime data;
    private String eq_visitado;
    private String eq_visitante;
    private Plantel visitado;
    private Plantel visitante;
    private int estadodojogo;
    private int golosvisitados;
    private int golosvisitantes;
    private LocalTime tempo;

    /**
     * Construtor por omissão
     */
    public Jogo(){
        this.data = LocalDateTime.now();
        this.eq_visitado = "TBD";
        this.eq_visitante = "TBD";
        this.visitado = new Plantel();
        this.visitante = new Plantel();
        this.estadodojogo = 0;
        this.golosvisitados = 0;
        this.golosvisitantes = 0;
        this.tempo = LocalTime.of(0,0,0);
    }

    /**
     * Construtor parametrizado
     */
    public Jogo(String visitado,String visitante,LocalDateTime date){
        this.data = date;
        this.eq_visitado = visitado;
        this.eq_visitante = visitante;
        this.visitado = new Plantel();
        this.visitante = new Plantel();
        this.estadodojogo = 0;
        this.golosvisitados = 0;
        this.golosvisitantes = 0;
        this.tempo = LocalTime.of(0,0,0);
    }

    /**
     * Construtor por cópia
     */
    public Jogo(Jogo outro){
        this.eq_visitado = outro.eq_visitado;
        this.eq_visitante = outro.eq_visitante;
        this.visitado = outro.visitado.clone();
        this.visitante = outro.visitante.clone();
        this.estadodojogo = outro.estadodojogo;
        this.golosvisitados = outro.golosvisitados;
        this.golosvisitantes = outro.golosvisitantes;
        this.tempo = outro.tempo;
    }



    /**
     * Métodos SET
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    public void setVisitado(String visitado) {
        this.eq_visitado = visitado;
    }
    public void setVisitante(String visitante) {
        this.eq_visitante = visitante;
    }
    public void setEstadodojogo(int estadodojogo) {
        this.estadodojogo = estadodojogo;
    }
    public void setGolosvisitados(int golosvisitados) {
        this.golosvisitados = golosvisitados;
    }
    public void setGolosvisitantes(int golosvisitantes) {
        this.golosvisitantes = golosvisitantes;
    }
    public void setTempo(LocalTime tempo) {
        this.tempo = tempo;
    }

    /**
     * SetJogoPronto
     */
    public void setJogoPronto(){
        this.estadodojogo= PRONTO;
    }

    /**
     * Métodos GET
     */
    public LocalDateTime getData() {
        return data;
    }
    public String getEq_Visitado() {
        return eq_visitado;
    }
    public String getEq_Visitante() {
        return eq_visitante;
    }
    public int getEstadodojogo() {
        return estadodojogo;
    }
    public int getGolosvisitados() {
        return golosvisitados;
    }
    public int getGolosvisitantes() {
        return golosvisitantes;
    }
    public LocalTime getTempo() {
        return tempo;
    }

    /**
     * Método toString
     * Não mostra as equipas em detalhes
     */
    public String toString() {
        //Jogo: Laceiras VS Laceiras   (Agendado)
        //      Data: 12/02/2021
        //      Resultado: 0 - 0
        final StringBuilder sb = new StringBuilder("Jogo: ");
        sb.append(eq_visitado).append(" VS ").append(eq_visitante);
        sb.append("   (").append(EstadoToString()).append(")").append('\n');
        sb.append("      Data: ").append(data.toString()).append('\n');
        sb.append("      Resultado: ").append(getResultado()).append('\n');
        return sb.toString();
    }

    /**
     * Método que traduz o estado para uma String
     */
    public String EstadoToString(){
        switch (this.estadodojogo){
            case 0: return "Agendado";
            case 1: return "Pronto";
            case 2: return "Parte 1 a decorrer";
            case 3: return "Intervalo";
            case 4: return "Parte 2 a decorrer";
            case 5: return "Terminado";
            default: return "Ups, não devias ter lido isto";
        }
    }

    /**
     * Método equals.
     * Compara todas as variáveis de instância.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o != null) || (this.getClass() != o.getClass())) return false;
        Jogo jogo = (Jogo) o;
        return getEq_Visitado().equals(jogo.getEq_Visitado()) &&
                getEq_Visitante().equals(jogo.getEq_Visitante());
    }

    /**
     * Método Clone
     */
    public Jogo clone(){
        return new Jogo(this);
    }

    /**
     * Métodos para devolver o resultado
     */
    public String getResultado(){
        return ""+this.golosvisitados+" - "+this.golosvisitantes;
    }

    /**
     * Método para avançar o estado do jogo
     */
    public void avancaEstado(){
        if (this.estadodojogo != 4)
        this.estadodojogo++;
    }

    /**
     * Método para marcar golo à equipa visitada
     */
    public void goloVisitado(){
        this.golosvisitados++;
    }

    /**
     * Método para marcar golo à equipa visitante
     */
    public void goloVisitante(){
        this.golosvisitantes++;
    }


    /**
     * Set 2 Plantel
     */
    public void setAllPlantel(Map<Integer,Jogador> casajogs,Map<Integer,Jogador> casasubs,
                              Map<Integer,Jogador> visjogs,Map<Integer,Jogador> vissubs){
        this.visitado.setAllJogs(casajogs,casasubs);
        this.visitante.setAllJogs(visjogs,vissubs);
    }

    /**
     * Set Plantel
     */
    public void setPlantel(Map<Integer,Jogador> jogs,Map<Integer,Jogador> subs,Boolean visitado){
        if(visitado) this.visitado.setAllJogs(jogs,subs);
        else this.visitante.setAllJogs(jogs,subs);
    }

}
