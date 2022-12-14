import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class SGCR implements ISGCR{
    private Map<String,Registo> registos;
    private Map<String,Funcionario> funcionarios;
    private Map<String,Equipamento> equipamentos;
    private Map<String,Cliente> clientes;
    private String idFuncionario;


    public SGCR(){}


// --------------------------------- MÉTODOS DA INTERFACE IGESTFUNCIONARIOS ----------------------------------------

    /**
     * Metodo que verifica se existe algum Funcionario Tecnico disponivel para um
     * serviço Expresso. Se existir, devolve o seu ID.
     * @return ID do Funcionario Tecnico
     */
    public String usaTecnicoExpresso(){
        String func ="n";
        for (Funcionario f : funcionarios.values()) {
            if (f instanceof FuncTecnico && ((FuncTecnico) f).isLivre()) {
                func = f.getIdFunc();
                break;
            }
        }
        return func;
    }

    /**
     * Este metodo trata de autenticar o Funcionario que quiser aceder ao sistema.
     * Se o id deste nao for valido entao lanca uma excecao que informa de que este Funcionario nao existe no sistema.
     * Se o id for valido e a password estiver errada, o sistema lança outra excecao que informa o sucedido.
     * Se ambos forem validos, entao da se a sessao como iniciada.
     * @param id Identificador do Funcionario
     * @param pass Password do Funcionario
     * @throws PasswordInvalidaException
     * @throws FuncionarioInexistenteException
     */
    public void autentica(String id,String pass) throws PasswordInvalidaException,FuncionarioInexistenteException{
        if(this.funcionarios.containsKey(id)){
            if (this.funcionarios.get(id).getPassword().equals(pass)){
                this.funcionarios.get(id).setSessaoIniciada(true);
                this.idFuncionario = id;
                return;
            }
            throw new PasswordInvalidaException("Password Incorreta");
        }
        throw new FuncionarioInexistenteException("Funcionário "+id+" inexistente");
    }

    /**
     * Esta funcao cicla entre os funcionarios que tem registados no sistema, e se estes forem Funcionarios de Balcao,
     * adiciona estes a uma lista, sendo esta no fim devolvida de forma a ter todos os Empregados de Balcao registados ate ao momento.
     * @return lista de Strings contendo os funcionarios de balcao toString
     */
    public List<String> getEmpregadosBalcao(){
        List<String> empregadosBalcao = new ArrayList<>();
        for(Funcionario f : funcionarios.values()){
            if(f instanceof FuncBalcao) empregadosBalcao.add(f.toString());
        }
        return empregadosBalcao;
    }

    /**
     * Esta funcao recebe estatisticas simples de tecnicos.
     * @return
     */
    public List<String> getTecnicosStatsSimples(){
      return new ArrayList<>();
    }

    /**
     * Esta funcao recebe estatisticas exaustivas de tecnicos.
     * Depois acedemos
     * @return
     */
    public List<String> getTecnicosStatsExaustivas(){
       return new ArrayList<>();
    }

    /**
     * Metodo que verifica se existe disponibilidade para um serviço Expresso,
     * ou seja, se encontrar um Funcionario Tecnico disponivel devolve true.
     * @return True se existir um funcionario tecnico disponivel.
     */
    public boolean verificaDisponibilidadeExpresso(){
        for(Funcionario f : funcionarios.values()) {
            if (f instanceof FuncTecnico && ((FuncTecnico) f).isLivre()) return true;
        }
        return false;
    }



    // ------------------------------------ MÉTODOS DA INTERFACE IGESTREGISTOS ----------------------------------------------

    /**
     * Funcao auxiliar que cria um ID de registo, verificando se o mesmo ja existe na lista
     * de registos do sistema. Se sim, cria outro.
     * @return ID de Registo.
     */
    private String geraIdRegisto(){
        Random rnd = new Random();
        Boolean done = false;
        String idGerado = "aaaa";
        while(!done){
            idGerado = rnd.ints(97, 123)
                    .limit(4)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            if(!this.registos.containsKey(idGerado)) done = true;
        }
        return idGerado;
    }


    /**
     * Metodo que cria um Registo Normal.
     * @param nif NIF do Cliente.
     * @param email Email do Cliente.
     */
    public void criaRegistoNormal(String nif, String email){
        String idReg = geraIdRegisto();
        RegistoNormal reg = new RegistoNormal(idReg,this.idFuncionario,nif);
        this.registos.put(idReg,reg);

        if(this.clientes.containsKey(nif))
            this.clientes.get(nif).alteraContacto(false,email);

        else {
            Cliente cliente = new Cliente(nif,email,"");
            this.clientes.put(nif,cliente);
        }
    }

    /**
     * Metodo que cria um Registo Expresso.
     * @param nif NIF do Cliente.
     * @param tele Telemovel do Cliente.
     * @param tipo Tipo de Reparacao
     * @param func ID do Funcionario Tecnico encarregue da reparacaow.
     */
    public void criaRegistoExpresso(String nif, String tele, String tipo, String func){
        String idReg = geraIdRegisto();
        RegistoExpresso reg = new RegistoExpresso(idReg, this.idFuncionario, tele, nif, tipo, func);
        this.registos.put(idReg, reg);

        if (this.clientes.containsKey(nif))
            this.clientes.get(nif).alteraContacto(true, tele);

        else {
            Cliente cliente = new Cliente(nif, tele, "");
            this.clientes.put(nif, cliente);
        }
    }


    /**
     * Metodo que atualiza o estado de um registo para o dado como parametro.
     * @param idRegisto ID de Registo alvo.
     * @param estado Estado atualizado.
     * @throws RegistoNaoExistenteException
     */
    public void atualizaEstado(String idRegisto,String estado) throws RegistoNaoExistenteException {
        if (registos.containsKey(idRegisto))
            registos.get(idRegisto).alteraEstado(estado);

        else throw new RegistoNaoExistenteException("ID de Registo: "+idRegisto+"inexistente");
    }


    /**
     * Metodo que devolve uma lista com os IDs de Registo que tem o estado passado como argumento.
     * @param estado Estado a procurar.
     * @return lista de IDs de registo que se adequam.
     */
    public List<String> getRegistosPorEstado(String estado){
        List<String> registos = new ArrayList<>();
        for(Registo r : this.registos.values())
            if (r.isEstado(estado)) registos.add(r.toString());
        return registos;
    }


    /**
     * Metodo que dada uma lista de IDs de Registo ordena a mesma por data crescente de chegada,
     * ou seja, os mais antigos primeiro.
     * @param lista lista alvo de organizacao.
     * @throws NotRegistoNormalException
     */
    public void ordenaRegistosPorChegada(List<String> lista) throws NotRegistoNormalException {
        for(int i=0; i<lista.size(); i++) {
            if(!(this.registos.get(lista.get(i)) instanceof RegistoNormal))
                throw new NotRegistoNormalException("ID de Registo: "+lista.get(i)+" não é Registo Normal.");

            for(int j=i+1; j<lista.size(); j++) {
                if(!(this.registos.get(lista.get(j)) instanceof RegistoNormal))
                    throw new NotRegistoNormalException("ID de Registo: "+lista.get(j)+" não é Registo Normal.");

                if(this.registos.get(lista.get(j)).getDataEntrada().isBefore(this.registos.get(lista.get(i)).getDataEntrada())) {
                    String troca = lista.get(i);
                    lista.add(i,lista.get(j));
                    lista.add(j, troca);
                }
            }
        }
    }


    /**
     * Metodo que dada uma lista de IDs de Registo ordena a mesma por data crescente de orcamento,
     * ou seja, os que foram orcamentados ha mais tempo em primeiro lugar.
     * @param lista lista alvo de organizacao.
     */
    public void ordenaRegistosPorPrazo(List<String> lista) {

        for(int i=0; i<lista.size(); i++) {
            Registo r = this.registos.get(lista.get(i));
            PlanoDeTrabalho plano;
            LocalDate data;

            if (r instanceof RegistoNormal) {
                plano = ((RegistoNormal) r).getPlano();
                data = plano.getDataOrcamento();
            }
            else data = r.getDataEntrada();


            for(int j=i+1; j<lista.size(); j++) {
                Registo reg = this.registos.get(lista.get(i));
                PlanoDeTrabalho pl;
                LocalDate novaData;

                if (reg instanceof RegistoNormal) {
                    pl = ((RegistoNormal) r).getPlano();
                    novaData = pl.getDataOrcamento();
                }
                else novaData = r.getDataEntrada();

                if(novaData.isBefore(data)) {
                    String troca = lista.get(i);
                    lista.add(i,lista.get(j));
                    lista.add(j, troca);
                }
            }
        }

    }


    /**
     * Metodo que avalia o tempo decorrido desde o momento em que a reparacao foi orcamentada.
     * Se ultrapassar o limite dos 30 dias, retorna true. Caso contrario, retorna false.
     * @param idRegisto Registo Alvo.
     * @return Se esta dentro do prazo ou não.
     * @throws RegistoNaoExistenteException
     * @throws NotRegistoNormalException
     */
    public boolean avaliaTempoDecorridoOrcamento(String idRegisto) throws RegistoNaoExistenteException, NotRegistoNormalException {
        if(this.registos.containsKey(idRegisto)) {
            Registo reg = this.registos.get(idRegisto);
            PlanoDeTrabalho pl;

            if (reg instanceof RegistoNormal) {
                pl = ((RegistoNormal) reg).getPlano();
                if(Duration.between(LocalDate.now(), pl.getDataOrcamento()).toDays() > 30) return true;
                return false;
            }
            else throw new NotRegistoNormalException("ID de Registo "+idRegisto+"nao corresponde a Registo Normal.");
        }

        else throw new RegistoNaoExistenteException("ID de Registo: "+idRegisto+"inexistente");
    }


    /**
     * Metodo que avalia o tempo decorrido desde o momento em que a reparacao foi feita.
     * Se ultrapassar o limite dos 90 dias, retorna true. Caso contrario, retorna false.
     * @param idRegisto
     * @return
     * @throws RegistoNaoExistenteException
     */
    public boolean avaliaTempoDecorridoReparado(String idRegisto) throws RegistoNaoExistenteException {
        if(this.registos.containsKey(idRegisto)) {
            Registo reg = this.registos.get(idRegisto);

            if (Duration.between(LocalDate.now(),reg.getDataEntrega()).toDays() > 90) return true;
            return false;
        }
        else throw new RegistoNaoExistenteException("ID de Registo: "+idRegisto+"inexistente");
    }


    /**
     * Metodo que regista um PlanoDeTrabalho dada uma lista com os varios passos do mesmo.
     * @param idRegisto Registo alvo.
     * @param passos Passos do PlanoDeTrabalho.
     * @throws RegistoNaoExistenteException
     * @throws NotRegistoNormalException
     */
    public void registarPlano(String idRegisto,List<Passo> passos) throws RegistoNaoExistenteException, NotRegistoNormalException {
        if(this.registos.containsKey(idRegisto)) {
            Registo reg = this.registos.get(idRegisto);
            if(reg instanceof RegistoNormal)
                ((RegistoNormal) reg).registarPassos(passos);

            else throw new NotRegistoNormalException("ID de Registo "+idRegisto+"nao corresponde a Registo Normal.");
        }
        else throw new RegistoNaoExistenteException("ID de Registo "+idRegisto+" inexistente.");

    }


    /**
     * Metodo que retorna o PlanoDeTrabalho de um dado Registo.
     * @param idRegisto Registo Alvo.
     * @return PlanoDeTrabalho do Registo.
     * @throws RegistoNaoExistenteException
     * @throws NotRegistoNormalException
     */
    public PlanoDeTrabalho getPlano(String idRegisto) throws RegistoNaoExistenteException, NotRegistoNormalException {
        if(!this.registos.containsKey(idRegisto)) throw new RegistoNaoExistenteException("ID de Registo "+idRegisto+" inexistente.");

        if(this.registos.get(idRegisto) instanceof RegistoNormal)
           return ((RegistoNormal) this.registos.get(idRegisto)).getPlano();

        else throw new NotRegistoNormalException("ID de Registo "+idRegisto+"nao corresponde a Registo Normal.");
    }


    /**
     * Metodo que avança o passo do PlanoDeTrabalho do ID de Registo dado como parâmetro.
     * @param idRegisto Registo Alvo.
     * @param passo Passo a ser avançado.
     * @param tempoReal Informacao para a conclusao do passo.
     * @param custoReal Informacao para a conclusao do passo.
     * @throws RegistoNaoExistenteException
     */
    public void avancarPasso(String idRegisto, int passo, float tempoReal, float custoReal) throws RegistoNaoExistenteException {
        if(!this.registos.containsKey(idRegisto)) throw new RegistoNaoExistenteException("ID de Registo "+idRegisto+" inexistente.");

        Registo reg = this.registos.get(idRegisto);
        PlanoDeTrabalho pl = ((RegistoNormal) reg).getPlano();
        if(pl!=null) pl.avancarPlano(passo,this.idFuncionario,tempoReal,custoReal);
    }


    /**
     * Metodo que avalia os custos dado um id de registo.
     * @param idRegisto
     * @return
     */
    public boolean avaliarCustos(String idRegisto){
        return true;
    }


    /**
     * Metodo que verifica se o PlanoDeTrabalho chegou ao fim, ou seja, se ja nao tem mais passos a executar.
     * @param idRegisto Registo Alvo.
     * @return Se chegou ao fim ou não.
     * @throws RegistoNaoExistenteException
     * @throws NotRegistoNormalException
     */
    public boolean checkPassos(String idRegisto) throws RegistoNaoExistenteException, NotRegistoNormalException {
        if (!this.registos.containsKey(idRegisto)) throw new RegistoNaoExistenteException("ID de Registo " + idRegisto + " inexistente.");

        if(this.registos.get(idRegisto) instanceof RegistoNormal)
            return ((RegistoNormal) this.registos.get(idRegisto)).getPlano().completarPlano();

        else throw new NotRegistoNormalException("ID de Registo "+idRegisto+" nao corresponde a Registo Normal.");
    }


    /**
     * Metodo que altera o estado do Registo para terminado.
     * @param idRegisto Registo Alvo.
     * @throws RegistoNaoExistenteException
     */
    public void registaConclusao(String idRegisto) throws RegistoNaoExistenteException {
        if (this.registos.containsKey(idRegisto))
            this.registos.get(idRegisto).alteraEstado(Registo.Estados.Terminado.toString());

        else throw new RegistoNaoExistenteException("ID de Registo "+idRegisto+" inexistente.");
    }


    /**
     * Metodo que altera o estado do Registo para suspendido.
     * @param idRegisto Registo Alvo.
     * @throws RegistoNaoExistenteException
     */
    public void suspendePlano(String idRegisto) throws RegistoNaoExistenteException {
        if (this.registos.containsKey(idRegisto)) {
            Registo reg = this.registos.get(idRegisto);
            if(reg instanceof RegistoNormal)
                ((RegistoNormal) reg).getPlano().pausarPlano();
        }
        else throw new RegistoNaoExistenteException("ID de Registo "+idRegisto+" inexistente.");
    }




    //------------------------------------ MÉTODOS DA INTERFACE INOTIFICACAO -----------------------------

    public void notificaTecnicos(String msg){}
    public void notificaBalcao(String msg){}


    /**
     * Metodo que devolve o Email do Cliente associado ao ID de Registo passado como argumento.
     * @param idRegisto Registo Alvo.
     * @return Email do Cliente.
     * @throws ContactoInexistenteException
     * @throws RegistoNaoExistenteException
     * @throws ClienteInexistenteException
     */
    public String getEmailCliente(String idRegisto) throws ContactoInexistenteException,RegistoNaoExistenteException,ClienteInexistenteException {
        if (registos.containsKey(idRegisto)){
            Registo reg = registos.get(idRegisto);
            String idCliente = reg.getNifCliente();
            if(clientes.containsKey(idCliente)){
                if (clientes.get(idCliente).getEmail().isBlank())
                    throw new ContactoInexistenteException("Cliente "+idCliente+" não possui Email");
                return clientes.get(idCliente).getEmail();
            }
            else throw new ClienteInexistenteException("Cliente "+idCliente+" inexistente");
        }
        else throw new RegistoNaoExistenteException("Registo "+idRegisto+" inexistente.");
    }


    /**
     * Metodo que retorna o telemovel do Cliente associado ao ID de Registo passado como parametro.
     * @param idRegisto Registo Alvo.
     * @return Telemovel do Cliente.
     * @throws ContactoInexistenteException
     * @throws RegistoNaoExistenteException
     * @throws ClienteInexistenteException
     */
    public String getTeleCliente(String idRegisto) throws ContactoInexistenteException,RegistoNaoExistenteException,ClienteInexistenteException {
        if (registos.containsKey(idRegisto)) {
            Registo reg = registos.get(idRegisto);
            String idCliente = reg.getNifCliente();
            if(clientes.containsKey(idCliente)) {
                if (clientes.get(idCliente).getTelemovel().isBlank())
                    throw new ContactoInexistenteException("Cliente "+idCliente+" não possui NºTelemóvel");
                return clientes.get(idCliente).getTelemovel();
            }
            throw new ClienteInexistenteException("Cliente "+idCliente+" inexistente");
        }
        throw new RegistoNaoExistenteException("Registo "+idRegisto+" inexistente.");
    }


    /**
     * Metodo que regista uma dada notificacao feita ao Cliente associado ao ID de Registo dado como parawmetro.
     * @param idRegisto Registo Alvo.
     * @param conteudo Conteudo da Notificacao.
     * @throws RegistoNaoExistenteException
     */
    public void registaNotifCliente(String idRegisto, String conteudo) throws RegistoNaoExistenteException{
        if (registos.containsKey(idRegisto))
            registos.get(idRegisto).registarNotificacao(idFuncionario, conteudo);

        else throw new RegistoNaoExistenteException("Registo "+idRegisto+" inexistente.");
    }
}
