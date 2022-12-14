/**
 * @Author Vicente Moreira
 * @Date 15/05/2021
 *
 * Classe Exception.
 * Caso ao ler os ficheiro das reviews aconteça algum erro.
 */

public class ReviewIOException extends Exception{
    /**
     * Construtor por omissão
     */
    public ReviewIOException(){
        super();
    }
    /**
     * Construtor com mensagem
     * @param msg mensagem
     */
    public ReviewIOException(String msg){
        super(msg);
    }
}
