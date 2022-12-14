/**
 * @Author Vicente Moreira
 * @Date 15/05/2021
 *
 * Classe Exception.
 * Caso ao ler os ficheiro dos businesses aconteça algum erro.
 */

public class BusinessIOException extends Exception{
    /**
     * Construtor por omissão
     */
    public BusinessIOException(){
        super();
    }

    /**
     * Construtor com mensagem
     * @param msg mensagem
     */
    public BusinessIOException(String msg){
        super(msg);
    }
}
