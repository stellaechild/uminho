import java.util.*;

public interface IGestFuncionarios {
    public String usaTecnicoExpresso();
    public void autentica(String id,String pass) throws PasswordInvalidaException,FuncionarioInexistenteException;
    public List<String> getEmpregadosBalcao();
    public List<String> getTecnicosStatsSimples();
    public List<String> getTecnicosStatsExaustivas();
    public boolean verificaDisponibilidadeExpresso();
}
