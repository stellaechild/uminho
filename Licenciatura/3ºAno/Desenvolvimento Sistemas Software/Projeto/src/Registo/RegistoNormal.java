import java.util.List;

public class RegistoNormal extends Registo{

    private PlanoDeTrabalho plano;

    /**
     * Construtor de um Registo Normal
     * @param idReg
     * @param idFuncBalcao
     * @param nif
     */
    RegistoNormal(String idReg,String idFuncBalcao,String nif){
        super(idReg,idFuncBalcao,nif);
    }


    public PlanoDeTrabalho getPlano(){
        return this.plano;
    }

    /**
     * Metodo que regista os passos de cada reparação.
     * @param passo
     */
    public void registarPassos (List<Passo> passo) {
        this.plano.registarPassos(passo);
    }
}
