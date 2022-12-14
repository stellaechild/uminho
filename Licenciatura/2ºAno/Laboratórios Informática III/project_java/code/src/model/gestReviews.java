import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author Vicente Moreira
 * @Date 13/05/2021
 *
 * Classe gestReviews
 */

public class gestReviews implements IgestReviews {

    /** Lista de Utilizadores */ private SortedMap<String,IUser> usr;
    /** Lista de Negócios */     private SortedMap<String,IBusiness> biz;
    /** Lista de Reviews */      private SortedMap<String,IReviews> rev;
    /** Estátisticas e Indexs*/  private IStats stats;
    /** Validade do catálogo*/   private Boolean valido;

    /**
     * Construtor por omissão
     */
    public gestReviews(){
        this.usr = new TreeMap<>();
        this.biz = new TreeMap<>();
        this.rev = new TreeMap<>();
        this.stats = new Stats();
        this.valido = false;
    }

    /**
     * Método principal para carregar a informação para o gestReviews
     * Carrega também as Stats necessárias
     * @param path_user Path para o ficheiro dos Users
     * @param path_bus Path para o ficheiro dos Businesses
     * @param path_rev Path para o ficheiro dos Reviews
     * @throws IOException Falha na leitura ou abertura de ficheiro.
     */
    public void load_IgestReviews_csv (String path_user, String path_bus, String path_rev) throws UserIOException,ReviewIOException,BusinessIOException {
        this.valido = false;
        long usrs_lidos = load_users      (path_user);
        long bizs_lidos = load_businesses (path_bus);
        long revs_lidos = load_review     (path_rev);

        this.rev.values().stream().forEach(rev -> this.stats.addRevToStats(rev));

        this.stats.calcula_stats(usrs_lidos,this.usr.size(),bizs_lidos,this.biz.size(),revs_lidos,this.rev.size());
        this.valido = true;
    }

    /**
     * Verificação de gestReviews válido
     * @return Boolean
     */
    public Boolean valido () {return this.valido;}

    /**
     * Devolve as Stats completas para serem escritas no terminal
     * @return String formatada
     */
    public String statsToString(){return this.stats.toString();}

    /**
     * Carrega os Users de um ficheiro, dado o path
     * @param path Path para o ficheiro
     * @return Número total de "linhas" lidas (Não confundir com Users válidos)
     * @throws IOException Load Failed
     */
    public long load_users(String path) throws UserIOException {
        AtomicLong total = new AtomicLong();
        try {
            Files.lines(Paths.get(path)).skip(1).forEach(s-> {
                    addIUser(s.split(";"));
                    total.getAndIncrement(); });
        }
        catch (IOException e){
        throw new UserIOException("Path do ficheiro de Users inválido e/ou erro de leitura.\nMore info:"+e);
        }
        return total.get();
    }

    /**
     * Carrega os Businesses de um ficheiro, dado o path
     * @param path Path para o ficheiro
     * @return Número total de "linhas" lidas (Não confundir com Businesses válidos)
     * @throws IOException Load Failed
     */
    public long load_businesses(String path) throws BusinessIOException {
        AtomicLong total = new AtomicLong();
        try {
            Files.lines(Paths.get(path)).skip(1).forEach(s->{
                        addIBusiness(s.split(";"));
                        total.getAndIncrement();});
        }
        catch (IOException e){
            throw new BusinessIOException("Path do ficheiro de Businesses inválido e/ou erro de leitura.\nMore info:"+e);
        }

        return total.get();
    }

    /**
     * Carrega as Reviews de um ficheiro, dado o path
     * @param path Path para o ficheiro
     * @return Número total de "linhas" lida (Não confundir com Reviews válidos)
     * @throws IOException Load Failed
     */
    public long load_review(String path) throws ReviewIOException {
        AtomicLong total = new AtomicLong();
        try {
            Files.lines(Paths.get(path)).skip(1).forEach(s->{
                        addIReview(s.split(";"));
                        total.getAndIncrement(); });
        }
        catch (IOException e){
            throw new ReviewIOException("Path do ficheiro de Reviews inválido e/ou erro de leitura.\nMore info:"+e);
        }

        return total.get();
    }

    /**
     * Método de adição de Users
     * @param line Lista de Linhas formatada
     */
    public Boolean addIUser(String[] line){
        if(IUser.IUserValido(line)){
            IUser usr = new User(line,false);
            this.usr.put(usr.getUserID(),usr);
            return true;
        }
        else return false;
    }

    /**
     * Método de adição de Business
     * @param line Lista de Linhas formatada
     */
    public Boolean addIBusiness(String[] line){
        if(IBusiness.IBusinessValido(line)){
            IBusiness biz = new Business(line);
            this.biz.put(biz.getBusinessID(),biz);
            return true;
        }
        else return false;
    }

    /**
     * Método de adição de Reviews
     * @param line Lista de Linhas formatada
     */
    public Boolean addIReview(String[] line){
        if(IReviews.IReviewValido(line)){
            IReviews rev = new Review(line);
            if (!this.usr.containsKey(rev.getUserID()) || !this.biz.containsKey(rev.getBusinessID()))
                return false;

            this.rev.put(rev.getReviewID(),rev);
            return true;
        }
        return false;
    }








    //-----------------------------------------------------------QUERIES----------------------------------------------------------------

    /**
     * Lista ordenada alfabeticamente com os identificadores dos negócios nunca avaliados e o seu respetivo total.
     *
     * ESTRATÉGIA: Percorrer a lista de negócios e verificar se existe no index busToRev.
     * @return Resultado
     */
    public String q1_listaNegociosNaoAvaliados(){
        StringBuilder res = new StringBuilder("Query1: Negócios não avaliados\n\n");
        // -------------------- LÓGICA DA QUERY ------------------------
        int total = 0;
        for(IBusiness bizlista: this.biz.values()){
            IBusiness biz = bizlista.clone();
            if (!this.stats.getBizToRev().containsKey(biz.getBusinessID())){
                total++;
                res.append(biz.getBusinessID()).append("\n");
            }
        }
        // --------------------- IMPRESSÃO DA QUERY -------------------------
        res.append("\nHá um total de ").append(total).append(" negócios não avaliados.\n\n");
        return res.toString();
    }

    /**
     * Dado um mês e um ano (válidos), determinar o número total global de reviews realizadas e o número total de users distintos que as realizaram
     *
     * ESTRATÉGIA: Percorrer o indíce de tradução Data -> rev, recolher o número de reviews e consultar a lista de reviews para recolher os utilizadores.
     *             Necessário verificar se o utilizador já foi recolhido.
     * @param ano Ano das reviews
     * @param mes Mês das reviews
     * @return Resultado
     */
    public String q2_reviewsNumMes(int ano, int mes){
        StringBuilder res = new StringBuilder("Query 2: Reviews Num Mês ("+mes+"/"+ano+")\n\n");
        // --------------------------------- LÓGICA DA QUERY -----------------------------------
        int total = 0;
        List<String> utilizadores = new ArrayList<>();
        LocalDateTime data = LocalDateTime.of(ano,mes,1,1,1,1);
        String data_inicio = data.toString();
        String data_fim = data.plusMonths(1).toString();
        for(String revID : this.stats.getDataToRev().subMap(data_inicio,data_fim).values()){
            total++;
            IReviews rev = this.rev.get(revID).clone();
            if(!utilizadores.contains(rev.getUserID())){
                utilizadores.add(rev.getUserID());
            }
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        res.append("Número total de reviews: ").append(total).append("\n");
        res.append("Número de utilizadores distintos: ").append(utilizadores.stream().count()).append("\n\n");
        return res.toString();
    }

    /**
     * Dado um código de utilizador, determinar, para cada mês, quantas reviews fez, quantos negócios distintos avaliou e que nota média atribuiu
     *
     * ESTRATÉGIA: Verificar o index UsrToREV, percorre a lista de reviews do utilizador e recolhe o ID dos negócios de cada review assim como as stars.
     * @param user User das reviews
     * @return Resultado
     */
    public String q3_infoUserReviews(String user){
        StringBuilder res = new StringBuilder("Query 3: Informação das Reviews de um Utilizador ("+user+")\n\n");
        // --------------------------------- LÓGICA DA QUERY -----------------------------------
        SortedMap<Integer,Integer> numRevsPorMes = new TreeMap<>();
        SortedMap<Integer,List<String>> negociosPorMes = new TreeMap<>();
        SortedMap<Integer, Double> mediasPorMes = new TreeMap<>();

        if(!this.stats.getUsrToRev().containsKey(user))
            return res.append("Utilizador inexistente e/ou sem Reviews!\n\n").toString();

        for (String revID : this.stats.getUsrToRev().get(user)) {
            IReviews rev = this.rev.get(revID).clone();
            int mesVal = rev.getDate().getMonthValue();

            if (!negociosPorMes.containsKey(mesVal)) {
                List<String> negocios = new ArrayList<>();
                negocios.add(rev.getBusinessID());
                negociosPorMes.put(mesVal, negocios);
            }
            else negociosPorMes.get(mesVal).add(rev.getBusinessID());

            int cont = numRevsPorMes.containsKey(mesVal) ? numRevsPorMes.get(mesVal) : 0;
            numRevsPorMes.put(mesVal, cont + 1);

            double soma = mediasPorMes.containsKey(mesVal) ? mediasPorMes.get(mesVal) : 0;
            mediasPorMes.put(mesVal, soma+rev.getStars());
        }

        for (Map.Entry<Integer,Double> set : mediasPorMes.entrySet()) {
            double soma = mediasPorMes.containsKey(set.getKey()) ? mediasPorMes.get(set.getKey()) : 0;
            mediasPorMes.put(set.getKey(), soma/numRevsPorMes.get(set.getKey()));
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        for (int i=1; i <= 12; i++) {
            if (numRevsPorMes.containsKey(i)) {
                String month = Month.of(i).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
                res.append(month).append(": ").append(numRevsPorMes.get(i)).append(" review");
                if(i > 1) res.append("s");
                res.append(" | Negócios distintos: ").append(negociosPorMes.get(i).stream().count()).append(" | ");
                res.append("Classificação média: ").append(String.format("%.1f",mediasPorMes.get(i))).append("\n");
            }
        }

        return res.toString();
    }

    /**
     * Dado o código de um negócio, determinar, mês a mês, quantas vezes foi avaliado, por quantos users diferentes e a média de classificação
     *
     * ESTRATÉGIA: Verificar o index BizToRev, percorre a lista de reviews do negócio e recolhe o ID dos utilizadores de cada review assim como as stars.
     * @param negocio Negócio a pesquisar.
     * @return String formatada com a informação requerida.
     */
    public String q4_avaliacaoMesAMes(String negocio) {
        StringBuilder res = new StringBuilder("Query 4: Informação das Reviews de um Negócio ("+negocio+")\n\n");

        // --------------------------------- LÓGICA DA QUERY -----------------------------------
        SortedMap<Integer,Integer> numRevsPorMes = new TreeMap<>();
        SortedMap<Integer,List<String>> usersPorMes = new TreeMap<>();
        SortedMap<Integer, Double> mediasPorMes = new TreeMap<>();
        if(!this.stats.getBizToRev().containsKey(negocio))
            return res.append("Negócio inexistente e/ou sem Reviews!\n\n").toString();

        for (String revID : this.stats.getBizToRev().get(negocio)){
            IReviews rev = this.rev.get(revID).clone();
            int mesVal = rev.getDate().getMonthValue();

            if (!usersPorMes.containsKey(mesVal)) {
                List<String> users = new ArrayList<>();
                users.add(rev.getUserID());
                usersPorMes.put(mesVal, users);
            }
            else usersPorMes.get(mesVal).add(rev.getUserID());

            int cont = numRevsPorMes.containsKey(mesVal) ? numRevsPorMes.get(mesVal) : 0;
            numRevsPorMes.put(mesVal, cont + 1);

            double soma = mediasPorMes.containsKey(mesVal) ? mediasPorMes.get(mesVal) : 0;
            mediasPorMes.put(mesVal, soma+rev.getStars());
        }

        for (Map.Entry<Integer,Double> set : mediasPorMes.entrySet()) {
            double soma = mediasPorMes.containsKey(set.getKey()) ? mediasPorMes.get(set.getKey()) : 0;
            mediasPorMes.put(set.getKey(), soma/numRevsPorMes.get(set.getKey()));
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        for (int i=1; i <= 12; i++) {
            if (numRevsPorMes.containsKey(i)) {
                String month = Month.of(i).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
                res.append(month).append(": ").append(numRevsPorMes.get(i)).append(" review");
                if(i > 1) res.append("s");
                res.append(" | Users distintos: ").append(usersPorMes.get(i).stream().count()).append(" | ");
                res.append("Classificação média: ").append(String.format("%.1f",mediasPorMes.get(i))).append("\n");
            }
        }

        return res.toString();
    }


    /**
     * Dado o código de um utilizador determinar a lista de nomes de negócios que mais avaliou (e quantos), ordenada
     * por ordem decrescente de quantidade e, para quantidades iguais, por ordem alfabética dos negócios;
     *
     * ESTRATÉGIA: Verificar o Index UserToRev, percorre a lista de reviews do user e contabiliza o número de vezes que cada negócio aparece.
     *             No fim, ordena ou por ordem decrescente do número ou, em caso de igualdade de números, por ordem alfabética.
     *
     * @param user User a pesquisar.
     * @return String formatada com a informação requerida.
     */
    public String q5_userNegociosMaisAvaliados(String user) {
        StringBuilder res = new StringBuilder("Query 5: Informação dos negócios avaliados pelo User ("+user+")\n\n");

        // --------------------------------- LÓGICA DA QUERY -----------------------------------
        SortedMap<String, Integer> negocios = new TreeMap<>();

        if(!this.stats.getUsrToRev().containsKey(user))
            return res.append("Utilizador inexistente e/ou sem Reviews!\n\n").toString();

        res.append ("Nº - Nome\n");
        for (String revID : this.stats.getUsrToRev().get(user)) {
            IReviews rev = this.rev.get(revID).clone();
            int cont = negocios.containsKey(rev.getBusinessID()) ? negocios.get(rev.getBusinessID()) : 0;
            negocios.put(rev.getBusinessID(), cont + 1);
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        int num = negocios.size();
        while (!negocios.isEmpty()) {
            String neg = "";
            int max = Integer.MIN_VALUE;

            for (Map.Entry<String, Integer> set : negocios.entrySet()) {
                if (set.getValue() > max) {
                    max = set.getValue();
                    neg = set.getKey();
                }
                else if (set.getValue() == max) {
                    if (biz.get(neg).clone().getName().compareTo(biz.get(set.getKey()).clone().getName()) > 0) neg = set.getKey();
                }
            }
            res.append(max).append("  - ").append(biz.get(neg).clone().getName()).append("\n");
            negocios.remove(neg);
        }
        res.append("\nNúmero de negócios distintos avaliados: ").append(num).append("\n");

        return res.toString();
    }

    /**
     * Determinar o conjunto dos X negócios mais avaliados (com mais reviews) em cada ano, indicando o número total de distintos utilizadores
     * que o avaliaram (X é um inteiro dado pelo utilizador);
     *
     * ESTRATÉGIA: Percorrendo a lista de reviews, fomos armazenando os IDs de negócios assim como o nº de reviews de acordo com o ano da review.
     *             Para evitar utilizadores repetidos criamos uma "black list" de IDs negócios e a lista dos users que já o avaliaram.
     *
     * @param x Top X de negócios de cada ano.
     * @return String formatada com a informação requerida.
     */
    public String q6_conjuntoXNegociosMaisAvaliados(int x) {
        StringBuilder res = new StringBuilder("Query 6: Top "+x+" de negócios mais avaliados por ano.\n\n");

        // --------------------------------- LÓGICA DA QUERY -----------------------------------
        Map<String, List<String>> usersPorNegocio = new HashMap<>();
        SortedMap<Integer, SortedMap<String, Integer>> negociosPorAno = new TreeMap<>();

        for (IReviews revlista : this.rev.values()) {
            IReviews rev = revlista.clone();
            int ano = rev.getDate().getYear();

            if (!usersPorNegocio.containsKey(rev.getBusinessID()) || !usersPorNegocio.get(rev.getBusinessID()).contains(rev.getUserID())) {
                if (!negociosPorAno.containsKey(ano)) {
                    SortedMap<String, Integer> lista = new TreeMap<>();
                    lista.put(rev.getBusinessID(), 1);
                    negociosPorAno.put(ano, lista);
                } else {
                    int cont = negociosPorAno.get(ano).containsKey(rev.getBusinessID()) ? negociosPorAno.get(ano).get(rev.getBusinessID()) : 0;
                    negociosPorAno.get(ano).put(rev.getBusinessID(), cont + 1);
                }

                if (!usersPorNegocio.containsKey(rev.getBusinessID())) {
                    List<String> users = new ArrayList<>();
                    users.add(rev.getUserID());
                    usersPorNegocio.put(rev.getBusinessID(), users);
                } else usersPorNegocio.get(rev.getBusinessID()).add(rev.getUserID());
            }
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        for (Map.Entry<Integer, SortedMap<String, Integer>> sorted : negociosPorAno.entrySet()) {
            int i=0;
            res.append("ANO ").append(sorted.getKey()).append(":\n");
            while (!sorted.getValue().isEmpty() && i<x) {
                String neg = "";
                int max = Integer.MIN_VALUE;
                for (Map.Entry<String, Integer> set : sorted.getValue().entrySet()) {
                    if (set.getValue() > max) {
                        max = set.getValue();
                        neg = set.getKey();
                    }
                }
                res.append("    ").append(i+1).append("º (").append(max).append(" users) - ").append(biz.get(neg).clone().getName()).append("\n");
                sorted.getValue().remove(neg);
                i++;
            }
        }
        return res.toString();
    }

    /**
     * Determinar, para cada cidade, a lista dos três mais famosos negócios em termos de número de reviews.
     *
     * ESTRATÉGIA: Percorremos o Index BizToRev, e para cada negócio calculamos o número de reviews.
     *             De seguida, inserimos numa lista de correspondência cidade->negócios.
     *             No final imprimimos os negócios de cada cidade com ordem decrescente do número de reviews.
     *
     * @return String formatada com a informação requerida.
     */
    public String q7_cityHallOfFame() {
        StringBuilder res = new StringBuilder("Query 7: Top 3 negócios de cada cidade.\n\n");

        // ----------------------------------- LÓGICA DA QUERY ---------------------------------------
        SortedMap<String, SortedMap<String, Integer>> cidades = new TreeMap<>();

        for (Map.Entry<String, List<String>> bizToRev : this.stats.getBizToRev().entrySet()) {
            IBusiness bus = biz.get(bizToRev.getKey()).clone();

            if (!cidades.containsKey(bus.getCity())) {
                SortedMap<String, Integer> negocio = new TreeMap<>();
                negocio.put(bus.getBusinessID(), bizToRev.getValue().size());
                cidades.put(bus.getCity(), negocio);
            }
            else {
                SortedMap<String, Integer> negocios = cidades.get(bus.getCity());
                negocios.put(bus.getBusinessID(), bizToRev.getValue().size());
            }
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        for (Map.Entry<String, SortedMap<String, Integer>> set : cidades.entrySet()) {
            res.append(set.getKey()).append("\n");
            SortedMap<String, Integer> negocios = set.getValue();
            int i=0;
            while (!negocios.isEmpty() && i<3) {
                String neg = "";
                int max = Integer.MIN_VALUE;

                for (Map.Entry<String, Integer> biz : negocios.entrySet()) {
                    if (biz.getValue() > max) {
                        max = biz.getValue();
                        neg = biz.getKey();
                    }
                }

                res.append("   ").append(max).append(" - ").append(biz.get(neg).clone().getName()).append("\n");
                negocios.remove(neg);
                i++;
            }
        }
        return res.toString();
    }


    /**
     * Determinar o código dos X users que avaliaram mais negócios diferentes, indicando quantos, sendo o critério de
     * ordenação a ordem decrescente do número dos negócios.
     *
     * ESTRATÉGIA: Percorre o Index UsertoRev e executa a funçao negocios_distintos que calcula o numero de negocios
     *             distintos de uma lista de reviews.
     *
     * @param x Top X número de users avaliadores do negócio.
     * @return String formatada com a informação requerida.
     */
    public String q8_usersComMaisReviews(int x){
        StringBuilder res = new StringBuilder("Query 8: Top "+x+" users avaliadores de negócios diferentes, (ordenação decrescente).\n\n");

        // ------------------------------------ LÓGICA DA QUERY --------------------------------------
        SortedMap<String,Integer> userSet = new TreeMap<>();
        for(Map.Entry<String,List<String>> set : this.stats.getUsrToRev().entrySet()){
            String userId = set.getKey();
            int numero_negocios_distintos = negocios_distintos(set.getValue());
            userSet.put(userId,numero_negocios_distintos);
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        int i = 0;
        while (!userSet.isEmpty() && i<x) {
            String user = "";
            int max = Integer.MIN_VALUE;

            for (Map.Entry<String, Integer> set : userSet.entrySet()) {
                if (set.getValue() > max) {
                    max = set.getValue();
                    user = set.getKey();
                }
            }
            res.append(i+1).append("º: ").append(max).append(" - ").append(user).append(" - ").append(this.usr.get(user).clone().getName()).append("\n");
            userSet.remove(user);
            i++;
        }

        return res.toString();
    }

    /**
     * Dado o código de um negócio, determinar o conjunto dos X users que mais o avaliaram e, para cada um, qual o valor
     * médio de classificação - ordenação cf. 5 (ordenada por ordem decrescente de quantidade e, para quantidades iguais,
     * por ordem alfabética dos negócios.
     *
     * ESTRATÉGIA: Percorrendo a lista de reviews de um dado negocio,(usando BizToRev), recolhemos os users, assim como
     *             o numero de vezes que estes aparecem e a sua media de classificacao.
     *
     * @param negocio Negócio alvo.
     * @param x Top X de users avaliadores.
     * @return String formatada com a informação requerida.
     */
    public String q9_usersMaisAvaliaramNegocio(String negocio,int x){
        StringBuilder res = new StringBuilder("Query 9: Top "+x+" users que mais avaliaram o negócios (").append(negocio).append(")\n\n");

        // --------------------------------- LÓGICA DA QUERY -----------------------------------
        SortedMap<String,Integer> bizSet = new TreeMap<>();
        SortedMap<String,Double> mediasUser = new TreeMap<>();
        for (String revs : this.stats.getBizToRev().get(negocio)){
            String user = this.rev.get(revs).clone().getUserID();

            int cont = bizSet.containsKey(user) ? bizSet.get(user) : 0;
            bizSet.put(user, cont + 1);

            double num = mediasUser.containsKey(user) ? mediasUser.get(user) : 0;
            mediasUser.put(user, num + this.rev.get(revs).clone().getStars());
        }
        for (Map.Entry<String,Double> set : mediasUser.entrySet()){
            double num = mediasUser.containsKey(set.getKey()) ? mediasUser.get(set.getKey()) : 0;
            mediasUser.put(set.getKey(), num/bizSet.get(set.getKey()));
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        res.append("# | NºRev | Nome | Média\n");
        int i = 0;
        while (!bizSet.isEmpty() && i<x) {
            String user = "";
            int max = Integer.MIN_VALUE;
            for (Map.Entry<String, Integer> set : bizSet.entrySet()) {
                if (set.getValue() > max) {
                    max = set.getValue();
                    user = set.getKey();
                }
                else if (set.getValue() == max) {
                    if (this.usr.get(user).clone().getName().compareTo(this.usr.get(set.getKey()).clone().getName()) > 0) user = set.getKey();
                }
            }
            res.append(i+1).append("º: ").append(max).append(" - ").append(this.usr.get(user).clone().getName()).append(" - ").append(mediasUser.get(user)).append("\n");
            bizSet.remove(user);
            i++;
        }

        return res.toString();
    }

    /**
     * Determinar para cada estado, cidade a cidade, a média de classificação de cada negócio.
     *
     * ESTRATÉGIA: Percorremos o Index BusToRev e vamos montando listas sucessivas estado->cidade, cidade->negócios.
     *             Também calculamos numa lista negócio->média a classificação de cada negócio.
     *
     * @return String formatada com a informação requerida.
     */
    public String q10_medias(){
        StringBuilder res = new StringBuilder("Query 10: Média de classificações dos negócios para cada cidade em cada estado.\n\n");

        // -------------------------------------------- LÓGICA DA QUERY ----------------------------------------------
        SortedMap<String, Double> medias = new TreeMap<>();
        SortedMap<String, SortedMap<String, List<String>>> local = new TreeMap<>();

        for (Map.Entry<String, List<String>> bizToRev : this.stats.getBizToRev().entrySet()) {
            IBusiness bus = biz.get(bizToRev.getKey()).clone();
            double media = media_classificacao(bizToRev.getValue());
            medias.put(bus.getBusinessID(), media);

            if (!local.containsKey(bus.getState())) {
                SortedMap<String, List<String>> cidade = new TreeMap<>();
                List<String> negocios = new ArrayList<>();
                negocios.add(bus.getBusinessID());
                cidade.put(bus.getCity(), negocios);
                local.put(bus.getState(), cidade);
            }
            else {
                SortedMap<String, List<String>> estado = local.get(bus.getState());
                if (!estado.containsKey(bus.getCity())) {
                    List<String> negocios = new ArrayList<>();
                    negocios.add(bus.getBusinessID());
                    estado.put(bus.getCity(), negocios);
                }
                else estado.get(bus.getCity()).add(bus.getBusinessID());
            }
        }

        // --------------------------------- IMPRESSÃO DA QUERY ---------------------------------------
        for (Map.Entry<String, SortedMap<String, List<String>>> estado : local.entrySet()) {
            res.append("STATE: ").append(estado.getKey()).append("\n");
            for (Map.Entry<String, List<String>> cidade : local.get(estado.getKey()).entrySet()) {
                res.append(cidade.getKey()).append(":\n");
                for (String negocio : cidade.getValue()) {
                    res.append("    ").append(String.format("%.1f",medias.get(negocio))).append(" - ").
                            append(biz.get(negocio).clone().getName()).append("\n");
                }
            }
        }
        return res.toString();
    }

    /**
     * Função auxiliar das queries, responsável pelo cálculo da média da classificação, dada uma lista de reviews.
     *
     * @param revs Lista de reviews.
     * @return média das classificações.
     */
    public double media_classificacao (List<String> revs) {
        double res = 0;
        double cont = 0;
        for (String revID : revs) {
            res += this.rev.get(revID).clone().getStars();
            cont++;
        }
        return res/cont;
    }

    /**
     * Funçao auxiliar das queries, responsavel pelo calculo do numero de negocios distintos a partir de uma lista de
     * reviews.
     *
     * @param revs Lista de reviews.
     * @return numero de negocios distintos.
     */
    public int negocios_distintos (List<String> revs) {
        int res = 0;
        List<String> negocios = new ArrayList<>();
        for (String revID : revs) {
            String idNegocio = this.rev.get(revID).clone().getBusinessID();
            if(!negocios.contains(idNegocio)){
                negocios.add(idNegocio);
                res+=1;
            };
        }
        return res;
    }

}
