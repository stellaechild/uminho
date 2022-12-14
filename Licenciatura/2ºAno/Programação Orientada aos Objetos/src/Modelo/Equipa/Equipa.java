import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Classe Equipa
 *
 * @author Joana Maia
 * @author Maria Cunha
 * @version 20210323
 */

public class Equipa implements Serializable {

  private String nome;
  private Estadio estadio;
  private int habilidade;
  private Map<Integer,Jogador> jogadores;
  private Map<String,Treinador> treinadores;

  /**
   * Construtor por omissão
   * Assume uma Equipa com o número mínimo de jogadores e um treinador
   */
  public Equipa() {
    this.nome = "n/a";
    this.habilidade = 0;
    this.estadio = new Estadio();
    this.jogadores = new HashMap<>();
    this.treinadores = new HashMap<>();
  }

  /**
   * Construtor parametrizado
   * Constroi uma Equipa dado o nome desta, e o número de jogadores e treinadores
   */
  public Equipa (String nome) {
    this.nome = nome;
    this.habilidade = 0;
    this.estadio = new Estadio();
    this.jogadores = new HashMap<>();
    this.treinadores = new HashMap<>();
  }

  /**
   * Construtor por cópia
   * Constroi uma Equipa dado uma cópia, assume-se que a equipa fornecida é previamente clonada
   */
  public Equipa (Equipa outraE) {
    this.nome = outraE.getNome();
    this.habilidade = outraE.habilidade;
    this.estadio = new Estadio(outraE.getEstadio());
    this.jogadores = new HashMap<>(outraE.getJogadores());
    this.treinadores = new HashMap<>(outraE.getTreinadores());
  }

  /**
   * Métodos SET
   */
  void setNome (String nome) {this.nome = nome;}
  public void setEstadio(Estadio estadio) {
    this.estadio = estadio.clone();
  }
  public void setJogadores(Map<Integer, Jogador> jogadores) {
    this.jogadores = jogadores.values().stream().map(Jogador :: clone).collect(Collectors.toMap(e->e.getNumero(), e -> e));
  }
  public void setTreinadores(Map<String, Treinador> treinadores) {
    this.treinadores = treinadores.values().stream().map(Treinador :: clone).collect(Collectors.toMap(e->e.getNome(), e -> e));;
  }
  public void calculaHabilidade(){
    if(this.getNumJogadores() == 0) this.habilidade = 0;
    else {
      this.habilidade = this.jogadores.values().stream().mapToInt(Jogador::calculaHabilidade).sum();
      this.habilidade = this.habilidade / this.jogadores.size();
    }
  }

  /**
   * Métodos GET
   */
  public String getNome() {
    return this.nome;
  }
  public Estadio getEstadio() {
    return this.estadio;
  }
  public Map getJogadores() {
    return this.jogadores; //Agregacao
  }
  public Map getTreinadores() {
    return this.treinadores; //Agregacao
  }
  public int getHabilidade() {return habilidade;}
  public int getNumJogadores(){return this.jogadores.size();}
  public int getNumTreinadores(){return this.treinadores.size();}


  //-----------------------------------VALIDADE DA EQUIPA--------------------------------

  /**
   * Valid team. >=11jogadores
   */
  public Boolean isValidTeam(){
    if(getNumJogadores()>=11) return true;
    else return false;
  }

  /**
   * Valid team. >=11 jogadores titulares
   */
  public Boolean isValidToGame(){
    int titulares = 0;
    for(Jogador jog: this.jogadores.values()){
      if(jog.isTitular()) titulares++;
    }
    if(titulares>=11) return true;
    else return false;
  }

  /**
   * Força a equipa a ter 11titulares
   */
  public void forceValidToGame(){
    int titulares = 0;
    for(Jogador jog: this.jogadores.values()){
      if(!jog.isTitular() && titulares < 11) {
        jog.setEstado(Jogador.TITULAR);
      }
      titulares++;
    }
  }

  //---------------------------------------------JOGADOR------------------------------------------------------

  /**
   * Contains Jogador
   */
  public Boolean containsJogador(int num){
    return this.jogadores.containsKey(num);
  }

  /**
   * Método AddJogador
   */
  public void addJogador(Jogador jogador, LocalDate data) throws JogadorJaExistenteException {
    if(this.jogadores.containsKey(jogador.getNumero())){
      throw new JogadorJaExistenteException("Jogador "+jogador.getNumero()+" já existe");
    }
    jogador.registaEntrada(this.nome,data);
    this.jogadores.put(jogador.getNumero(),jogador);
  }

  /**
   * Jogador fromLine
   */
  public void addjogadorFromLine(String line,LocalDate data) throws NumeroDeArgumentosInvalidosException, ArgumentosInvalidosException,JogadorJaExistenteException{
    String[] fields = line.split(";");
    int[] nums = new int[11];
    Jogador novo;
    if(fields.length != 13) throw new NumeroDeArgumentosInvalidosException("Número de campos incorretos");
    try{
      for(int aux = 0;aux < 11;aux++) nums[aux] = Integer.valueOf(fields[aux+2]);
    }
    catch (NumberFormatException e){
      throw new ArgumentosInvalidosException("Introduza valores corretos!");
    }
    Boolean valido = true;
    for(int aux = 1;valido && aux<11;aux++) if(nums[aux] <0 || nums[aux] > 100) valido = false;
    if(!valido || (nums[0] < 1 || nums[0] > 99)) throw new ArgumentosInvalidosException("Um ou mais valores inválido!\nNúmeros de jogadores (1-99)\nHabilidades (0-100)");

    if(fields[1].equals("avancado")) novo = new Avancado(fields[0],nums[0],nums[1],nums[2],nums[3],nums[4],nums[5],nums[6],nums[7],nums[8],nums[9],nums[10]);
    else if(fields[1].equals("defesa")) novo = new Defesa(fields[0],nums[0],nums[1],nums[2],nums[3],nums[4],nums[5],nums[6],nums[7],nums[8],nums[9],nums[10]);
    else if(fields[1].equals("guardaredes")) novo = new GuardaRedes(fields[0],nums[0],nums[1],nums[2],nums[3],nums[4],nums[5],nums[6],nums[7],nums[8],nums[9],nums[10]);
    else if(fields[1].equals("lateral")) novo = new Lateral(fields[0],nums[0],nums[1],nums[2],nums[3],nums[4],nums[5],nums[6],nums[7],nums[8],nums[9],nums[10]);
    else if(fields[1].equals("medio")) novo = new Medio(fields[0],nums[0],nums[1],nums[2],nums[3],nums[4],nums[5],nums[6],nums[7],nums[8],nums[9],nums[10]);
    else throw new ArgumentosInvalidosException("Posição inválida");

    this.addJogador(novo,data);
  }

  /**
   * Remove Jogador
   */
  public void removeJogador(int num)throws JogadorNaoExistenteException{
    if(!this.jogadores.containsKey(num)) throw new JogadorNaoExistenteException("O jogador "+num+" não existe");
    this.jogadores.remove(num);
  }

  /**
   * Retira Jogador
   * Este devolve o Jogador retirado com o seu historico de saida registado
   */
  public Jogador retiraJogador(int num,LocalDate data) throws JogadorNaoExistenteException{
    if(!this.jogadores.containsKey(num)) throw new JogadorNaoExistenteException("O jogador "+num+" não existe");
    this.jogadores.get(num).registaSaida(data);
    Jogador clone = this.jogadores.get(num).clone();
    removeJogador(num);
    return clone;
  }

  //------------------------------------------------TREINADOR--------------------------------------------------------

  /**
   * Contains Treinador
   */
  public Boolean containsTreinador(String nome){
    return this.treinadores.containsKey(nome);
  }

  /**
   * Método AddTreinador
   */
  public void addTreinador(Treinador treinador) throws TreinadorJaExistenteException {
    if(this.treinadores.containsKey(treinador.getNome())){
      throw new TreinadorJaExistenteException("Treinador "+treinador.getNome()+" já existe");
    }
    this.treinadores.put(treinador.getNome(),treinador);
  }

  /**
   * Add Treinador fromLine
   */
  public void addTreinadorFromLine(String line)throws NumeroDeArgumentosInvalidosException, ArgumentosInvalidosException,TreinadorJaExistenteException{
    String[] fields = line.split(";");
    if(fields.length != 2) throw new NumeroDeArgumentosInvalidosException("Número de campos incorretos");
    int qualidade;
    try{
       qualidade = Integer.valueOf(fields[1]);
    }
    catch (NumberFormatException e){
      throw new ArgumentosInvalidosException("Introduza valor da qualidade corretamente");
    }
    if(qualidade < 0 || qualidade > 100) throw new ArgumentosInvalidosException("Valor de qualidade inválido. Escolha entre 0 e 100");
    Treinador novo = new Treinador(fields[0],qualidade,Treinador.SUPLENTE);
    addTreinador(novo);
  }

  /**
   * Remove Treinador
   */
  public void removeTreinador(String nome)throws TreinadorNaoExistenteException{
    if(!this.treinadores.containsKey(nome)) throw new TreinadorNaoExistenteException("O treinador "+nome+" não existe");
    this.treinadores.remove(nome);
  }

  /**
   * Retira Treinador
   * Este devolve o Treinador retirado.
   */
  public Treinador retiraTreinador(String nome) throws TreinadorNaoExistenteException{
    if(!this.treinadores.containsKey(nome)) throw new TreinadorNaoExistenteException("O treinador "+nome+" não existe");
    Treinador clone = this.treinadores.get(nome).clone();
    removeTreinador(nome);
    return clone;
  }


  //-------------------------------------------------TO STRINGS---------------------------------------------------------

  /**
   * Método toString
   */
  public String toString() {
    this.calculaHabilidade();
    final StringBuilder sb = new StringBuilder("Equipa: ");
    sb.append(nome).append('\n');
    sb.append("Estádio ").append(this.estadio.getNome()).append(" - ").append(this.estadio.getLocal()).append('\n');
    sb.append(jogadores.size()).append(" Jogadores | ");
    sb.append(treinadores.size()).append(" Treinadores | Habilidade: ").append(getHabilidade()).append('\n');
    return sb.toString();
  }

  /**
   * Método ToStringJogadores
   */
  public String JogadoresToString(){
    final StringBuilder sb = new StringBuilder("Jogadores: \n");
    for(Jogador jog : this.jogadores.values()){
      sb.append(jog.toString()).append('\n');
    }
    return sb.toString();
  }

  /**
   * Método ToStringTreinadores
   */
  public String TreinadoresToString(){
    final StringBuilder sb = new StringBuilder("Treinadores: \n");
    for(Treinador tre : this.treinadores.values()){
      sb.append(tre.toString());
    }
    return sb.toString();
  }


  //----------------------------------------------------------OUTROS----------------------------------------------------

  /**
   * Método Equals
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o.getClass() != Equipa.class || o == null) return false;
    Equipa equipa = (Equipa) o;
    return Objects.equals(getNome(), equipa.getNome()) &&
            Objects.equals(getEstadio(), equipa.getEstadio());
  }

  /**
   * Método Clone
   */
  public Equipa clone(){
    return new Equipa(this);
  }


  /**
   * Método para recolher os Jogadores titulares
   */
  public Map<Integer,Jogador> getTitulares(){
    return this.jogadores.values().stream().filter(Jogador::isTitular).map(Jogador::clone).collect(Collectors.toMap(e->e.getNumero(),e->e));
  }

  /**
   * Método para recolher os Jogadores titulares
   */
  public Map<Integer,Jogador> getSuplentes(){
    return this.jogadores.values().stream().filter(Jogador::isSuplente).map(Jogador::clone).collect(Collectors.toMap(e->e.getNumero(),e->e));
  }
}
