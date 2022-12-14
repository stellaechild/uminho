public class Entrega {
    private String id;
    private String morada;
    private double peso;
    private double volume;
    private double tempoMax;
    private double distancia;
    private boolean estado;

    public int MelhorVeiculo(){
        int v;
        if(peso<=5){
            v=0;
        }else if(peso<=20&& peso>5){
            v=1;
        }else v=2;
        return v;
    }
    public double calculaTempoEntrega(){
        double t=0;
        int aux1= (int) peso;
        double aux= aux1;
        switch (this.MelhorVeiculo()){
            case 0:
                aux= aux*0.7*10;
            case 1:
                aux= aux*0.5*20;
            case 2:
                aux=aux*0.1*35;
        }
        t=distancia/aux;
        return t;
    }

}
