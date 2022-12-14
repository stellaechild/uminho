import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Classe do utilizadores simples
 */

public class User implements IUser {

    /** Identificador do Utilizador */   private String userID;
    /** Nome do Utilizador */            private String name;
    /** Lista de amigos do utilizador */ private List<String> friends;

    /**
     * Construtor por omissão
     */
    public User(){
        this.userID = "";
        this.name = "";
        this.friends = new ArrayList<>();
    }

    /**
     * Construtor que recebe uma lista de Strings previamente verificada, e constroi um User
     * @param line Lista de String fonte
     * @param load_friends Opção para carregar amigos
     */
    public User(String[] line,Boolean load_friends){
        this.userID = line[0];
        this.name = line[1];
        this.friends = new ArrayList<>();
        if(load_friends){
            this.friends = new ArrayList<>(Arrays.asList(line[2].split(",")));
            if(friends.size() == 1 && friends.contains("None")){
                friends.remove("None");
            }
        }
    }

    /**
     * Construtor de cópia
     * @param outro Alvo a ser copiado
     */
    public User(User outro){
        this.userID = outro.getUserID();
        this.name = outro.getName();
        this.friends = outro.getFriends();
    }

    /**
     * Get UserID
     * @return UserID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Get UserID
     * @return UserID
     */
    public String getName() {
        return name;
    }

    /**
     * Get Friends List
     * @return Friends List
     */
    public List<String> getFriends() {
        return new ArrayList<>(this.friends);
    }

    /**
     * Get Number of Friends
     * @return Number of Friends
     */
    public int getNumFriends() {
        return this.friends.size();
    }

    /**
     * Método Clone
     * @return IUser clonado
     */
    public IUser clone(){
        return new User(this);
    }

    /**
     * Método semelhante "toString", devolve uma linha formatada com os campos a pedido do utilizador
     * @param usr Campo User
     * @param name Campo Name
     * @param numFriends Campo do número de amigos
     * @param friends Campo de lista de amigos
     * @return String formatada
     */
    public String toStringValues(Boolean usr,Boolean name,Boolean numFriends,Boolean friends){
        StringBuilder sb = new StringBuilder("");
        if(usr) sb.append(getUserID()+" ; ");
        if(name) sb.append(getName()+" ; ");
        if(numFriends) sb.append(String.valueOf(getNumFriends())+" ; ");
        if(friends) sb.append(getFriends().toString());
        sb.append("\n");
        return sb.toString();
    }
}
