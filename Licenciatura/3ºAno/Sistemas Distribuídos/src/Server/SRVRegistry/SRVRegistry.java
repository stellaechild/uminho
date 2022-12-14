import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class SRVRegistry {

    /** Lista de Voos */
    private SortedMap<String,Flight> flights;
    /** Ocupação de cada voo */
    private SortedMap<String,Integer> occupations;
    /** Lista de Reservas */
    private Map<String,Reservation> reservations;
    /** Dias fechados */
    private Set<String> closedDays;
    /** Lock de concorrência para a lista de voos */
    private ReentrantReadWriteLock flightsLock;
    /** Lock de concorrência para a estutura de reservas, ocupações e dias fechados */
    private ReentrantReadWriteLock resOccCloLock;

    /**
     * Método construtor simples. Recebe o path para um ficheiro que contém a informação dos voos.
     * Caso o Path esteja vazio, não carrega nenhum Voo.
     * @param path Caminho do ficheiro
     * @throws IOException I/O error
     */
    public SRVRegistry(String path) throws IOException {
        this.flights = new TreeMap<>();
        this.occupations = new TreeMap<>();
        this.reservations = new TreeMap<>();
        this.closedDays = new TreeSet<>();
        if (!path.isEmpty()) readFlights(path);
        this.flightsLock = new ReentrantReadWriteLock();
        this.resOccCloLock = new ReentrantReadWriteLock();
    }

    /**
     * Método para adição de um Voo na lista de Voos.
     * @param origin Origem do Voo
     * @param destination Destino do Voo
     * @param hour Hora do Voo
     * @param capacity Capacidade do Voo
     */
    public void addFlight(String origin,String destination,String hour,int capacity){
        try {
            flightsLock.writeLock().lock();
            String key = generateFlightID(hour);
            Flight f = new Flight(key, origin, destination, hour, capacity);

            this.flights.put(key, f);
        }finally{flightsLock.writeLock().unlock();}
    }

    /**
     * Método para "fechar" um dia, impedindo novas reservas ou o cancelamento destas.
     * @param day Dia (AAAA-MM-DD)
     */
    public void closeDay(String day){
        try {
            resOccCloLock.writeLock().lock();
            this.closedDays.add(day);
        }finally {resOccCloLock.writeLock().unlock();}
    }

    /**
     * Método para obter a informação de todos os Voos com uma origem e um destino.
     * @param origin Origem do Voo
     * @param destination Destino do Voo
     * @return Lista com a informação dos vários Voos.
     * @throws NoFlightsAvailableException Nenhum Voo foi encontrado.
     */
    public List<String> infoAllFlightsFromTo(String origin,String destination) throws NoFlightsAvailableException{
        List<String> res = new ArrayList<>();

        try {
            flightsLock.readLock().lock();
            /** Percorre a lista de todos os voos */
            for (Flight flight : this.flights.values()) {
                /** Caso a origem e destino correspondam, insere a informação do voo na lista */
                if (flight.getOrigem().equals(origin) && flight.getDestino().equals(destination))
                    res.add(flight.toString());
            }
        } finally {flightsLock.readLock().unlock();}

        /** Caso a lista esteja vazia, atira exceção */
        if (res.isEmpty())
            throw new NoFlightsAvailableException();

        return res;
    }

    /**
     * Método para obter a informação de todos as Reservas efetuadas por um Utilizador.
     * @param username Utilizador
     * @return Lista com a informação das Reservas.
     */
    public List<String> infoAllReservationsUsername(String username){
        List<String> res = new ArrayList<>();

        try {
            resOccCloLock.readLock().lock();
            /** Percorre a lista de todos as Reservas */
            for (Map.Entry<String,Reservation> reservation : this.reservations.entrySet()) {
                if (reservation.getValue().getUsername().equals(username)){
                    res.add("ReservationID "+reservation.getKey()+": Data:"+reservation.getValue().getDate());
                }
            }
        } finally {resOccCloLock.readLock().unlock();}

        /** Caso a lista esteja vazia, atira exceção */
        if (res.isEmpty())
            res.add("Não contém Reservas");

        return res;
    }

    /**
     * Método para cancelamento de Reservas, este recebe o utilizador que efetuou a reserva e o Id da Reserva.
     * Verifica se o dia foi encerrado, e de seguida vai "libertando" a ocupação de cada voo, depois apaga a reserva.
     * @param username Utilizador autor da reserva
     * @param reservationID ID da reserva
     * @throws InexistentReservation Id de reserva Inexistente
     * @throws DayClosedException Dia encerrado
     */
    public void cancelReservation(String username,String reservationID) throws InexistentReservation,DayClosedException{
        try {
            resOccCloLock.writeLock().lock();
            /** Verifica se o ID da reserva existe */
            if (!this.reservations.containsKey(reservationID))
                throw new InexistentReservation();

            Reservation resv = this.reservations.get(reservationID);

            /** Verifica o autor da reserva corresponde */
            if (!resv.getUsername().equals(username))
                throw new InexistentReservation();

            /** Verifica se o dia da reserva já foi encerrado */
            if (this.closedDays.contains(resv.getDate()))
                throw new DayClosedException();

            /** Recolhe a data e os Voos da reserva */
            Set<String> flightToCancel = resv.getFlights();
            String date = resv.getDate();
            /** Para cada voo na reserva */
            for (String flightId : flightToCancel) {
                String occupationID = date + " " + flightId;
                /** Vai buscar a ocupação atual e decrementa, caso esta fique a 0, apaga-a da lista */
                int val = this.occupations.get(occupationID);
                val--;
                if (val <= 0)
                    this.occupations.remove(occupationID);
                else
                    this.occupations.put(occupationID, val);
            }

            /** Apaga a reserva */
            this.reservations.remove(reservationID);
        } finally {resOccCloLock.writeLock().unlock();}
    }

    /**
     * Método de criação de reservas de Voo. Este é executado em duas fases.
     * A primeira fase recolhe os voos de acordo com o percurso e o tempo fornecido.
     * Para esta fase o algoritmo escolhe sempre o voo mais cedo possivel que corresponde a origem e destino, repetindo o processo para o resto dos voos.
     * A segunda fase apenas incrementa a ocupação dos voos recolhidos e gera o ID da reserva
     * @param username Utilizador a efetuar a reserva
     * @param date Data da reserva
     * @param minHour Hora mínima requerida
     * @param maxHour Hora máxima requerida
     * @param route Rota dos voos
     * @return ID da Reserva
     * @throws DayClosedException Dia da reserva Fechado
     * @throws NoFlightsAvailableException Não há voos disponíveis.
     */
    public String reservarVoo(String username,String date,String minHour,String maxHour,List<String> route) throws DayClosedException,NoFlightsAvailableException{
        try {
            resOccCloLock.writeLock().lock();
            /** Verifica se o dia está encerrado */
            if (this.closedDays.contains(date) || route.size() < 2)
                throw new DayClosedException();

            Map<String, Flight> submap;
            List<String> res = new ArrayList<>();
            String hourUntilNow = minHour;

            /** Primeira fase, para cada par "Origem-Destino" na rota, analisa os voos disponíveis e se possível seleciona o voo mais cedo possível */

            try {
                flightsLock.readLock().lock();
                for (int i = 1; i < route.size(); i++) {

                    /** Cria o subMapa de todos os voos entre Minhora-MaxHora */
                    try {
                        submap = this.flights.subMap(hourUntilNow, maxHour);
                    } catch (IllegalArgumentException e) {
                        throw new NoFlightsAvailableException();
                    }

                    Boolean found = false;
                    /** Percorre a lista de subMapas */
                    for (Flight flight : submap.values()) {
                        /** Caso a origem e o destino correspondam... */
                        if (flight.getOrigem().equals(route.get(i - 1)) && flight.getDestino().equals(route.get(i))) {
                            String occupationID = date + " " + flight.getKey();

                            /** Verifica a ocupação do voo */
                            if (!this.occupations.containsKey(occupationID) || this.occupations.get(occupationID) < flight.getCapacidade()) {
                                res.add(flight.getKey());
                                /** Atualiza o minHora para a hora do voo encontrado */
                                hourUntilNow = flight.getHora();
                                found = true;
                                break;
                            }

                        }
                    }
                    /** Caso não tenha encontrado um voo, atira exceção */
                    if (!found)
                        throw new NoFlightsAvailableException();
                }
            } finally {
                flightsLock.readLock().unlock();
            }

            /** Para cada voo utilizado na reserva, incrementa a ocupação */
            for (String flightID : res) {
                String occupationID = date + " " + flightID;

                if (!this.occupations.containsKey(occupationID))
                    this.occupations.put(occupationID, 1);
                else
                    this.occupations.put(occupationID, this.occupations.get(occupationID) + 1);
            }

            /** Gera a reserva e o seu ID */
            Reservation rev = new Reservation(res.stream().collect(Collectors.toSet()), date, username);
            String revID = generateReservationID();
            this.reservations.put(revID, rev);

            return revID;
        } finally{resOccCloLock.writeLock().unlock();}
    }

    /**
     * Método auxiliar para geração de IDs de Reserva.
     * Gera um ID de 6 caracteres no formato "Rxxxxx" e verifica se esta já existe.
     * @return ID gerado.
     */
    private String generateReservationID(){
        Random rnd = new Random();
        Boolean done = false;
        String reservationID = "N/a";

        while (!done){
            reservationID = "R";
            for(int i = 0;i < 5;i++){
                int num = rnd.nextInt(26) + 97;
                char chr = (char) num;
                reservationID += chr;
            }

            if(!this.reservations.containsKey(reservationID)) done = true;
        }

        return reservationID;
    }

    /**
     * Método auxiliar para geração de IDs de Voos.
     * Gera um ID de 12 caracteres no formato "hh:mm Vxxxxx" e verifica se esta já existe.
     * @param hora Hora do voo
     * @return Id de Voo gerado
     */
    private String generateFlightID(String hora){
        Random rnd = new Random();
        Boolean done = false;
        String flightID = "N/a";

        while (!done){
            flightID = hora+" V";
            for(int i = 0;i < 5;i++){
                int num = rnd.nextInt(26) + 97;
                char chr = (char) num;
                flightID += chr;
            }

            if(!this.flights.containsKey(flightID)) done = true;
        }

        return flightID;
    }

    /**
     * Método auxiliar para leitura do ficheiro que contém a informação de todos os voos.
     * Este ficheiro tem de conter os voos no seguinte formato:
     * Origem;Destino;Hora;Capacidade
     * (Hora - hh:mm)
     * @param path Caminho do ficheiro
     * @throws IOException I/O error
     */
    private void readFlights(String path) throws IOException {
        this.flights = new TreeMap<>();
        Files.lines(Paths.get(path)).forEach(s-> {
            String[] fields = s.split(";");

            String flightId = generateFlightID(fields[2]);
            Flight flight = new Flight(flightId,fields[0],fields[1],fields[2],Integer.parseInt(fields[3]));

            this.flights.put(flightId,flight);
        });
    }
}
