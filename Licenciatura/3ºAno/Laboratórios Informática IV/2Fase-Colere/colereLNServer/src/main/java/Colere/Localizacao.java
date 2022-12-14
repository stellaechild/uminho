package Colere;
import Colere.Exceptions.*;

/**
 * Classe responsável pela representação da Localização de um Local.
 */
public class Localizacao {

    /** Endereço do Local */
    private String endereco;
    /** Latitude do Local */
    private double latitude;
    /** Longitude do Local */
    private double longitude;


    /**
     * Construtor da Classe Localização. Testa se as coordenadas fornecidas
     * são válidas, lançando uma exceção em caso de falha.
     * @param endereco Endereço do Local
     * @param latitude Latitude do Local
     * @param longitude Longitude do Local
     * @throws CoordenadasInvalidas
     */
    public Localizacao (String endereco,double latitude,double longitude) throws CoordenadasInvalidas{
        if(latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180)
            throw new CoordenadasInvalidas();
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    /**
     * Construtor Padrão. Coloca valores default nas variáveis,
     * correspondendo ao Centro de Braga.
     */
    public Localizacao(){
        this.endereco = "Centro de Braga";
        this.latitude = 41.551410;
        this.longitude = -8.422718;
    }

    // ----------------- MÉTODOS GET ------------------
    public String getEndereco()  {return endereco;}
    public double getLatitude()  {return latitude;}
    public double getLongitude() {return longitude;}


    /**
     * Método que calcula a distância (em linha reta) entre a mesma e uma
     * Localização fornecida como argumento.
     * Algoritmo consultado no site apresentado abaixo.
     *
     * @param local Localização a comparar.
     * @return Distância (em metros) dos locais.
     */
    public double distanciaAte(Localizacao local){

        double lat1 = this.latitude;
        double lat2 = local.getLatitude();
        double lon1 = this.longitude;
        double lon2 = local.getLongitude();

        //Consultar:
        //https://www.movable-type.co.uk/scripts/latlong.html
        double R = 6371e3; // metres
        double φ1 = lat1 * Math.PI/180; // φ, λ in radians
        double φ2 = lat2 * Math.PI/180;
        double Δφ = (lat2-lat1) * Math.PI/180;
        double Δλ = (lon2-lon1) * Math.PI/180;

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                   Math.cos(φ1) * Math.cos(φ2) *
                   Math.sin(Δλ/2) * Math.sin(Δλ/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c; // in metres
    }
}
