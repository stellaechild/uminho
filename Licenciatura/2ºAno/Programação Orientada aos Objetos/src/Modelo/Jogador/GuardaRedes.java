/**
 * SubClasse GuardaRedes
 *
 * @author Vicente Moreira
 * @date 21/04/2021
 */

public class GuardaRedes extends Jogador {
    private int elasticidade;
    private int reflexo;
    private int segurar; //"Segurar a bola" (GRIP)

    /**
     * Construtor por Omissão
     */
    public GuardaRedes(){
        super();
        this.elasticidade = 0;
        this.reflexo = 0;
        this.segurar = 0;
    }

    /**
     * Construtor parametrizado
     */
    public GuardaRedes(String nome, int numero,int velocidade,int resistencia,int destreza,
                       int impulsao,int jogDeCabeca,int remate,int capacidadeDePasse,
                       int elasticidade,int reflexo,int segurar){
        super(nome,numero,velocidade,resistencia,destreza,impulsao,jogDeCabeca,remate,capacidadeDePasse);
        this.setElasticidade(elasticidade);
        this.setReflexo(reflexo);
        this.setSegurar(segurar);
        this.calculaHabilidade();
    }

    /**
     * Construtor de cópia parametrizado
     */
    public GuardaRedes(Jogador outro,int elasticidade,int reflexo,int segurar){
        super(outro);
        this.setElasticidade(elasticidade);
        this.setReflexo(reflexo);
        this.setSegurar(segurar);
        this.calculaHabilidade();
    }

    /**
     * Construtor de cópia
     */
    public GuardaRedes(Jogador outro){
        super(outro);
        this.setElasticidade(0);
        this.setReflexo(0);
        this.setSegurar(0);
    }

    /**
     * Métodos SET
     */
    public void setElasticidade(int elasticidade) {
        this.elasticidade =  betweenZeroToHundred(elasticidade);
    }
    public void setReflexo(int reflexo) {
        this.reflexo = betweenZeroToHundred(reflexo);
    }
    public void setSegurar(int segurar) {
        this.segurar = betweenZeroToHundred(segurar);
    }

    /**
     * Métodos GET
     */
    public int getElasticidade() {
        return elasticidade;
    }
    public int getReflexo() {
        return reflexo;
    }
    public int getSegurar() {
        return segurar;
    }

    /**
     * Método que calcula a Habilidade específica do Guarda-Redes.
     */
    public int calculaHabilidade(){
        double res = 0;
        res+= 0.02 * this.getCaracteristica("velocidade");
        res+= 0.03 * this.getCaracteristica("resistencia");
        res+= 0.15 * this.getCaracteristica("destreza");
        res+= 0.1 * this.getCaracteristica("impulsao");
        res+= 0.02 * this.getCaracteristica("jogo de cabeca");
        res+= 0.03 * this.getCaracteristica("remate");
        res+= 0.1 * this.getCaracteristica("capacidade de passe");
        res+= 0.2 * elasticidade;
        res+= 0.2 * reflexo;
        res+= 0.15 * segurar;
        return (int) res;
    }

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("  Guarda-Redes: ").append(super.getNumero()).append(" - ").append(super.getNome()).append('\n');
        sb.append("Estado: ").append(super.estadoToString()).append(" / ")
                .append("Motivação: ").append(super.motivacaoToString()).append('\n');
        sb.append("Hablidade: ").append(super.getHabilidade()).append('\n');
        sb.append("VEL / RES / DES / IMP / JGC / REM / PAS - ELA / REF / SEG").append('\n');
        sb.append(super.caracteristicasToString());
        sb.append(this.elasticidade).append("    ")
                .append(this.reflexo).append("    ")
                .append(this.segurar).append('\n');
        return sb.toString();
    }

    /**
     * Método clone
     */
    public Jogador clone() {
        return new GuardaRedes(this);
    }
}
