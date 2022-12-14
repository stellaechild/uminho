import java.io.IOException;
import java.io.Serializable;

/**
 * @Author Maria Eugénia
 * @Date 07/06/2021
 *
 * Interface do gestor de Reviews
 */

public interface IgestReviews extends Serializable{

    /** Nome do ficheiro de save default */       String dat =       "gestReviews.dat";
    /** Nome do ficheiro .csv dos utilizadores */ String user_path = "../../project_c/input_files/users_full.csv";
    /** Nome do ficheiro .csv dos negócios */     String bus_path =  "../../project_c/input_files/business_full.csv";
    /** Nome do ficheiro .csv dos reviews */      String rev_path =  "../../project_c/input_files/reviews_1M.csv";

    /**
     * Método principal para carregar a informação para o gestReviews
     * Carrega também as Stats necessárias
     * @param path_user Path para o ficheiro dos Users
     * @param path_bus Path para o ficheiro dos Businesses
     * @param path_rev Path para o ficheiro dos Reviews
     * @throws IOException Falha na leitura ou abertura de ficheiro.
     */
    public void load_IgestReviews_csv (String path_user, String path_bus, String path_rev) throws UserIOException,ReviewIOException,BusinessIOException ;

    /**
     * Devolve as Stats completas para serem escritas no terminal
     * @return String formatada
     */
    public String statsToString();
    /**
     * Verificação de gestReviews válido
     * @return Boolean
     */
    public Boolean valido();

    public String q1_listaNegociosNaoAvaliados();
    public String q2_reviewsNumMes(int ano, int mes);
    public String q3_infoUserReviews(String user);
    public String q4_avaliacaoMesAMes(String negocio);
    public String q5_userNegociosMaisAvaliados(String user);
    public String q6_conjuntoXNegociosMaisAvaliados(int x);
    public String q7_cityHallOfFame();
    public String q8_usersComMaisReviews(int x);
    public String q9_usersMaisAvaliaramNegocio(String negocio,int x);
    public String q10_medias();
}
