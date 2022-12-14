public class Equipamento {
    private String idEquipamento="";
    private String nifCliente="";
    private boolean reparado=false;
    private String idRegisto="";
    private boolean arquivado=false;

    /**
     * Construtor do Equipamento
     * @param id Id deste
     * @param nif Número de Identificacao Fiscal do Cliente a que este equipamento pertence
     * @param rep Se se encontra reparado
     * @param reg O id de registo deste equipamento
     * @param arq Se se encontra arquivado
     */
    public Equipamento(String id, String nif, boolean rep, String reg, boolean arq){
        this.idEquipamento=id;
        this.nifCliente=nif;
        this.reparado=rep;
        this.idRegisto=reg;
        this.arquivado=arq;
    }

    public String getIdEquipamento(){return this.idEquipamento;}
    public String getNifCliente(){return this.nifCliente;}
    public String getIdRegisto(){return this.idRegisto;}
    public boolean getReparado(){return this.reparado;}
    public boolean getArquivado(){return this.arquivado;}

    /**
     * Metodo que arquiva o equipamento.
     */
    public void arquivado(){
        this.arquivado=true;
    }

    /**
     * Metodo que muda o estado do equipamento para reparado.
     */
    public void reparado(){
        this.reparado=true;
    }
}
