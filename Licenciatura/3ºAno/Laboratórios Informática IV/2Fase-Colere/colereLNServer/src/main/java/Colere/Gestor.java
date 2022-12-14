package Colere;

/**
 * Classe responsável pela representação de um Gestor de um certo Local.
 * Contém o email do Gestor, a sua password, assim como a chave do seu Local associado.
 */
public class Gestor {

    /** Email do Gestor */
    private String email;
    /** Password do Gestor */
    private String password;
    /** Chave do Local associado ao Gestor */
    private String local;


    /**
     * Construtor da Classe Gestor.
     * @param email Email do Gestor
     * @param pwd Password do Gestor
     * @param local Chave do Local associado ao Gestor
     */
    public Gestor(String email,String pwd,String local){
        this.email = email;
        this.password = pwd;
        this.local = local;
    }

    /**
     * Método que compara uma password com a password associada ao Gestor,
     * retornando true se coincidirem e false em caso contrário.
     * @param pwd Password de comparação.
     * @return Coincidem ou não.
     */
    public boolean comparaPwd(String pwd){
        return password.equals(pwd);
    }

    /**
     * Método GET da chave do Local associado ao Gestor.
     * @return Chave do local associado ao Gestor.
     */
    public String getLocal(){
        return this.local;
    }

}

