
public interface INotificacao {
    public void notificaTecnicos(String msg);
    public void notificaBalcao(String msg);
    public String getEmailCliente(String idRegisto) throws ContactoInexistenteException,RegistoNaoExistenteException,ClienteInexistenteException;
    public String getTeleCliente(String idRegisto) throws ContactoInexistenteException,RegistoNaoExistenteException,ClienteInexistenteException;
    public void registaNotifCliente(String registoID, String conteudo)throws RegistoNaoExistenteException;
}
