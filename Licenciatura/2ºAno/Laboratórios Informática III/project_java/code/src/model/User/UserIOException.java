/**
 * @Author Vicente Moreira
 * @Date 15/05/2021
 *
 * Classe Exception.
 * Caso ao ler os ficheiro dos users aconteça algum erro.
 */

public class UserIOException extends Exception{
    /**
     * Construtor por omissão
     */
    public UserIOException(){
        super();
    }

    /**
     * Construtor com mensagem
     * @param msg mensagem
     */
    public UserIOException(String msg){
        super(msg);
    }
}
