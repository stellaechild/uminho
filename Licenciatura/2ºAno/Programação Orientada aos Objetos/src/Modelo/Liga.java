import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe Liga
 *
 * @author Vicente Moreira
 * @author Maria Cunha
 *
 * @version 20210511
 */

public class Liga implements Serializable {

    private String nome;
    private int jogosDecorridos;
    private LocalDate data_atual;
    private Map<String,Equipa> equipas;
    private List<Jogo> jogos;
    private List<Classificacao> classificacao;

    /**
     * Contrutor por omissão
     */
    public Liga(){
        this.nome = "n/a - n/a";
        this.jogosDecorridos = 0;
        this.data_atual = LocalDate.now();
        this.equipas = new HashMap<>();
        this.jogos = new ArrayList<>();
        this.classificacao = new ArrayList<>();
    }

    /**
     * Contrutor parametrizado
     */
    public Liga(String name){
        this.nome = name;
        this.jogosDecorridos = 0;
        this.data_atual = LocalDate.now();
        this.equipas = new HashMap<>();
        this.jogos = new ArrayList<>();
        this.classificacao = new ArrayList<>();
    }

    /**
     * Contrutor por cópia
     */
    public Liga(Liga outro){
        this.nome = outro.nome;
        this.jogosDecorridos = outro.jogosDecorridos;
        this.data_atual = outro.data_atual;
        this.equipas = outro.equipas.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(),e->e.getValue().clone()));
        this.jogos = outro.jogos.stream().map(Jogo :: clone).collect(Collectors.toList());
        this.classificacao = outro.classificacao.stream().map(Classificacao :: clone).collect(Collectors.toList());
    }

    /**
     * Métodos SET
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setJogosDecorridos(int jogosDecorridos) {
        this.jogosDecorridos = jogosDecorridos;
    }
    public void setData_atual(LocalDate data_atual) {this.data_atual = data_atual;}
    public void setEquipas(HashMap<String, Equipa> equipas) {
        this.equipas = equipas.values().stream().map(Equipa :: clone).
                collect(Collectors.toMap(e -> e.getNome(),e->e));
    }
    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos.stream().map(Jogo :: clone).collect(Collectors.toList());;
    }
    public void setClassificacao(ArrayList<Classificacao> classificacao) {
        this.classificacao = classificacao.stream().map(Classificacao :: clone).collect(Collectors.toList());
    }
    public void incJogosDecorridos(){
        this.jogosDecorridos++;
    }

    /**
     * Métodos GET
     */
    public String getNome() {
        return nome;
    }
    public int getJogosDecorridos() {
        return jogosDecorridos;
    }
    public LocalDate getData_atual() {return data_atual;}

    public Equipa getEquipa(String nome) throws EquipaNaoExistenteException{
        if(this.equipas.containsKey(nome)){
            return this.equipas.get(nome); //Agregação
        }
        else throw new EquipaNaoExistenteException("Equipa não existe");
    }

    public int getNumJogos(){
        return this.jogos.size();
    }
    public int getNumEquipas(){
        return this.equipas.size();
    }

    /**
     * Método de sort aos Jogos
     */
    public void sortJogos(){
        this.jogos.sort(Comparator.comparing(Jogo::getData));
    }

    /**
     * Método que adiciona um Equipa
     */
    public void addEquipa(Equipa equipa) throws EquipaJaExistenteException{
        if (this.equipas.containsKey(equipa.getNome())){
            throw new EquipaJaExistenteException("Equipa já existe");
        }
        this.equipas.put(equipa.getNome(),equipa);
        this.classificacao.add(new Classificacao(equipa.getNome()));
    }

    /**
     * Método que remove uma Equipa
     */
    public void removeEquipa(String nome) throws EquipaNaoExistenteException{
        if(!containsEquipa(nome)) throw new EquipaNaoExistenteException("Equipa não existe");
        this.equipas.remove(nome);
        removeJogosDeUmaEquipa(nome);

        Iterator<Classificacao> it = this.classificacao.iterator();
        Boolean done = false;
        while (it.hasNext() && !done) {
            Classificacao c = it.next();
            if(c.getEquipa().equals(nome)) {
                it.remove();
                done = true;
            }
        }
    }

    /**
     * Método quea adiciona um Jogo
     */
    public void addJogo(Jogo jogo)throws JogoJaExistenteException{
        if(this.jogos.contains(jogo)){
            throw new JogoJaExistenteException("Jogador já Existe");
        }
        this.jogos.add(jogo);
        sortJogos();  //(j1,j2) -> j1.getData().compareTo(j2.getData())
    }

    /**
     * Método que remove um Jogo
     */
    public void removeJogo(int idx)throws JogoNaoExistenteException{
        if(!this.jogos.contains(idx)){
            throw new JogoNaoExistenteException("Valor incorreto");
        }
        this.jogos.remove(idx);
    }

    /**
     * Método que remove todos os jogos associados a uma Equipa
     */
    public void removeJogosDeUmaEquipa(String nome){
        Iterator<Jogo> it = this.jogos.iterator();
        while (it.hasNext()){
            Jogo jog = it.next();
            if(jog.getEq_Visitante().equals(nome) || jog.getEq_Visitado().equals(nome))
                it.remove();
        }
    }

    /**
     * Prepara Jogo. Vê as equipas em questão e "carrega" a lista de jogadores
     */
    public void preparaJogo(){
        String eq_visitado = this.jogos.get(this.jogosDecorridos).getEq_Visitado();
        String eq_visitante = this.jogos.get(this.jogosDecorridos).getEq_Visitante();
        this.jogos.get(this.jogosDecorridos).setAllPlantel(getEquipaTitulares(eq_visitado),getEquipaSubstitutos(eq_visitado),
                                             getEquipaTitulares(eq_visitante),getEquipaSubstitutos(eq_visitante));
        this.jogos.get(this.jogosDecorridos).setJogoPronto();
    }

    /**
     * Contains Equipa.
     * @return true, se a equipa existir.
     */
    public Boolean containsEquipa(String equipa){
        return this.equipas.containsKey(equipa);
    }

    /**
     * Método Get Titulares de uma equipa
     */
    public Map<Integer,Jogador> getEquipaTitulares(String equipa){
        return this.equipas.get(equipa).getTitulares();
    }

    /**
     * Método Get Substitutos de uma equipa
     */
    public Map<Integer,Jogador> getEquipaSubstitutos(String equipa){
        return this.equipas.get(equipa).getSuplentes();
    }

    /**
     * Método ToString da tabela de classificação
     */
    public String classificacaoToString(){
        Comparator<Classificacao> ordemDecPontos = (v1,v2) -> v1.getPontos() == v2.getPontos() ?
                                                    v2.getPontos() - v1.getPontos() :
                                                    v2.getGolosmarcados() - v1.getGolosmarcados();
        this.classificacao.sort(ordemDecPontos);
        final StringBuilder sb = new StringBuilder("");
        sb.append("Equipa - Pontos/Golos\n");
        for(Classificacao cl : this.classificacao){
            sb.append(cl.toString());
        }
        return sb.toString();
    }


    //------------------------------------------ToStrings----------------------------------------------

    /**
     * toString Liga.
     * @return String formatada.
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder("Liga: ");
        sb.append(nome).append('\n').append(EquipasToString());
        return sb.toString();
    }

    /**
     * Lista as Equipas na Liga.
     * @return String formatada.
     */
    public String EquipasToString(){
        final StringBuilder sb = new StringBuilder("Equipas:\n");
        for(Equipa equipa : this.equipas.values()){
            sb.append("   ").append(equipa.getNome()).append('\n');
        }
        return sb.toString();
    }

    /**
     * Lista os Jogos na Liga
     * @return String formatada
     */
    public String JogosToString(){
        final StringBuilder sb = new StringBuilder("JOGOS:").append('\n');
        int i= 1;
        for(Jogo jogo : this.jogos){
            sb.append(i+" - ").append(jogo.toString());
            i++;
        }
        return sb.toString();
    }


    //-----------------------------------------------------------------FROMLINE---------------------------------------------------------------

    /**
     * Equipa fromLine
     * Nome da Equipa. Nome do Estádio. Local. Capacidade do Estádio
     */
    public void addEquipaFromLine(String line) throws NumeroDeArgumentosInvalidosException,ArgumentosInvalidosException,EquipaJaExistenteException{
        String[] fields = line.split(";");
        int capacidade = -1;
        if(Arrays.stream(fields).count() != 4) throw new NumeroDeArgumentosInvalidosException("Número de campos incorretos");
        try{
            capacidade = Integer.valueOf(fields[3]);
        }
        catch (NumberFormatException e){
            throw new ArgumentosInvalidosException("Introduza um valor na capacidade (>=0)");
        }
        if(capacidade < 0) throw new ArgumentosInvalidosException("Capacidade do estádio inválida! Use um valor maior que 0");
        Equipa nova = new Equipa(fields[0]);
        nova.setEstadio(new Estadio(fields[1],fields[2],capacidade));
        addEquipa(nova);
    }

    /**
     * Jogo fromLine
     * Nome da Equipa visitada - vistante e data.
     */
    public void addJogoFromLine(String line) throws NumeroDeArgumentosInvalidosException,ArgumentosInvalidosException,JogoJaExistenteException,EquipaNaoExistenteException{
        String[] fields = line.split(";");
        if(Arrays.stream(fields).count() != 7) throw new NumeroDeArgumentosInvalidosException("Número de campos incorretos");
        if(!containsEquipa(fields[0]) || !containsEquipa(fields[1]))
            throw new EquipaNaoExistenteException("Uma ou mais Equipas não existem");
        if(fields[0].equals(fields[1]))
            throw new ArgumentosInvalidosException("Equipa repetida!");
        if(!this.equipas.get(fields[0]).isValidTeam() || !this.equipas.get(fields[1]).isValidTeam())
            throw new ArgumentosInvalidosException("Uma ou mais das Equipas são Equipas incompletas. Estas precisam de pelo menos 11 Jogadores");

        int[] args = new int[5];
        Jogo novo;
        try{
            for(int aux = 0;aux < 5;aux++) args[aux] = Integer.valueOf(fields[aux+2]);
            novo = new Jogo(fields[0],fields[1], LocalDateTime.of(args[0],args[1],args[2],args[3],args[4]));
        }
        catch (NumberFormatException|DateTimeException e){
            throw new ArgumentosInvalidosException("Introduza números corretos");
        }
        addJogo(novo);
    }
}
