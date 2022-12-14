import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Interface para classes que suportem "utilizadores"
 */

public interface IUser extends Serializable {

    /**
     * Método de classe que determina se uma lista de Strings pode ser qualificada como um "User"
     * @param line Lista de strings
     * @return Indicador de validade
     */
    public static Boolean IUserValido (String[] line){
        return Stream.of(line).noneMatch(s -> s.equals("")) && line.length == 3;
    }

    /**
     * Método get do ID do utilizador
     * @return UserID
     */
    public String getUserID ();
    /**
     * Método get do nome do utilizador
     * @return Nome
     */
    public String getName ();
    /**
     * Método get da lista de amigos do utilizador
     * @return Lista de Friends
     */
    public List<String> getFriends ();
    /**
     * Método get do número de amigos do utilizador
     * @return NumFriends
     */
    public int getNumFriends();

    /**
     * Método de Clone
     * @return IUser clonado
     */
    public IUser clone();

    /**
     * Método semelhante "toString", devolve uma linha formatada com os campos a pedido do utilizador
     * @param usr Campo User
     * @param name Campo Name
     * @param numFriends Campo do número de amigos
     * @param friends Campo de lista de amigos
     * @return String formatada
     */
    public String toStringValues(Boolean usr,Boolean name,Boolean numFriends,Boolean friends);
}
