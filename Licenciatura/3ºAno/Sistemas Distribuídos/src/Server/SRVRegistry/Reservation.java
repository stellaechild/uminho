import java.util.Set;
import java.util.stream.Collectors;

public class Reservation {

    /** Lista de Voos da Reserva */
    private Set<String> flights;
    /** Data dos Voos */
    private String date;
    /** Utilizador da reserva */
    private String username;

    /**
     * Método construtor simples
     * @param flights Lista de Voos da Reserva
     * @param date Data dos Voos
     * @param username Utilizador da reserva
     */
    public Reservation(Set<String> flights,String date,String username){
        this.flights = flights.stream().collect(Collectors.toSet());
        this.date = date;
        this.username = username;
    }

    /*-------------------Métodos GET---------------*/
    public Set<String> getFlights() {return flights.stream().collect(Collectors.toSet());}
    public String getDate()         {return date;}
    public String getUsername()     {return username;}
}
