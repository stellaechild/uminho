import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Classe dos negócios simples
 */

public class Business implements IBusiness{

    /** Identificador do negócio */ private String businessID;
    /** Nome do negócio */          private String name;
    /** Cidade do negócio */        private String city;
    /** Estado do negócio */        private String state;
    /** Categorias do negócio */    private List<String> categories;

    /**
     * Construtor por omissão
     */
    public Business(){
        this.businessID= "";
        this.name= "";
        this.city= "";
        this.state= "";
        this.categories = new ArrayList<>();
    }

    /**
     * Construtor que recebe uma lista de Strings previamente verificada, e constroi um Business
     * @param line Lista de String fonte
     */
    public Business(String[] line){
        this.businessID = line[0];
        this.name = line[1];
        this.city = line[2];
        this.state = line[3];
        this.categories = new ArrayList<>(Arrays.asList(line[4].split(",")));
    }

    /**
     * Construtor de cópia
     * @param outro Alvo copiado
     */
    public Business(Business outro){
        this.businessID= outro.businessID;
        this.name= outro.getName();
        this.city= outro.getCity();
        this.state= outro.getState();
        this.categories = outro.getCategories();
    }

    /**
     * Método get do ID do negócio
     * @return BusinessID
     */
    public String getBusinessID() {
        return businessID;
    }
    /**
     * Método get do Nome do negócio
     * @return Name
     */
    public String getName() {
        return name;
    }
    /**
     * Método get da cidade do negócio
     * @return City
     */
    public String getCity() {
        return city;
    }
    /**
     * Método get do estado do negócio
     * @return State
     */
    public String getState() {
        return state;
    }
    /**
     * Método get da lista de categorias do negócio
     * @return Lista de Categorias
     */
    public List<String> getCategories() {
        return new ArrayList<>(this.categories);
    }
    /**
     * Método get do número de categorias do negócio
     * @return NumCategorias
     */
    public int getNumCategories() {
        return this.categories.size();
    }
    /**
     * Método de Clone
     * @return IBusiness clonado
     */
    public IBusiness clone(){
        return new Business(this);
    }

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
                                 Boolean state,Boolean numCat,Boolean cat){
        StringBuilder sb = new StringBuilder("");
        if(biz) sb.append(getBusinessID()+" ; ");
        if(name) sb.append(getName()+" ; ");
        if(city) sb.append(getCity()+" ; ");
        if(state) sb.append(getState()+" ; ");
        if(numCat) sb.append(String.valueOf(getNumCategories())+" ; ");
        if(cat) sb.append(getCategories().toString());
        sb.append("\n");
        return sb.toString();
    }

}
