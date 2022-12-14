import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Classe Classificao
 *
 * @author Maria Cunha.
 * @version 20210511
 */

public class Classificacao implements Serializable {

    private String equipa;
    private int pontos;
    private int golosmarcados;

    /**
     * Construtor por omissão
     */
    public Classificacao(){
        this.equipa = "n/a";
        this.pontos = 0;
        this.golosmarcados = 0;
    }

    /**
     * Construtor parametrizado
     */
    public Classificacao(String Equipa){
        this.equipa = Equipa;
        this.pontos = 0;
        this.golosmarcados = 0;
    }

    /**
     * Construtor por cópia
     */
    public Classificacao(Classificacao outro){
        this.equipa = outro.equipa;
        this.pontos = outro.pontos;
        this.golosmarcados = outro.golosmarcados;
    }

    /**
     * Métodos SET
     */
    public void setEquipa(String equipa) {
        this.equipa = equipa;
    }
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
    public void setGolosmarcados(int golosmarcados) {
        this.golosmarcados = golosmarcados;
    }

    /**
     * Métodos GET
     */
    public String getEquipa() {
        return equipa;
    }
    public int getPontos() {
        return pontos;
    }
    public int getGolosmarcados() {
        return golosmarcados;
    }

    /**
     * Método Equals
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if ( !this.getClass().equals(o) || o == null) return false;
        Classificacao that = (Classificacao) o;
        return getPontos() == that.getPontos() &&
                getGolosmarcados() == that.getGolosmarcados() &&
                Objects.equals(getEquipa(), that.getEquipa());
    }

    /**
     * Método Clone
     */
    public Classificacao clone(){
        return new Classificacao(this);
    }

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder(equipa);
        sb.append(" - ").append(pontos).append("/").append(golosmarcados).append('\n');
        return sb.toString();
    }

    /**
     * Comparator de Classificação
     */
    public Comparator<Classificacao> ordemDecrescentePontos(){
        return (v1,v2) -> v1.getPontos() == v2.getPontos() ?
                v1.getPontos() - v2.getPontos() :
                v1.getGolosmarcados() - v2.getGolosmarcados();
    }
}
