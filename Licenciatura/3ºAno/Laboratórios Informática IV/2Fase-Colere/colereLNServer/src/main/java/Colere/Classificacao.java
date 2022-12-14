package Colere;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável pela Classificação média de um Local.
 * Contém uma map com o par (chave, valor) correspondente a (IP, Avaliação).
 * Para além disso, contém também um double referente à média das classificações
 * presentes no map. Tem como objetivo diminuir os número de vezes que a média do Local
 * é calculada, passando a ser atualizado sempre que se insere uma avaliação nova.
 */
public class Classificacao {

    /** Map de IP -> Avaliação */
    private Map<String,Integer> classificacoes;
    /** Classificação Média */
    private double media;

    /**
     * Construtor Vazio da Classe
     */
    public Classificacao(){
        this.classificacoes = new HashMap<>();
        this.media = calculaMedia();
    }

    /**
     * Método Get do map de classificações.
     * @return map das classificações.
     */
    public Map<String, Integer> getClassificacoes() {
        return classificacoes;
    }

    /**
     * Método get da classificação média.
     * @return classificação média.
     */
    public double getMedia() {
        return media;
    }

    /**
     * Método que devolve a média das classificações.
     * @return classificação média.
     */
    public double obterClassificacaoMedia(){
        return media;
    }

    /**
     * Método que adiciona uma avaliação de um dado IP.
     * Após a sua inserção no map, a média do mesmo é calculada e o valor
     * da média das classificações atualizado.
     * @param ip IP do utilizador avaliador.
     * @param novaClassificacao Avaliação do utilizador.
     */
    public void adicionarClassificacao(String ip,int novaClassificacao){
        this.classificacoes.put(ip,novaClassificacao);
        this.media = calculaMedia();
    }

    /**
     * Método privado (auxiliar) de cálculo da média das classificações.
     * @return Média das classificações.
     */
    private double calculaMedia(){
        if(this.classificacoes.isEmpty()) return 0;

        int n = 0,total = 0;
        for(Integer valor : this.classificacoes.values()){
            total += valor;
            n++;
        }

        double res = ((double) total/ (double) n);
        return Math.floor(res*100)/100;
    }

}
