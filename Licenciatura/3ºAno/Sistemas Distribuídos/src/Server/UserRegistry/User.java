public class User {

    /** Nome do Utilizador */
    private String name;
    /** Password do Utilizador */
    private String pass;
    /** Indicador de privilégios de administrador */
    private Boolean admin;

    /**
     * Construtor simples
     * @param name Nome do Utilizador
     * @param pass Password do Utilizador
     * @param admin Indicador de privilégios de administrador
     */
    public User(String name,String pass,Boolean admin){
        this.name = name;
        this.pass = pass;
        this.admin = admin;
    }

    /**
     * Método para verificação da password
     * @param pass Password a testar
     * @return boolean - true se correta
     */
    public Boolean checkPassword(String pass){
        return this.pass.equals(pass);
    }

    /**
     * Método de verificação de privilégios de administrador
     * @return boolean - true se é admin
     */
    public Boolean isAdmin(){
        return admin;
    }
}
