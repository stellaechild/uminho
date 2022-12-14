import java.io.Serializable;

/**
 * Classe Treinador
 *
 * @author Joana Maia
 * @version 20210323
 */

public class Treinador implements Serializable {

    public static final int PRINCIPAL = 0;
    public static final int ADJUNTO = 1;
    public static final int SUPLENTE = 2;

    private String nome;
    private int qualidade;
    private int cargo;

    /**
     * Construtor por omissão
     * Cria um treinador suplente sem nome e sem qualidade
     */
    public Treinador() {
        this.nome = "n/a";
        this.qualidade = 0;
        this.cargo = SUPLENTE;
    }

    /**
     * Construtor parametrizado
     * Constroi um treinador com o seu nome, qualidade e cargo;
     */
    public Treinador (String nome, int qualidade, int cargo) {
        this.nome = nome;
        this.qualidade = qualidade;
        this.cargo = cargo;
    }

    /**
     * Construtor por cópia.
     * Constroi um treinador por cópia, assume-se que o treinador fornecido é previamente clonado.
     */
    public Treinador (Treinador outroT) {
        this.nome = outroT.getNome();
        this.qualidade = outroT.getQualidade();
        this.cargo = outroT.getCargo();
    }

    /**
     * Métodos SET
     */
    void setNome(String nome) {this.nome = nome;}
    void setQualidade (int q) {this.qualidade = q;}
    void setCargo (int c) {this.cargo = c;}

    /**
     * Métodos GET
     */
    String getNome() {return this.nome;}
    int getQualidade() {return this.qualidade;}
    int getCargo() {return this.cargo;}

    /**
     * Método toString
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Treinador ").append(nome).append('\n');
        sb.append("Cargo: ").append(this.estadoToString()).append(" | Habilidade: ").append(qualidade).append("\n\n");
        return sb.toString();
    }

    /**
     * Método ToString Cargo
     */
    public String estadoToString(){
        switch (this.cargo){
            case 0: return "Principal";
            case 1: return "Adjunto";
            case 2: return "Suplente";
            default: return "Erro :c";
        }
    }

    /**
     * Método Clone
     * @return Clone
     */
    public Treinador clone(){
        return new Treinador(this);
    }
}
