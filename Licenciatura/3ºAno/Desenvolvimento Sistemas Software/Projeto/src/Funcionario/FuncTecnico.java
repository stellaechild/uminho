public class FuncTecnico extends Funcionario {

    private boolean livre=false;

    FuncTecnico(boolean l){
        this.livre=l;
    }

    /**
     * Metodo que determina se um funcionario esta livre ou nao.
     * @return livre, valor boleano
     */
    public boolean isLivre(){
        return livre;
    }
}
