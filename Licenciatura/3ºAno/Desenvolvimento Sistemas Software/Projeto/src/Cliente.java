public class Cliente {
    private String nifCliente="";
    private String email="";
    private String telemovel="";

    /**
     * Construtor do Cliente
     * @param nif número de identificacao fiscal
     * @param e email
     * @param t telemovel
     */
    public Cliente(String nif, String e, String t){
        this.nifCliente=nif;
        this.email=e;
        this.telemovel=t;
    }

    /**
     * Metodo que que altera o contacto de um cliente.
     * Se o tipo deste for um email, entao altera-se este parametro.
     * Caso contrario, altera-se o parametro do telemovel, ( o outro unico caso alternativo ).
     * @param tipoContatoTelemovel boolean que retorna verdade se for um telemovel.
     * @param contato o contacto para o qual se vai mudar.
     */
    public void alteraContacto(boolean tipoContatoTelemovel, String contato){
        if (tipoContatoTelemovel) this.telemovel=contato;
        else this.email=contato;
    }

    public String getTelemovel(){return this.telemovel;}
    public String getNifCliente(){return this.nifCliente;}
    public String getEmail(){return this.email;}
}
