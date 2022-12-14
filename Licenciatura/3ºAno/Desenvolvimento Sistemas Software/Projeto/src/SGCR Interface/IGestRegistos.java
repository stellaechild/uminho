import java.util.*;

public interface IGestRegistos {
    public void criaRegistoNormal(String nif,String email);
    public void criaRegistoExpresso(String nif, String telemovel, String tipo, String idTecnico);
    public void atualizaEstado(String idRegisto, String estado) throws RegistoNaoExistenteException;
    public List<String> getRegistosPorEstado(String estado);
    public void ordenaRegistosPorChegada(List<String> lista) throws NotRegistoNormalException;
    public void ordenaRegistosPorPrazo(List<String> lista);
    public boolean avaliaTempoDecorridoOrcamento(String idRegisto) throws RegistoNaoExistenteException, NotRegistoNormalException;
    public boolean avaliaTempoDecorridoReparado(String idRegisto) throws RegistoNaoExistenteException;
    public void registarPlano(String idRegisto,List<Passo> passos) throws RegistoNaoExistenteException, NotRegistoNormalException;
    public PlanoDeTrabalho getPlano(String idRegisto) throws RegistoNaoExistenteException, NotRegistoNormalException;
    public void avancarPasso(String idRegisto,int passo, float tempoReal, float custoReal) throws RegistoNaoExistenteException;
    public boolean avaliarCustos(String idRegisto);
    public boolean checkPassos(String idRegisto) throws RegistoNaoExistenteException, NotRegistoNormalException;
    public void registaConclusao(String idRegisto) throws RegistoNaoExistenteException;
    public void suspendePlano(String idRegisto) throws RegistoNaoExistenteException;
}
