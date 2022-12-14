import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Classe gestReviews
 */

public interface IStats extends Serializable {

    /**
     * Método de adicição de Reviews às estatisticas. Esta recolhe todas as informações
     * relevantes para os indexes apropriados.
     * @param rev Review Adicionada
     */
    public void addRevToStats (IReviews rev);

    /**
     * Método para consulta do index User -> Revs. Não efetua clonamento (Agregação)
     * @return Index
     */
    public SortedMap<String, List<String>> getUsrToRev();

    /**
     * Método para consulta do index Biz -> Revs. Não efetua clonamento (Agregação)
     * @return Index
     */
    public SortedMap<String, List<String>> getBizToRev();

    /**
     * Método para consulta do index Data -> Rev. Não efetua clonamento (Agregação)
     * @return Index
     */
    public SortedMap<String,String> getDataToRev();

    /**
     * Método para calculo das estatísticas, utilizando as linhas lidas e os elementos válidos
     * @param total_usrs Users lidos    (Linhas)
     * @param valid_usrs Users válido
     * @param total_bizs Negócios lidos (Linhas)
     * @param valid_bizs Negócios válido
     * @param total_revs Reviews lidas  (Linhas)
     * @param valid_revs Reviews válido
     */
    public void calcula_stats(long total_usrs,long valid_usrs,long total_bizs,long valid_bizs,long total_revs,long valid_revs);

    /**
     * Método principal de leitura de Stats
     * @return String formatada.
     */
    public String toString();

}
