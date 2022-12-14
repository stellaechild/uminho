import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Interface para classes que suportem "negócios"
 */

public interface IBusiness extends Serializable {

    /**
     * Método de classe que determina se uma lista de Strings pode ser qualificada como um "Business"
     * @param line Lista de strings
     * @return Indicador de validade
     */
    public static Boolean IBusinessValido(String[] line){
        return Stream.of(line).noneMatch(s -> s.equals("")) && line.length == 5;
    }

    /**
     * Método get do ID do negócio
     * @return BusinessID
     */
    public String getBusinessID();
    /**
     * Método get do Nome do negócio
     * @return Name
     */
    public String getName();
    /**
     * Método get da cidade do negócio
     * @return City
     */
    public String getCity();
    /**
     * Método get do estado do negócio
     * @return State
     */
    public String getState();
    /**
     * Método get da lista de categorias do negócio
     * @return Lista de Categorias
     */
    public List<String> getCategories();
    /**
     * Método get do número de categorias do negócio
     * @return NumCategorias
     */
    public int getNumCategories();

    /**
     * Método de Clone
     * @return IBusiness clonado
     */
    public IBusiness clone();

    /**
     * Método semelhante "toString", devolve uma linha formatada com os campos a pedido do utilizador
     * @param biz Campo ID
     * @param name Campo Nome
     * @param city Campo City
     * @param state Campo State
     * @param numCat Campo Num categorias
     * @param cat Campo Lista de categorias
     * @return String formatada
     */
    public String toStringValues(Boolean biz,Boolean name,Boolean city,
                                 Boolean state,Boolean numCat,Boolean cat);
}
