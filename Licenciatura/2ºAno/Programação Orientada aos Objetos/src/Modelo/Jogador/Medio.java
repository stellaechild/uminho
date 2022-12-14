/**
 * SubClasse Médio
 *
 * @author Vicente Moreira
 * @date 21/04/2021
 */

public class Medio extends Jogador {
    private int visao;
    private int pressao;
    private int desarme; //recuperacao de bola

    /**
     * Construtor por omissão
     */
    public Medio(){
        super();
        this.visao = 0;
        this.pressao = 0;
        this.desarme = 0;
    }

    /**
     * Construtor parametrizado
     */
    public Medio(String nome, int numero, int velocidade,int resistencia,int destreza,
                 int impulsao,int jogDeCabeca,int remate,int capacidadeDePasse,
                 int visao,int pressao,int desarme){
        super(nome,numero,velocidade,resistencia,destreza,impulsao,jogDeCabeca,remate,capacidadeDePasse);
        this.setVisao(visao);
        this.setPressao(pressao);
        this.setDesarme(desarme);
        this.calculaHabilidade();
    }

    /**
     * Construto parameterizado.
     */
    public Medio(Jogador outro, int visao,int pressao,int desarme){
        super(outro);
        this.setVisao(visao);
        this.setPressao(pressao);
        this.setDesarme(desarme);
        this.calculaHabilidade();
    }

    /**
     * Construto por cópia
     */
    public Medio(Medio outro){
        super(outro);
        this.setVisao(outro.visao);
        this.setPressao(outro.pressao);
        this.setDesarme(outro.desarme);
    }

    /**
     * Métodos SET
     */
    public void setVisao(int visao) {
        this.visao = betweenZeroToHundred(visao);
    }
    public void setPressao(int pressao) {
        this.pressao = betweenZeroToHundred(pressao);
    }
    public void setDesarme(int desarme) {
        this.desarme = betweenZeroToHundred(desarme);
    }

    /**
     * Métodos GET
     */
    public int getVisao() {
        return visao;
    }
    public int getDesarme() {
        return desarme;
    }
    public int getPressao() {
        return pressao;
    }

    /**
     * Método que calcula a Habilidade específica do jogador (médio).
     */
    public int calculaHabilidade(){
        double res = 0;
        res+= 0.1 * this.getCaracteristica("velocidade");
        res+= 0.1 * this.getCaracteristica("resistencia");
        res+= 0.05 * this.getCaracteristica("destreza");
        res+= 0.1 * this.getCaracteristica("impulsao");
        res+= 0.05 * this.getCaracteristica("jogo de cabeca");
        res+= 0.05 * this.getCaracteristica("remate");
        res+= 0.05 * this.getCaracteristica("capacidade de passe");
        res+= 0.2 * visao;
        res+= 0.15 * desarme;
        res+= 0.15 * pressao;
        return (int) res;
    }

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("         Médio: ").append(super.getNumero()).append(" - ").append(super.getNome()).append('\n');
        sb.append("Estado: ").append(super.estadoToString()).append(" / ")
                .append("Motivação: ").append(super.motivacaoToString()).append('\n');
        sb.append("Hablidade: ").append(super.getHabilidade()).append('\n');
        sb.append("VEL / RES / DES / IMP / JGC / REM / PAS - VIS / DES / PRE").append('\n');
        sb.append(super.caracteristicasToString());
        sb.append(this.visao).append("    ")
                .append(this.desarme).append("    ")
                .append(this.pressao).append('\n');
        return sb.toString();
    }

    /**
     * Método clone
     */
    public Jogador clone() {
        return new Medio(this);
    }
}
