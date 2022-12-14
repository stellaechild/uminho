import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe Plantel
 *
 * @author Vicente Moreira
 * @version 20210327
 */

public class Plantel implements Serializable {

    public static final String DEFAULT = "1-4-3-3";
    public static final String DEFESA = "1-5-4-1";
    public static final String CONTROLE = "1-4-4-2";
    public static final String ATAQUE = "1-3-4-3";

    private Map<Integer,Jogador> nocampo;
    private Map<Integer,Jogador> nobanco;
    private String formacao;
    private List<Integer> amarelos;
    private List<Integer> expulsos;

    /**
     * Construtor por omissão
     */
    public Plantel(){
        this.nocampo = new HashMap<>();
        this.nobanco = new HashMap<>();
        this.formacao = DEFAULT;
        this.amarelos = new ArrayList<>();
        this.expulsos = new ArrayList<>();
    }

    /**
     * Contrutor de cópia
     */
    public Plantel(Plantel outro){
        this.nocampo = outro.nocampo.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
        this.nobanco = outro.nobanco.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
        this.formacao = outro.formacao;
        this.amarelos = outro.amarelos.stream().collect(Collectors.toList());
        this.expulsos = outro.expulsos.stream().collect(Collectors.toList());
    }

    /**
     * Métodos Set
     */
    public void setNocampo(Map<Integer, Jogador> nocampo) {
        this.nocampo = nocampo.values().stream().map(Jogador :: clone).collect(Collectors.toMap(e -> e.getNumero(),e -> e));
    }
    public void setNobanco(Map<Integer, Jogador> nobanco) {
        this.nobanco = nobanco.values().stream().map(Jogador :: clone).collect(Collectors.toMap(e -> e.getNumero(),e -> e));;
    }
    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }
    public void setAmarelos(List<Integer> amarelos) {
        this.amarelos = amarelos.stream().collect(Collectors.toList());
    }
    public void setExpulsos(List<Integer> expulsos) {
        this.expulsos = expulsos.stream().collect(Collectors.toList());
    }

    /**
     * Método Clone
     */
    public Plantel clone(){
        return new Plantel(this);
    }

    /**
     * Set Plantel full
     */
    public void setAllJogs(Map<Integer, Jogador> nocampo,Map<Integer, Jogador> nobanco){
        setNocampo(nocampo);
        setNobanco(nobanco);
    }
}
