/**
 * Classe Histórico
 *
 * Armazena o histórico de um Jogador
 *
 * @author Vicente Moreira
 * @date 1/05/2021
 */

import java.io.Serializable;
import java.time.LocalDate;

public class Historico implements Serializable {

    private LocalDate entryDate;
    private LocalDate leftDate;
    private String equipa;
    private int golosmarcados;
    private int jogospresente;

    /**
     * Construtor por Omissão
     */
    public Historico(){
        this.entryDate = LocalDate.now();
        this.leftDate = LocalDate.now();
        this.equipa = "n/a";
        this.golosmarcados = 0;
        this.jogospresente = 0;
    }

    /**
     * Construtor parameterizado
     */
    public Historico(LocalDate entrada,String Equipa){
        this.entryDate = entrada;
        this.leftDate = LocalDate.now();
        this.equipa = Equipa;
        this.golosmarcados = 0;
        this.jogospresente = 0;
    }

    /**
     * Construtor de cópia
     */
    public Historico(Historico outro){
        this.entryDate = outro.getEntryDate();
        this.leftDate = outro.getLeftDate();
        this.equipa = outro.getEquipa();
        this.golosmarcados = outro.getGolosmarcados();
        this.jogospresente = outro.getJogospresente();
    }

    /**
     * Métodos GET
     */
    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
    public void setLeftDate(LocalDate leftDate) {
        this.leftDate = leftDate;
    }
    public void setEquipa(String equipa) {
        this.equipa = equipa;
    }
    public void setGolosmarcados(int golosmarcados) {
        this.golosmarcados = golosmarcados;
    }
    public void setJogospresente(int jogospresente) {
        this.jogospresente = jogospresente;
    }

    /**
     * Métodos GET
     */
    public LocalDate getEntryDate() {
        return entryDate;
    }
    public LocalDate getLeftDate() {
        return leftDate;
    }
    public String getEquipa() {
        return equipa;
    }
    public int getGolosmarcados() {
        return golosmarcados;
    }
    public int getJogospresente() {
        return jogospresente;
    }

    /**
     * Método Equals
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o.getClass() != Historico.class) || o == null) return false;
        Historico historico = (Historico) o;
        return getEntryDate().equals(historico.getEntryDate()) &&
                getEquipa().equals(historico.getEquipa());
    }

    /**
     * Método Clone
     */
    public Historico clone(){
        return new Historico(this);
    }

    /**
     * Método que devolve o tempo que o jogador esteve numa Equipa, devolvido em número de dias
     */
    public long tempoNaEquipa(){
        long dat1 = this.entryDate.toEpochDay();
        long dat2 = this.leftDate.toEpochDay();
        return dat2-dat1;
    }

    /**
     * Método que incrementa os jogos feitos do jogador
     */
    public void incJogosFeitos(){
        this.jogospresente++;
    }

    /**
     * Método que incrementa os golos marcados
     */
    public void incGolosMarcados(){
        this.golosmarcados ++;
    }

}
