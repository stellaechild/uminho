public class RegistoExpresso extends Registo {

    private String idTecAutor = "";
    private String telemovel;
    private String tipo;
    private boolean terminado = false;

    /**
     * Construtor de um Registo Expresso
     * @param idReg
     * @param idFuncBalcao
     * @param tel
     * @param nif
     * @param tipo
     * @param idTec
     */
    RegistoExpresso(String idReg,String idFuncBalcao,String tel,String nif, String tipo, String idTec){
        super(idReg,idFuncBalcao,nif);
        this.telemovel = tel;
        this.tipo = tipo;
        this.idTecAutor= idTec;
    }

    /**
     * Metodo que devolve o valor da variavel terminado.
     * @return
     */
    public boolean getTerminado(){return this.terminado;}

    /**
     * Metodo que devolve o autor tecnico da reparacao expresso.
     * @return
     */
    public String getidTecAutor(){return this.idTecAutor;}

    /**
     * Metodo que define que a reparacao foi concluida.
     */
    public void isTerminado(){this.terminado = true;}

}
