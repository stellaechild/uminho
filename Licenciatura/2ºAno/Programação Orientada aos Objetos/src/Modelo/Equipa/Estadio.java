import java.io.Serializable;

/**
 * Classe Estadio.
 *
 * @author Vicente Moreira
 * @version 25/03/2021
 */

public class Estadio implements Serializable {
    private String nome;
    private String local;
    private int numerojogos;
    private int capacidade;

    /**
     * Construtor por omissão
     */
    public Estadio(){
        nome = "n/a";
        local = "n/a";
        numerojogos = 0;
        capacidade = 0;
    }

    /**
     * Construtor parametrizado
     */
    public Estadio(String nome,String local,int capacidade){
        this.nome = nome;
        this.local = local;
        this.numerojogos = 0;
        this.capacidade = capacidade;
    }

    /**
     * Construtor por cópia
     */
    public Estadio(Estadio outro){
        this.nome = outro.getNome();
        this.local = outro.getLocal();
        this.numerojogos = outro.getNumerojogos();
        this.capacidade = outro.getCapacidade();
    }

    /**
     * Métodos SET
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public void setNumerojogos(int numerojogos) {
        this.numerojogos = numerojogos;
    }
    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    /**
     * Métodos GET
     */
    public String getNome() {
        return nome;
    }
    public String getLocal() {
        return local;
    }
    public int getNumerojogos() {
        return numerojogos;
    }
    public int getCapacidade() {
        return capacidade;
    }

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder("Estádio ");
        sb.append(nome).append('\n');
        sb.append(local);
        sb.append("  / Capacidade: ").append(capacidade);
        sb.append("/ Jogos Realizados: ").append(numerojogos);
        sb.append('\n');
        return sb.toString();
    }

    /**
     * Método Equals
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o.getClass() != Estadio.class) || o == null) return false;
        Estadio estadio = (Estadio) o;
        return (this.getNome().equals(estadio.getNome())) &&
                this.getLocal().equals(estadio.getLocal());
    }

    /**
     * Método Clone
     */
    public Estadio clone(){
        return new Estadio(this);
    }

}
