public class Funcionario {

    private String idFunc="";
    private String password="";
    private String nome="";
    private Boolean sessaoIniciada = false;

    public Funcionario(){}

    /**
     * Construtor da classe Funcionario.
     * @param id
     * @param pass
     * @param name
     */
    public Funcionario(String id, String pass, String name){
        this.idFunc=id;
        this.password=pass;
        this.nome=name;
    }

    /**
     * Iniciar a sessao de um funcionario.
     * @param sessaoIniciada
     */
    public void setSessaoIniciada(Boolean sessaoIniciada) {
        this.sessaoIniciada = sessaoIniciada;
    }

    public String getIdFunc() {return this.idFunc;}
    public String getNome(){return this.nome;}
    public String getPassword(){return this.password;}
    public Boolean getSessaoIniciada() {
        return sessaoIniciada;
    }
}
