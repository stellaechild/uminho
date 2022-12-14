import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserRegistry {

    /** Lista de todos os Utilizadores */
    private Map<String,User> users;
    /** Lista de sessões */
    private Map<String,Session> sessions;
    /** Lock de concorrência */
    private ReentrantReadWriteLock lock;

    /**
     * Método construtor simples. Recebe o path para um ficheiro que contém a dos Utilizadores.
     * Caso o Path esteja vazio, não carrega nenhum User.
     * @param path Caminho para o ficheiro
     * @throws IOException Erro de IO
     */
    public UserRegistry(String path) throws IOException{
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        if (!path.isEmpty()) readUsers(path);
    }

    /**
     * Método que retorna o nome do utilizador responsável por uma sessão
     * @param idSession Id da sessão
     * @return Nome do utilizador
     * @throws InexistentSessionException Sessão inexistente
     */
    public String getUsername(String idSession) throws InexistentSessionException{
        try {
            lock.readLock().lock();
            if (this.sessions.containsKey(idSession)) {
                return this.sessions.get(idSession).getName();
            }
            throw new InexistentSessionException();
        } finally { lock.readLock().unlock();}
    }

    /**
     * Método para verificar se uma sessão está expirada, caso não esteja, atualiza-a
     * @param idSession ID da Sessão
     * @throws InexistentSessionException Sessão expirada ou inexistente
     */
    public void checkAndUpdateSession(String idSession) throws InexistentSessionException{
        try {
            lock.readLock().lock();
            if (this.sessions.containsKey(idSession)) {
                if (!this.sessions.get(idSession).isExpired()) {
                    this.sessions.get(idSession).updateSession();
                    return;
                } else {
                    this.sessions.remove(idSession);
                }
            }
            throw new InexistentSessionException();
        }finally {lock.readLock().unlock();}
    }

    /**
     * Método de Login dos utilizadores. Verifica o nome e password, e gera um ID de sessão.
     * @param name Nome do utilizador
     * @param pass Password do utilizador
     * @return Id da Sessão
     * @throws InvalidLoginException Login incorreto
     */
    public String login(String name,String pass) throws InvalidLoginException{
        try {
            lock.writeLock().lock();
            if (this.users.containsKey(name) && this.users.get(name).checkPassword(pass)) {
                String idSession = generateIdSession(this.users.get(name).isAdmin());
                Session session = new Session(name);
                this.sessions.put(idSession, session);
                return idSession;
            }
            throw new InvalidLoginException();
        }finally {lock.writeLock().unlock();}
    }

    /**
     * Método de Logout. Apaga a sessão
     * @param idSession Id da sessão
     */
    public void logout(String idSession){
        try {
            lock.writeLock().lock();
            if (this.sessions.containsKey(idSession))
                this.sessions.remove(idSession);
        }finally { lock.writeLock().unlock();}
    }

    /**
     * Método auxiliar para gerar Id da Sessão.
     * Gera um ID com 6 caracteres no formato "Uxxxxx" ou "Axxxxx" (caso seja admin), e verifica se este já existe.
     * @param admin Gerar ID de admin
     * @return Id de sessão gerado
     */
    private String generateIdSession(Boolean admin){
        Random rnd = new Random();
        Boolean done = false;
        String idSession = "Nxxxxx";

        while (!done){
            idSession = "";

            if (admin) idSession += "A";
            else idSession += "U";

            for(int i = 0;i < 5;i++){
                /** BETWEEN CHAR a - z */
                int num = rnd.nextInt(26) + 97;
                char chr = (char) num;
                idSession += chr;
            }

            if(!this.sessions.containsKey(idSession)) done = true;
        }

        return idSession;
    }


    /**
     * Método auxiliar para leitura do ficheiro que contém a informação de todos os utilizadores.
     * Este ficheiro tem de conter os users no seguinte formato:
     * Nome;Password;Admin
     * (Admin - 'true' ou 'false')
     * @param path Caminho do ficheiro
     * @throws IOException Erro de IO
     */
    private void readUsers(String path) throws IOException{
        this.users = new HashMap<>();
        Files.lines(Paths.get(path)).forEach(s-> {
            String[] fields = s.split(";");

            Boolean admin = false;
            if(fields[2].equals("true")) admin = true;

            User usr = new User(fields[0],fields[1],admin);

            this.users.put(fields[0],usr);
        });
    }

}
