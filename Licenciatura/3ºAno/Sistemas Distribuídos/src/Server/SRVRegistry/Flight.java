public class Flight {

    /** Chave identificadora do Voo */
    private String key;
    /** Origem do Voo */
    private String origem;
    /** Destino do Voo */
    private String destino;
    /** Capacidade do Voo */
    private int capacidade;
    /** Hora do Voo */
    private String hora;

    /**
     * Construtor simples de um Voo
     * @param key Chave identificadora do Voo
     * @param origem Origem do Voo
     * @param destino Destino do Voo
     * @param hora Hora do Voo
     * @param capacidade Capacidade do Voo
     */
    public Flight(String key, String origem,String destino,String hora,int capacidade){
        this.key = key;
        this.origem = origem;
        this.destino = destino;
        this.hora = hora;
        this.capacidade = capacidade;
    }

    /*--------------Métodos GET------------*/
    public String getKey()     {return key;}
    public String getOrigem()  {return origem;}
    public String getDestino() {return destino;}
    public String getHora()    {return hora;}
    public int getCapacidade() {return capacidade;}

    /**
     * Método to String. Utilizado para listar a informação de um Voo
     * @return String formatada.
     */
    public String toString() {
        final StringBuffer sb = new StringBuffer("Voo "+this.key.substring(5)+": ");
        sb.append(origem).append(" -> ").append(destino);
        sb.append(" | Hora: ").append(hora).append(" | Capacidade: ").append(capacidade);
        return sb.toString();
    }
}
