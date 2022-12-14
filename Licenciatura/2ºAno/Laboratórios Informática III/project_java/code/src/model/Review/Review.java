import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Classe das reviews simples
 */

public class Review implements IReviews{

    /** Identificador da review */                      private String reviewID;
    /** Identificador do utilizador que fez a review */ private String userID;
    /** Identificador do negócios reviewed */           private String businessID;
    /** Classificação atribuída */                      private float stars;
    /** Classificação "Useful" da review */             private int useful;
    /** Classificação "Funny" da review */              private int funny;
    /** Classificação "Cool" da review */               private int cool;
    /** Data da review */                               private LocalDateTime date;
    /** Texto da review */                              private String text;

    /**
     * Construtor por omissão
     */
    public Review(){
        this.reviewID = "";
        this.userID = "";
        this.businessID = "";
        this.stars = 0;
        this.useful = 0;
        this.funny = 0;
        this.cool = 0;
        this.date = LocalDateTime.of(2000,1,1,0,0,0);
        this.text = "";
    }

    /**
     * Construtor que recebe uma lista de Strings previamente verificada, e constroi uma Review
     * @param line Lista de String fonte
     */
    public Review(String[] line){
        this.reviewID = line[0];
        this.userID = line[1];
        this.businessID = line[2];
        this.stars = Float.parseFloat(line[3]);
        this.useful = Integer.parseInt(line[4]);
        this.funny = Integer.parseInt(line[5]);
        this.cool = Integer.parseInt(line[6]);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = LocalDateTime.parse(line[7],format);
        this.text = line[8];
    }

    /**
     * Construtor de cópia
     * @param outro Alvo copiado
     */
    public Review(Review outro){
        this.reviewID = outro.getReviewID();
        this.userID = outro.getUserID();
        this.businessID = outro.getBusinessID();
        this.stars = outro.getStars();
        this.useful = outro.getUseful();
        this.funny = outro.getFunny();
        this.cool = outro.getCool();
        this.date = outro.getDate();
        this.text = outro.getText();
    }


    /**
     * Método get do ID da Review
     * @return ReviewID
     */
    public String getReviewID() {
        return reviewID;
    }
    /**
     * Método get do ID do User da Review
     * @return UserID
     */
    public String getUserID() {
        return userID;
    }
    /**
     * Método get do ID do Business da Review
     * @return BusinessID
     */
    public String getBusinessID() {
        return businessID;
    }
    /**
     * Método get das estrelas da Review
     * @return Stars
     */
    public float getStars() {
        return stars;
    }
    /**
     * Método get do "Useful" da Review
     * @return Useful
     */
    public int getUseful() {
        return useful;
    }
    /**
     * Método get do "Funny" da Review
     * @return Funny
     */
    public int getFunny() {
        return funny;
    }
    /**
     * Método get do "Cool" da Review
     * @return Cool
     */
    public int getCool() {
        return cool;
    }
    /**
     * Método get da data da Review
     * @return Data
     */
    public LocalDateTime getDate() {
        return date;
    }
    /**
     * Método get da data da Review em formato String
     * @return Data em String
     */
    public String getDateString(){
        return this.date.toString();
    }
    /**
     * Método get do texto da Review
     * @return Texto
     */
    public String getText() {
        return text;
    }
    /**
     * Método de Clone
     * @return IReview clonado
     */
    public IReviews clone(){
        return new Review(this);
    }

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
                                 Boolean cool,Boolean date,Boolean text){
        StringBuilder sb = new StringBuilder("");
        if(rev) sb.append(getReviewID()+" ; ");
        if(usr) sb.append(getUserID()+" ; ");
        if(biz) sb.append(getBusinessID()+" ; ");
        if(stars) sb.append(String.valueOf(getStars())+" ; ");
        if(useful) sb.append(String.valueOf(getUseful())+" ; ");
        if(funny) sb.append(String.valueOf(getFunny())+" ; ");
        if(cool) sb.append(String.valueOf(getCool())+" ; ");
        if(date) sb.append(getDate().toString()+" ; ");
        if(text) sb.append(getText());
        sb.append("\n");
        return sb.toString();
    }

}
