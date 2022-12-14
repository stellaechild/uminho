import java.util.HashMap;

/**
 * SubClasse Avançados
 *
 * @author Vicente Moreira
 * @date 21/04/2021
 */

public class Avancado extends Jogador {
    private int controlobola;
    private int finta;
    private int oportunismo;

    /**
     * Construtor por omissão
     */
    public Avancado(){
        super();
        controlobola = 0;
        finta = 0;
        oportunismo = 0;
    }


    /**
     * Construtor parametrizado
     */
    public Avancado(String nome, int numero,int velocidade,int resistencia,int destreza,
                    int impulsao,int jogDeCabeca,int remate,int capacidadeDePasse,
                    int controlobola, int finta, int oportunismo){
        super(nome,numero,velocidade,resistencia,destreza,impulsao,jogDeCabeca,remate,capacidadeDePasse);
        this.setControlobola(controlobola);
        this.setFinta(finta);
        this.setOportunismo(oportunismo);
        this.calculaHabilidade();
    }

    /**
     * Construtor por cópia parametrizado.
     */
    public Avancado(Jogador outro,int controlobola,int finta,int oportunismo){
        super(outro);
        this.setControlobola(controlobola);
        this.setFinta(finta);
        this.setOportunismo(oportunismo);
    }

    /**
     * Construtor por cópia
     */
    public Avancado(Avancado outro){
        super(outro);
        this.setControlobola(outro.controlobola);
        this.setFinta(outro.finta);
        this.setOportunismo(outro.oportunismo);
        this.calculaHabilidade();
    }

    /**
     * Métodos SET
     */
    public void setControlobola(int controlobola) {
        this.controlobola = betweenZeroToHundred(controlobola);
    }
    public void setFinta(int finta) {
        this.finta = betweenZeroToHundred(finta);
    }
    public void setOportunismo(int oportunismo) {
        this.oportunismo = betweenZeroToHundred(oportunismo);
    }

    /**
     * Métodos GET
     */
    public int getControlobola() {
        return controlobola;
    }
    public int getFinta() {
        return finta;
    }
    public int getOportunismo() {
        return oportunismo;
    }

    /**
     * Método que calcula a Habilidade específica do Avancado.
     */
    public int calculaHabilidade(){
        double res = 0;
        res+= 0.05 * this.getCaracteristica("velocidade");
        res+= 0.05 * this.getCaracteristica("resistencia");
        res+= 0.1 * this.getCaracteristica("destreza");
        res+= 0.05 * this.getCaracteristica("impulsao");
        res+= 0.1 * this.getCaracteristica("jogo de cabeca");
        res+= 0.2 * this.getCaracteristica("remate");
        res+= 0.05 * this.getCaracteristica("capacidade de passe");
        res+= 0.15 * controlobola;
        res+= 0.15 * finta;
        res+= 0.1 * oportunismo;
        return (int) res;
    }

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("      Avançado: ").append(super.getNumero()).append(" - ").append(super.getNome()).append('\n');
        sb.append("Estado: ").append(super.estadoToString()).append(" / ")
          .append("Motivação: ").append(super.motivacaoToString()).append('\n');
        sb.append("Hablidade: ").append(super.getHabilidade()).append('\n');
        sb.append("VEL / RES / DES / IMP / JGC / REM / PAS - CTR / FIN / OPR").append('\n');
        sb.append(super.caracteristicasToString());
        sb.append(this.controlobola).append("    ")
                .append(this.finta).append("    ")
                .append(this.oportunismo).append('\n');
        return sb.toString();
    }

    /**
     * Método clone
     */
    public Jogador clone() {
        return new Avancado(this);
    }

}
