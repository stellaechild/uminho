import java.util.*;

public class Stats implements IStats {

    /** Linhas de Users lidos */    private long users_lidos;
    /** Linhas de Negócios lidos */ private long bus_lidos;
    /** Linhas de Reviews lidas */  private long rev_lidos;

    /** Users válidos */    private long users_validos;
    /** Negócios válidos */ private long bus_validos;
    /** Reviews válidas */  private long rev_validos;

    /** Users inválidos */    private long users_invalidos;
    /** Negócios inválidos */ private long bus_invalidos;
    /** Reviews inválidas */  private long rev_invalidos;

    /** Negócios com reviews */ private long bus_avaliados;
    /** Negócios sem reviews */ private long bus_nao_avaliados;

    /** Users com reviews */ private long users_avaliadores;
    /** Users sem reviews */ private long users_nao_avaliadores;

    /** Reviews sem impacto. Funny==Cool==Useful == 0 */ private long revs_sem_impacto;

    /** Index de Users e a sua respetiva lista de Reviews */    private SortedMap<String, List<String>> usrToRev;
    /** Index de Negócios e a sua respetiva lista de Reviews */ private SortedMap<String, List<String>> busToRev;
    /** Index ordenado de datas e as respetivas reviews */      private SortedMap<String, String> dataToRev;

    /**
     * Construtor por omissão
     */
    public Stats () {
        this.users_validos = 0; this.bus_validos = 0; this.rev_validos = 0;
        this.users_invalidos = 0; this.bus_invalidos = 0; this.rev_invalidos = 0;
        this.bus_avaliados = 0; this.bus_nao_avaliados = 0;
        this.users_avaliadores = 0; this.users_nao_avaliadores = 0;
        this.revs_sem_impacto = 0;

        usrToRev = new TreeMap<>();
        busToRev = new TreeMap<>();
        dataToRev = new TreeMap<>();
    }

    /**
     * Método de adicição de Reviews às estatisticas. Esta recolhe todas as informações
     * relevantes para os indexes apropriados.
     * @param rev Review Adicionada
     */
    public void addRevToStats(IReviews rev) {
        addUsrToRev(rev.getUserID(),rev.getReviewID());
        addBusToRev(rev.getBusinessID(),rev.getReviewID());
        addDataToRev(rev.getDateString(),rev.getReviewID());

        if(rev.getCool() + rev.getFunny() + rev.getUseful() == 0) this.revs_sem_impacto++;
    }

    /**
     * Método para consulta do index User -> Revs. Não efetua clonamento (Agregação)
     * @return Index
     */
    public SortedMap<String, List<String>> getUsrToRev(){return this.usrToRev;}

    /**
     * Método para consulta do index Biz -> Revs. Não efetua clonamento (Agregação)
     * @return Index
     */
    public SortedMap<String, List<String>> getBizToRev(){return this.busToRev;}

    /**
     * Método para consulta do index Data -> Rev. Não efetua clonamento (Agregação)
     * @return Index
     */
    public SortedMap<String,String> getDataToRev (){return this.dataToRev; }

    /**
     * Adição ao Index User to Rev (Lista de Users e as suas reviews)
     * Caso o User em questão já exista no index. Apenas adiciona a review à sua lista.
     * Caso contrário cria uma nova entrada.
     * @param user User na entrada
     * @param review Review correspondente
     */
    public void addUsrToRev (String user, String review) {
        if (this.usrToRev.containsKey(user)) this.usrToRev.get(user).add(review);
        else {
            List<String> novo = new ArrayList<>();
            novo.add(review);
            this.usrToRev.put(user, novo);
        }
    }

    /**
     * Adição à lista de tradução Data -> review. Permite consultar as reviews por ordem de data.
     * @param data Data da review
     * @param review Id da review
     */
    public void addDataToRev(String data,String review){
        this.dataToRev.put(data,review);
    }

    /**
     * Adição ao Index Bus to Rev (Lista de Businesses e as suas reviews)
     * Caso o Business em questão já exista no index. Apenas adiciona a review à sua lista.
     * Caso contrário cria uma nova entrada.
     * @param bus Business na entrada
     * @param review Review correspondente
     */
    public void addBusToRev (String bus, String review) {
        if (this.busToRev.containsKey(bus)) this.busToRev.get(bus).add(review);
        else {
            List<String> novo = new ArrayList<>();
            novo.add(review);
            this.busToRev.put(bus, novo);
        }
    }

    /**
     * Método para calculo das estatísticas, utilizando as linhas lidas e os elementos válidos
     * @param total_usrs Users lidos    (Linhas)
     * @param valid_usrs Users válido
     * @param total_bizs Negócios lidos (Linhas)
     * @param valid_bizs Negócios válido
     * @param total_revs Reviews lidas  (Linhas)
     * @param valid_revs Reviews válido
     */
    public void calcula_stats(long total_usrs,long valid_usrs,long total_bizs,long valid_bizs,long total_revs,long valid_revs){
        this.users_validos = valid_usrs; this.bus_validos = valid_bizs; this.rev_validos = valid_revs;
        this.users_lidos = total_usrs;this.bus_lidos = total_bizs;this.rev_lidos = total_revs;

        this.users_invalidos = this.users_lidos - this.users_validos;
        this.bus_invalidos = this.bus_lidos - this.bus_validos;
        this.rev_invalidos = this.rev_lidos - this.rev_validos;

        this.users_avaliadores = this.usrToRev.size();
        this.bus_avaliados = this.busToRev.size();

        this.users_nao_avaliadores = this.users_validos - this.users_avaliadores;
        this.bus_nao_avaliados = this.bus_validos - this.bus_avaliados;
    }

    /**
     * Método principal de leitura de Stats
     * @return String formatada.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("------------------------------STATS------------------------------\n");
        sb.append("Users      - Válidos: ").append(users_validos).append("  | Inválidos: ").append(users_invalidos).append("\n");
        sb.append("Businesses - Válidos: ").append(bus_validos).append("   | Inválidos: ").append(bus_invalidos).append("\n");
        sb.append("Reviews    - Válidas: ").append(rev_validos).append("   | Inválidas: ").append(rev_invalidos).append("\n");
        sb.append("Users com reviews:      ").append(users_avaliadores).append(" | Users sem Reviews: ").append(users_nao_avaliadores).append("\n");
        sb.append("Businesses com reviews: ").append(bus_avaliados).append("  | Businesses sem Reviews: ").append(bus_nao_avaliados).append("\n");
        sb.append("Reviews sem impacto:    ").append(revs_sem_impacto).append("\n");
        return sb.toString();
    }
}
