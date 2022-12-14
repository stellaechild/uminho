import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Classe Jogador
 *
 * @author Joana Maia
 * @version 20210323
 */

public abstract class Jogador implements Serializable {

	public static final int TITULAR  = 0;
	public static final int SUPLENTE = 1;
	public static final int LESIONADO = 2;
	public static final int AUSENTE = 3;

	public static final int INSEGURO = -2;
	public static final int DESMOTIVADO = -1;
	public static final int NORMAL = 0;
	public static final int MOTIVADO = 1;
	public static final int CONFIANTE = 2;

	private String nome;
	private int numero;
	private int estado;
	private int motivacao;
	private Map<String,Integer> caracteristicas;
	private List<Historico> historicoList;
	private int habilidade;

	/**
	 * Construtor por omissão
	 * Cria um Jogador Default
	 */
	public Jogador () {
		this.numero = 0;
		this.nome = "n/a";
		this.estado = AUSENTE;
		this.initCaracteristicas();
		this.historicoList = new ArrayList<>();
		this.habilidade = 0;
	}

	/**
	 * Construtor parametrizado que recebe um HashMap de caracteristicas.
	 * Constroi um Jogador com o seu nome, numero e lista de caracteristicas.
	 */
	public Jogador (String nome, int numero,HashMap<String,Integer> caracteristicas) {
		this.nome = nome;
		this.numero = numero;
		this.estado = AUSENTE;
		this.motivacao = NORMAL;
		this.caracteristicas = new HashMap<>(7);
		this.caracteristicas = caracteristicas.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(),e->e.getValue()));
		this.historicoList = new ArrayList<>();
		this.habilidade = calculaHabilidade();
	}

	/**
	 * Construtor parametrizado
	 * Constroi um Jogador com o seu nome, numero e lista de caracteristicas.
	 */
	public Jogador (String nome, int numero,int velocidade,int resistencia,int destreza,
					int impulsao,int jogDeCabeca,int remate,int capacidadeDePasse) {
		this.nome = nome;
		this.numero = numero;
		this.estado = AUSENTE;
		this.motivacao = NORMAL;
		this.caracteristicas = new HashMap<>(7);
		this.caracteristicas.put("velocidade",velocidade);
		this.caracteristicas.put("resistencia",resistencia);
		this.caracteristicas.put("destreza",destreza);
		this.caracteristicas.put("impulsao",impulsao);
		this.caracteristicas.put("jogo de cabeca",jogDeCabeca);
		this.caracteristicas.put("remate",remate);
		this.caracteristicas.put("capacidade de passe",capacidadeDePasse);
		this.historicoList = new ArrayList<>();
		this.habilidade = calculaHabilidade();
	}

	/**
	 * Construtor por cópia
	 * Constroi um Jogdor por cópia, assume-se que o jogador fornecido é previamente clonado
	 */
	public Jogador (Jogador outroJog) {
		this.nome = outroJog.getNome();
		this.numero = outroJog.getNumero();
		this.estado = outroJog.getEstado();
		this.motivacao = outroJog.getMotivacao();
		this.habilidade = outroJog.getHabilidade();
		this.historicoList = outroJog.historicoList.stream().map(Historico :: clone).collect(Collectors.toList());
		this.caracteristicas = outroJog.caracteristicas.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(),e->e.getValue()));
	}

	/**
	 * Inicializador do HasMap de características
	 */
	private void initCaracteristicas(){
		this.caracteristicas = new HashMap<>(7);
		this.caracteristicas.put("velocidade",0);
		this.caracteristicas.put("resistencia",0);
		this.caracteristicas.put("destreza",0);
		this.caracteristicas.put("impulsao",0);
		this.caracteristicas.put("jogo de cabeca",0);
		this.caracteristicas.put("remate",0);
		this.caracteristicas.put("capacidade de passe",0);
	}

	/**
	 * Método auxiliar de controlo de valores inválidos de habiliades
	 */
	protected int betweenZeroToHundred(int value){
		if (value<0) return 0;
		else if (value>100) return 100;
		else return value;
	}

	/**
	 * Métodos SET
	 */
	void setNome (String nome) {
		this.nome = nome;
	}
	void setNumero (int num) {
		if(num < 1) num = 1;
		if(num > 99) num = 99;
		this.numero = num;
	}
	void setEstado (int e) {
		this.estado = e;
	}
	void setCaracteristica (String categoria,int value) {
		value = betweenZeroToHundred(value);
		if(this.caracteristicas.containsKey(categoria)) this.caracteristicas.put(categoria,value);
	}
	void setCaracteristicas(HashMap<String,Integer> outro){
		this.caracteristicas = outro.entrySet().stream().
				collect(Collectors.toMap(e -> e.getKey(),e->betweenZeroToHundred(e.getValue())));
	}
	void setCaracteristicas (String key, int value) {
		if (this.caracteristicas.containsKey(key)) this.caracteristicas.put(key, value);
	}
	public void setMotivacao(int motivacao) {
		this.motivacao = motivacao;
	}
	public void setHistoricoList(List<Historico> historicoList) {
		this.historicoList = historicoList.stream().map(Historico::clone).collect(Collectors.toList());
	}

	/**
	 * Métodos GET
	 */
	public String getNome() {
		return this.nome;
	}
	public int getNumero() {
		return this.numero;
	}
	public int getEstado() {
		return this.estado;
	}
	public int getCaracteristica(String key){
		return this.caracteristicas.get(key);
	}
	public Map<String,Integer> getCaracteristicas() {
		return this.caracteristicas;
	}
	public int getHabilidade() {
		return this.habilidade;
	}
	public int getMotivacao() {
		return motivacao;
	}
	public List<Historico> getHistoricoList() {
		return this.getHistoricoList();
	}

	/**
	 * Método equals
	 */
	public boolean equals(Object obj) {
		if(obj==this) return true;
		if(obj==null || obj.getClass() != this.getClass()) return false;
		Jogador le = (Jogador) obj;
		return le.getNome().equals(this.nome) &&
				le.getNumero() == this.numero; //&& NÚMERO E NOME É SUFICIENTE RIGHT???
				//le.getEstado() == this.estado &&
				//le.getHabilidade() == this.habilidade &&
				//le.getCaracteristicas() == this.caracteristicas;
	}

	/**
	 * Identifica os jogadores titulares
	 */
	public Boolean isTitular(){
		if(this.estado == TITULAR) return true;
		else return false;
	}

	/**
	 * Identifica os jogadores suplente
	 */
	public Boolean isSuplente(){
		if(this.estado == SUPLENTE) return true;
		else return false;
	}

	/**
	 * Regista a saida de um clube
	 */
	public void registaSaida(LocalDate data){
		int actual = this.historicoList.size()-1;
		this.historicoList.get(actual).setLeftDate(data);
	}

	/**
	 * Adiciona uma entrada na lista. Regista num clube
	 */
	public void registaEntrada(String equipa, LocalDate data){
		this.historicoList.add(new Historico(data,equipa));
	}


	/**
	 * ToString Estado
	 */
	public String estadoToString(){
		switch (this.estado){
			case 0: return "Titular";
			case 1: return "Suplente";
			case 2: return "Lesionado";
			case 3: return "Ausente";
			default: return "Erro :c";
		}
	}

	/**
	 * ToString Motivacao
	 */
	public String motivacaoToString(){
		switch (this.motivacao){
			case -2: return "Inseguro";
			case -1: return "Desmotivado";
			case 0: return "Normal";
			case 1: return "Motivado";
			case 2: return "Confiante";
			default: return "Erro :c";
		}
	}

	/**
	 * Caracteristicas toString
	 */
	public String caracteristicasToString(){
		final StringBuilder sb = new StringBuilder();
		sb.append(this.caracteristicas.get("velocidade")).append("    ");
		sb.append(this.caracteristicas.get("resistencia")).append("    ");
		sb.append(this.caracteristicas.get("destreza")).append("    ");
		sb.append(this.caracteristicas.get("impulsao")).append("    ");
		sb.append(this.caracteristicas.get("jogo de cabeca")).append("    ");
		sb.append(this.caracteristicas.get("remate")).append("    ");
		sb.append(this.caracteristicas.get("capacidade de passe")).append("    ");
		return sb.toString();
	}

	/**
	 * Método toString
	 */
	public abstract String toString();

	/**
	 * Método clone
	 */
	public abstract Jogador clone();

	/**
	 * Método abstrato de cálculo da capacidade do Jogador.
	 */
	public abstract int calculaHabilidade();
}
