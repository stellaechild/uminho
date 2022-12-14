import java.time.Duration;
import java.time.LocalDateTime;

public class Session {

    /** Tempo da sessão: 5 minutes*/
    private final Duration sessionTime = Duration.ofMinutes(5);
    /** Nome do utilizador da sessão */
    private String name;
    /** Data de expiração da sessão */
    private LocalDateTime expires;

    /**
     * Método construtor simples, recebe o nome do utilizador
     * @param name Nome do Utilizador
     */
    public Session(String name){
        this.name = name;
        updateSession();
    }

    public String getName() {return name;}

    /**
     * Método de verificação se a sessão foi expirada.
     * @return boolean. True se expirado
     */
    public Boolean isExpired(){
        LocalDateTime now = LocalDateTime.now();
        if (this.expires.isBefore(now)) return true;
        return false;
    }

    /**
     * Método para atualizar a sessão. Cria uma nova data de expiração utilizando o tempo de sessão fixo
     */
    public void updateSession(){
        LocalDateTime time = LocalDateTime.now();
        this.expires = time.plus(sessionTime);
        return;
    }
}
