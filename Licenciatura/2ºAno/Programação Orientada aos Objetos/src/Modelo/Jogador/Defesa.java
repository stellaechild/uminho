/**
 * SubClasse Defesa
 *
 * @author Maria Cunha
 * @date 21/04/2021
 */

public class Defesa extends Jogador {
    private int posicionamento;
    private int porte;
    private int marcacao;

    /**
     * Construtor por omissão
     */
    public Defesa(){
        super();
        this.posicionamento = 0;
        this.porte = 0;
        this.marcacao = 0;
    }

    /**
     * Construtor parametrizado
     */
    public Defesa(String nome, int numero,int velocidade,int resistencia,int destreza,
                  int impulsao,int jogDeCabeca,int remate,int capacidadeDePasse,
                  int posicionamento,int porte,int marcacao){
        super(nome,numero,velocidade,resistencia,destreza,impulsao,jogDeCabeca,remate,capacidadeDePasse);
        this.setPosicionamento(posicionamento);
        this.setPorte(porte);
        this.setMarcacao(marcacao);
        this.calculaHabilidade();
    }

    /**
     * Construtor por cópia parametrizado.
     */
    public Defesa(Jogador outro,int posicionamento,int porte,int marcacao){
        super(outro);
        this.setPosicionamento(posicionamento);
        this.setPorte(porte);
        this.setMarcacao(marcacao);
        this.calculaHabilidade();
    }

    /**
     * Construtor por cópia
     */
    public Defesa(Defesa outro){
        super(outro);
        this.setPosicionamento(outro.posicionamento);
        this.setPorte(outro.porte);
        this.setMarcacao(outro.marcacao);
    }

    /**
     * Métodos SET
     */
    public void setPosicionamento(int posicionamento) {
        this.posicionamento = betweenZeroToHundred(posicionamento);
    }
    public void setPorte(int porte) {
        this.porte = betweenZeroToHundred(porte);
    }
    public void setMarcacao(int marcacao) {
        this.marcacao = betweenZeroToHundred(marcacao);
    }

    /**
     * Métodos GET
     */
    public int getPosicionamento() {
        return posicionamento;
    }
    public int getPorte() {
        return porte;
    }
    public int getMarcacao() {
        return marcacao;
    }

    /**
     * Método que calcula a Habilidade específica do Defesa.
     */
    public int calculaHabilidade(){
        double res = 0;
        res+= 0.05 * this.getCaracteristica("velocidade");
        res+= 0.05 * this.getCaracteristica("resistencia");
        res+= 0.1 * this.getCaracteristica("destreza");
        res+= 0.1 * this.getCaracteristica("impulsao");
        res+= 0.02 * this.getCaracteristica("jogo de cabeca");
        res+= 0.03 * this.getCaracteristica("remate");
        res+= 0.15 * this.getCaracteristica("capacidade de passe");
        res+= 0.15 * posicionamento;
        res+= 0.15 * porte;
        res+= 0.2 * marcacao;
        return (int) res;
    }

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("        Defesa: ").append(super.getNumero()).append(" - ").append(super.getNome()).append('\n');
        sb.append("Estado: ").append(super.estadoToString()).append(" / ")
                .append("Motivação: ").append(super.motivacaoToString()).append('\n');
        sb.append("Hablidade: ").append(super.getHabilidade()).append('\n');
        sb.append("VEL / RES / DES / IMP / JGC / REM / PAS - POS / POR / MAR").append('\n');
        sb.append(super.caracteristicasToString());
        sb.append(this.posicionamento).append("    ")
                .append(this.porte).append("    ")
                .append(this.marcacao).append('\n');
        return sb.toString();
    }

    /**
     * Método clone
     */
    public Jogador clone() {
        return new Defesa ( this);
    }
}
