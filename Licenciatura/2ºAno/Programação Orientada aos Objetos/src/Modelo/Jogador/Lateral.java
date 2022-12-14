/**
 * SubClasse Lateral
 *
 * @author Maria Cunha
 * @date 21/04/2021
 */

public class Lateral extends Jogador {
    private int cruzamento;
    private int cobertura;
    private int iniciativa;

    /**
     * Construtor por omissão
     */
    public Lateral(){
        super();
        cruzamento = 0;
        cobertura = 0;
        iniciativa = 0;
    }

    /**
     * Construtor parametrizado
     */
    public Lateral(String nome, int numero,int velocidade,int resistencia,int destreza,
                   int impulsao,int jogDeCabeca,int remate,int capacidadeDePasse,
                   int cruzamento, int cobertura, int iniciativa){
        super(nome,numero,velocidade,resistencia,destreza,impulsao,jogDeCabeca,remate,capacidadeDePasse);
        this.setCruzamento(cruzamento);
        this.setCobertura(cobertura);
        this.setIniciativa(iniciativa);
        this.calculaHabilidade();
    }

    /**
     * Construtor por cópia e com alguns parâmetros.
     */
    public Lateral(Jogador outro,int cruzamento,int cobertura,int iniciativa){
        super(outro);
        this.setCruzamento(cruzamento);
        this.setCobertura(cobertura);
        this.setIniciativa(iniciativa);
        this.calculaHabilidade();
    }

    /**
     * Construtor por cópia
     */
    public Lateral(Lateral outro){
        super(outro);
        this.setCruzamento(outro.cruzamento);
        this.setCobertura(outro.cobertura);
        this.setIniciativa(outro.iniciativa);
    }

    /**
     * Métodos SET
     */
    public void setCruzamento(int cruzamento) {
        this.cruzamento = betweenZeroToHundred(cruzamento);
    }
    public void setCobertura(int cobertura) {
        this.cobertura = betweenZeroToHundred(cobertura);
    }
    public void setIniciativa(int iniciativa) {
        this.iniciativa = betweenZeroToHundred(iniciativa);
    }

    /**
     * Métodos GET
     */
    public int getCruzamento() {
        return cruzamento;
    }
    public int getCobertura() {
        return cobertura;
    }
    public int getIniciativa() {
        return iniciativa;
    }

    /**
     * Método que calcula a Habilidade específica do jogador (lateral).
     */
    public int calculaHabilidade(){
        double res = 0;
        res+= 0.1 * this.getCaracteristica("velocidade");
        res+= 0.1 * this.getCaracteristica("resistencia");
        res+= 0.05 * this.getCaracteristica("destreza");
        res+= 0.1 * this.getCaracteristica("impulsao");
        res+= 0.1 * this.getCaracteristica("jogo de cabeca");
        res+= 0.05 * this.getCaracteristica("remate");
        res+= 0.1 * this.getCaracteristica("capacidade de passe");
        res+= 0.2 * cruzamento;
        res+= 0.1 * cobertura;
        res+= 0.1 * iniciativa;
        return (int) res;
    }

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("       Lateral: ").append(super.getNumero()).append(" - ").append(super.getNome()).append('\n');
        sb.append("Estado: ").append(super.estadoToString()).append(" / ")
                .append("Motivação: ").append(super.motivacaoToString()).append('\n');
        sb.append("Hablidade: ").append(super.getHabilidade()).append('\n');
        sb.append("VEL / RES / DES / IMP / JGC / REM / PAS - CRU / COB / INI").append('\n');
        sb.append(super.caracteristicasToString());
        sb.append(this.cruzamento).append("    ")
                .append(this.cobertura).append("    ")
                .append(this.iniciativa).append('\n');
        return sb.toString();
    }

    /**
     * Método clone
     */
    public Jogador clone() {
        return new Lateral(this);
    }
}
