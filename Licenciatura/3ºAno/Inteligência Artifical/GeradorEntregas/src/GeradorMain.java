import java.io.*;
import java.util.Random;

public class GeradorMain {

    public static final String[] ruas = {
            "Rua da Universidade","Rua da Lage",
            "Rua da Mouta", "Largo de São Pedro",
            "Avenida Padre Júlio Fragata","Rua Monsenhor Ferreira",
            "Rua de São Vicente","Rua Conselheiro Januário",
            "Rua da Boavista", "Campo das Hortas",
            "Rua Conselheiro Lobato","Avenida da Liberdade",
            "Rua Frei José Vilaça","Rua António Ferreira Rito",
            "Rua da Igreja","Avenida Dom João II"
    };

    public static void main(String args[]) throws IOException {
        System.out.print("GIMME A SEED:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        long seed = Long.parseLong(line);

        System.out.print("How many?:");
        line = reader.readLine();
        int n = Integer.parseInt(line);

        Random rnd = new Random(seed);

        File file = new File("Results.txt");
        PrintWriter print = new PrintWriter(file);

        for(int i = 1; i <= n;i++){
            int idEstafeta = rnd.nextInt(8)+1;
            int idCliente = rnd.nextInt(12)+1;
            print.println("entrega("+i+","+idEstafeta+","+idCliente+").");
        }

        print.println("\n\n\n\n");

        for(int i = 1; i<= n;i++){
            int idVeiculo = rnd.nextInt(9)+1;
            int tempoEntrega = rnd.nextInt(118)+3;
            //int preco = rnd.nextInt(37)+4;
            String data = geraData2021(rnd);
            int satInt = rnd.nextInt(6);
            int satDec = 0;
            if (satInt != 5) satDec = rnd.nextInt(10);
            String sat = ""+satInt+"."+satDec+"";

            int estado = 0;
            int dice = rnd.nextInt(10);
            if (dice == 7) estado = 2;
            if (dice == 4) estado = 1;

            int freg = rnd.nextInt(8)+1;
            String rua = geraRuaFreg(rnd,freg);

            int peso;

            switch (idVeiculo){
                case 1,2,3,4: peso = rnd.nextInt(5)+1; break;
                case 7,8,9: peso = rnd.nextInt(20)+1; break;
                default:  peso = rnd.nextInt(100)+1; break;
            }
            int preco = 5 + peso/2;

            int volume = peso;
            if (volume > 5) {
                if (volume < 20) dice = rnd.nextInt(6) - 2;
                else dice = rnd.nextInt(11) - 5;
                volume += dice;
            }
            //int volumeInt = rnd.nextInt(20);
            //int volumeDec = rnd.nextInt(10);
            //String volume = ""+volumeInt+"."+volumeDec+"";

            print.println("info("+i+","+idVeiculo+","+tempoEntrega+","+preco+","+data+","+sat+","+estado+","+freg+","+rua+","+peso+","+volume+").");
        }


        print.flush();
        print.close();
    }



    public static String geraRuaFreg(Random rnd, int freg){
        int dice = rnd.nextInt(2);
        int idx = freg*2 + dice - 2;

        return "'"+ruas[idx]+"'";
    }

    public static String geraData2021(Random rnd){
        int mes = rnd.nextInt(12)+1;
        int dia;
        switch (mes){
            case 2: dia = rnd.nextInt(28)+1; break;
            case 1,3,5,7,8,10,12: dia = rnd.nextInt(31)+1; break;
            default: dia = rnd.nextInt(30)+1; break;
        }
        int hora = rnd.nextInt(24);
        int minuto = rnd.nextInt(60);

        return "[2021,"+mes+","+dia+","+hora+","+minuto+"]";
    }
}
