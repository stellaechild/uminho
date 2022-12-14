import java.text.NumberFormat;
import java.util.Scanner;

/**
 * Aplicação de Testes de Performance ao programa gestReviewsAppMVC.
 * @author Joana Alves
 *
 * Versão: 17/06/2021
 */

public class TestApp {
    private static IgestReviews sgr;

    private static final int numTestes = 11;
    private static final int numIter   = 10;

    private static final double divMili = 1000000;
    private static final double divSegs = 1000000000;
    private static double tempos[];
    private static long loadInic;

    private static final int x = 10;
    private static final String user = "s3ISezuKaSJST4rkFcY3IQ";
    private static final String bus  = "ZRhYwKCaDZqnngd-KJlixw";



    public static void main(String[] args) {
        sgr = new gestReviews();
        tempos = new double[numTestes];
        System.out.println("Testes de performance iniciados!\nNúmero de testes a serem executados: "+numTestes);
        System.out.println("Número de iterações de cada teste: "+numIter+"\n");


        // -------------------------------- TESTE LOAD ----------------------------------
        System.out.print("Teste 1 (load dos ficheiros) iniciado");
        double tempo = performance_load ();
        if (tempo == 0) return;
        tempos[0] = tempo;

        // ---------------------------------- TESTE QUERIES -------------------------------

        for (int query = 1; query <= 10; query++) {
            double soma = 0;
            System.out.print("Teste "+(query+1)+" (QUERY "+query+") iniciado");
            for (int teste = 1; teste <= numIter; teste++) {
                System.out.print(".");
                soma += performance_query(query);
            }
            tempos[query] = (soma/numIter);
            System.out.println("");
        }
        System.out.println("");

        // ---------------------------------- ENTER --------------------------------------
        Scanner sc = new Scanner(System.in);
        System.out.print("Prima qualquer tecla para visualizar os resultados: ");
        sc.nextLine();

        // ---------------------------------- RESULTADOS ---------------------------------

        System.out.println("\nRESULTADOS:");
        for (int i=0; i < numTestes; i++) {
            System.out.print("Teste "+(i+1)+": ");
            printTime(tempos[i]);
            if(i == 0) {
                System.out.print("  |  ");
                printMemory(loadInic);
            }
            System.out.println("");
        }
    }



    // ------------------------------------------- MÉTODOS AUXILIARES -----------------------------------------
    /**
     * Método que calcula a média do tempo de execução de 10 iterações,
     * do método de laod dos dados a partir dos ficheiros.
     *
     * @return média do tempo de execução.
     */
    public static double performance_load() {
        double soma = 0;
        for (int i=0; i<numIter; i++) {
            sgr = new gestReviews();
            long start = System.nanoTime();
            Runtime runtime = Runtime.getRuntime();
            long StMem = runtime.totalMemory() - runtime.freeMemory();
            try {
                System.out.print(".");
                sgr.load_IgestReviews_csv(IgestReviews.user_path, IgestReviews.bus_path, IgestReviews.rev_path);
            }
            catch (UserIOException | ReviewIOException | BusinessIOException e) {
                System.out.println("Carregamento de ficheiros inválido!\n");
                return 0;
            }
            long end = System.nanoTime();
            long EdMem = runtime.totalMemory() - runtime.freeMemory();

            if(i == 0) loadInic = EdMem - StMem;

            soma += end - start;
        }
        System.out.println("");
        return (soma/numIter);
    }

    /**
     * Método que calcula o tempo de execução de uma dada query.
     *
     * @param num Query alvo.
     * @return Tempo de execução da query.
     */
    public static double performance_query (int num) {
        long inicio = System.nanoTime();
        switch (num) {
            case 1:
                sgr.q1_listaNegociosNaoAvaliados();
                break;
            case 2:
                sgr.q2_reviewsNumMes(2019, 5);
                break;
            case 3:
                sgr.q3_infoUserReviews(user);
                break;
            case 4:
                sgr.q4_avaliacaoMesAMes(bus);
                break;
            case 5:
                sgr.q5_userNegociosMaisAvaliados(user);
                break;
            case 6:
                sgr.q6_conjuntoXNegociosMaisAvaliados(x);
                break;
            case 7:
                sgr.q7_cityHallOfFame();
                break;
            case 8:
                sgr.q8_usersComMaisReviews(x);
                break;
            case 9:
                sgr.q9_usersMaisAvaliaramNegocio(bus, x);
                break;
            case 10:
                sgr.q10_medias();
                break;
            default:
                break;
        }
        long end = System.nanoTime();
        return (end-inicio);
    }
    public static void printMemory(long memory) {
        double memPrint = memory;
        int unit = 0;
        while (memPrint > 1024) {
            memPrint /= 1024;
            unit++;
        }
        System.out.print ("Memória utilizada: " +String.format("%.3f",memPrint)+" ");
        if (unit == 0) System.out.print("bytes");
        else if (unit == 1) System.out.print("kibibytes");
        else if (unit == 2) System.out.print("mebibytes");
        else if (unit == 3) System.out.print("gibibytes");
        else System.out.print("");
    }

    public static void printTime (double time) {
        int unit = 0;
        while (time > 1000) {
            time /= 1000;
            unit++;
        }
        System.out.print ("Tempo médio: " +String.format("%.3f",time)+" ");
        if (unit == 0) System.out.print("nanosegundos");
        else if (unit == 1) System.out.print("microsegundos");
        else if (unit == 2) System.out.print("milissegundos");
        else if (unit == 3) System.out.print("segundos");
        else System.out.print("");
    }
}
