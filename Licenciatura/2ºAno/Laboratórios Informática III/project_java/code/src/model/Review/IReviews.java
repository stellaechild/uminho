import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Interface para classes que suportem "reviews"
 */

public interface IReviews extends Serializable {

    /**
     * Método de classe que determina se uma lista de Strings pode ser qualificada como uma "Review"
     * @param line Lista de strings
     * @return Indicador de validade
     */
    public static Boolean IReviewValido(String[] line){
        return Stream.of(line).noneMatch(s -> s.equals("")) && line.length == 9;
    }

    /**
     * Método get do ID da Review
     * @return ReviewID
     */
    public String getReviewID();
    /**
     * Método get do ID do User da Review
     * @return UserID
     */
    public String getUserID();
    /**
     * Método get do ID do Business da Review
     * @return BusinessID
     */
    public String getBusinessID();
    /**
     * Método get das estrelas da Review
     * @return Stars
     */
    public float getStars();
    /**
     * Método get do "Useful" da Review
     * @return Useful
     */
    public int getUseful();
    /**
     * Método get do "Funny" da Review
     * @return Funny
     */
    public int getFunny();
    /**
     * Método get do "Cool" da Review
     * @return Cool
     */
    public int getCool();
    /**
     * Método get da data da Review
     * @return Data
     */
    public LocalDateTime getDate();
    /**
     * Método get da data da Review em formato String
     * @return Data em String
     */
    public String getDateString();
    /**
     * Método get do texto da Review
     * @return Texto
     */
    public String getText();

    /**
     * Método de Clone
     * @return IReview clonado
     */
    public IReviews clone();

    /**
     * Método semelhante "toString", devolve uma linha formatada com os campos a pedido do utilizador
     * @param rev Campo ID Review
     * @param usr Campo ID User
     * @param biz Campo ID Business
     * @param stars Campo Stars
     * @param useful Campo Useful
     * @param funny Campo Funny
     * @param cool Campo Cool
     * @param date Campo Data
     * @param text Campo Text
     * @return String formatada
     */
    public String toStringValues(Boolean rev,Boolean usr,Boolean biz,
                          Boolean stars,Boolean useful,Boolean funny,
                          Boolean cool,Boolean date,Boolean text);
}
