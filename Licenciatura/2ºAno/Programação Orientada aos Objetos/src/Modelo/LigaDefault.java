import javax.print.event.PrintJobAdapter;
import java.time.LocalDate;

/**
 * Classe do Controlador
 * @author Vicente Moreira
 * @author Joana Alves
 * @date 06/05/2021
 */

public class LigaDefault {

    public static Liga ligaDefault(){
        Liga liga = new Liga("Liga UM - 20/21");
        liga.setData_atual(LocalDate.of(2021,1,1));

        try {liga.addEquipa(LigaDefault.laceiras(liga.getData_atual()));}
        catch (EquipaJaExistenteException e){}

        try { liga.addEquipa(LigaDefault.celoricense(liga.getData_atual()));}
        catch (EquipaJaExistenteException e){}

        try { liga.addEquipa(LigaDefault.realMadre(liga.getData_atual()));}
        catch (EquipaJaExistenteException e) {}

        try { liga.addEquipa(LigaDefault.python(liga.getData_atual())); }
        catch (EquipaJaExistenteException e) {}

        return liga;
    }

    /* MELHOR EQUIPA DA LIGA */
    private static Equipa realMadre (LocalDate data) {
        Equipa realMadre = new Equipa ("Real Madre");

        Estadio erealMadre = new Estadio("Estádio Real Madre", "Rua dos Campeões", 5000);
        realMadre.setEstadio(erealMadre);

        //TREINADORES
        Treinador real1 = new Treinador("Jesus Conceição", 80, Treinador.PRINCIPAL);
        Treinador real2 = new Treinador("Damiano Lopes", 75, Treinador.ADJUNTO);
        Treinador real3 = new Treinador("Ricardo Reis", 60, Treinador.SUPLENTE);
        try {realMadre.addTreinador(real1);realMadre.addTreinador(real2);realMadre.addTreinador(real3);}
        catch (TreinadorJaExistenteException e) {}

        //GUARDA-REDES
        Jogador rgr1 = new GuardaRedes("Eusébio Teixeira", 1, 70,95,99,60,20,86,67,87,94,98);
        Jogador rgr2 = new GuardaRedes("Constantino Lopez",2, 65,90,94,55,15,81,60,80,90,94);

        //DEFESAS
        Jogador rdef1 = new Defesa("Jorge Zara",   15, 67, 86, 81, 90, 70, 89, 97, 99, 79, 88);
        Jogador rdef2 = new Defesa("Prim Ark",     23, 70, 90, 86, 95, 60, 90, 90, 90, 70, 81);
        Jogador rdef3 = new Defesa("Dulci Gabana", 17, 75, 95, 87, 94, 59, 85, 87, 89, 71, 80);
        Jogador rdef4 = new Defesa("Ibraham Ovic", 19, 76, 96, 88, 95, 60, 86, 88, 90, 72, 81);
        Jogador rdef5 = new Defesa("Ferrero Roche",27, 70, 90, 80, 90, 60, 80, 80, 90, 70, 80);

        //MÉDIOS
        Jogador rmed1 = new Medio("Bongo Pomme",  60, 80, 82, 90, 87, 79, 83, 91, 95, 96, 97);
        Jogador rmed2 = new Medio("Antoine Costa",55, 85, 87, 95, 92, 84, 88, 96, 97, 80, 91);
        Jogador rmed3 = new Medio("Rone Table",   43, 75, 77, 85, 82, 74, 78, 86, 87, 82, 90);
        Jogador rmed4 = new Medio("Mark Donalds", 52, 81, 83, 91, 88, 80, 84, 92, 96, 97, 98);
        Jogador rmed5 = new Medio("Peter Pan",    31, 79, 81, 89, 86, 78, 84, 90, 94, 95, 96);

        //AVANÇADOS
        Jogador ravan1 = new Avancado("Messi Conhaque",80, 90,95,99,92,93,94,96,93,92,91);
        Jogador ravan2 = new Avancado("Beck Ham",      75, 91,96,99,93,94,95,97,94,93,92);
        Jogador ravan3 = new Avancado("Ho Lee Fuk",    85, 89,94,98,91,92,93,95,92,91,90);
        Jogador ravan4 = new Avancado("Luís Camões",   70, 95,99,99,97,98,99,99,99,98,96);
        Jogador ravan5 = new Avancado("Gordon Freeman",90, 85,90,95,90,90,89,91,90,88,86);


        try {
            realMadre.addJogador(rgr1, data); realMadre.addJogador(rgr2,data);
            realMadre.addJogador(rdef1,data); realMadre.addJogador(rdef2,data); realMadre.addJogador(rdef3,data); realMadre.addJogador(rdef4,data); realMadre.addJogador(rdef5,data);
            realMadre.addJogador(rmed1,data); realMadre.addJogador(rmed2,data); realMadre.addJogador(rmed3,data); realMadre.addJogador(rmed4,data); realMadre.addJogador(rmed5,data);
            realMadre.addJogador(ravan1,data);realMadre.addJogador(ravan2,data);realMadre.addJogador(ravan3,data);realMadre.addJogador(ravan4,data);realMadre.addJogador(ravan5,data);
        }
        catch (JogadorJaExistenteException e) {}

        return realMadre;
    }


    private static Equipa python (LocalDate data) {
        Equipa python = new Equipa("Manchester Town");

        Estadio e = new Estadio("Manchester Town Stadium","Avenida da Liberdade",1000);
        python.setEstadio(e);

        //TREINADORES
        Treinador t1 = new Treinador("Joaquim Mota e Silva", 50, Treinador.PRINCIPAL);
        Treinador t2 = new Treinador("Arnaldo Cunha", 49, Treinador.ADJUNTO);
        try { python.addTreinador(t1); python.addTreinador(t2); }
        catch (TreinadorJaExistenteException a) {}


        /* FALTAM OS STATS TODOS NOS JOGADORES */

        //GUARDA REDES
        Jogador g1 = new GuardaRedes("Cristiano Ronaldi",1,21,51,32,51,10,15,38,67,30,19);
        Jogador g2 = new GuardaRedes("Marcelo Rebelo",   2,21,51,32,51,10,15,38,67,30,19);

        //DEFESAS
        Jogador d1 = new Defesa("Vivaldi Stazione",10,50, 40, 48, 70, 32, 55, 81, 67,40,52);
        Jogador d2 = new Defesa("Dinoh Mite",      11,51, 41, 49, 71, 33, 56, 82, 68,41,53);
        Jogador d3 = new Defesa("Stan Dupp",       12,49, 39, 47, 69, 31, 54, 80, 66,39,51);
        Jogador d4 = new Defesa("Max Ehmumm",      13,52, 42, 50, 72, 34, 57, 83, 69,42,54);

        //MEDIOS
        Jogador m1 = new Medio("Cardi D",      30,61,71,62,56,61,56,55,59,44,61);
        Jogador m2 = new Medio("John Lenda",   31,62,72,63,57,62,57,56,60,45,62);
        Jogador m3 = new Medio("Jay-C",        32,60,71,62,55,60,55,55,58,43,60);
        Jogador m4 = new Medio("Vin Gas",      33,63,73,64,58,63,58,57,61,46,63);
        Jogador m5 = new Medio("Nicolas Jaula",34,65,75,68,50,69,60,52,57,47,64);

        //AVANÇADOS
        Jogador a1 = new Avancado("Angelino Jolie",60,50,65,40,55,60,55,60,60,65,60);
        Jogador a2 = new Avancado("Jimmy Fell",    61,51,66,41,56,61,56,61,61,66,61);
        Jogador a3 = new Avancado("Mario Carey",   62,49,64,39,54,59,54,59,59,64,59);
        Jogador a4 = new Avancado("Ben Aflição",   63,52,67,42,57,62,57,62,62,67,62);
        Jogador a5 = new Avancado("Cameron Diaz",  64,55,70,50,60,65,65,65,65,70,55);

        try{
            python.addJogador(g1,data);python.addJogador(g2,data);
            python.addJogador(d1,data);python.addJogador(d2,data);python.addJogador(d3,data);python.addJogador(d4,data);
            python.addJogador(m1,data);python.addJogador(m2,data);python.addJogador(m3,data);python.addJogador(m4,data);python.addJogador(m5,data);
            python.addJogador(a1,data);python.addJogador(a2,data);python.addJogador(a3,data);python.addJogador(a4,data);python.addJogador(a5,data);
        }
        catch (JogadorJaExistenteException b) {}

        return python;
    }

    private static Equipa celoricense (LocalDate data) {
        Equipa celoricense = new Equipa("Celoricense");

        Estadio cel = new Estadio("Estádio Municipal Celoricense", "Rua do Campo de Jogos", 300);
        celoricense.setEstadio(cel);

        //TREINADORES
        Treinador cel1 = new Treinador("Abílio Campos", 50, Treinador.PRINCIPAL);
        Treinador cel2 = new Treinador("Joaquim Teixeira", 35, Treinador.ADJUNTO);
        try {celoricense.addTreinador(cel1); celoricense.addTreinador(cel2);}
        catch (TreinadorJaExistenteException e) {}

        //GUARDA-REDES
        Jogador cgr1 = new GuardaRedes("Rui Patrício", 1, 20,50,31,50,4,11,30,77,90,89);
        Jogador cgr2 = new GuardaRedes("Mário Marinho",2, 15,40,30,50,4,11,30,67,60,70);

        //DEFESAS
        Jogador cdef1 = new Defesa("Miguel Antunes",10, 50, 40, 48, 70, 32, 55, 81, 67,40,52);
        Jogador cdef2 = new Defesa("Messi Carvalho",11, 56, 32, 67, 68, 40, 39, 76, 60,56,47);
        Jogador cdef3 = new Defesa("Filipe Neto",   12, 57, 36, 52, 69, 35, 48, 78, 63,48,50);

        //MEDIOS
        Jogador cmed1 = new Medio("Miguel Oliveira", 30,60,70,61,55,60,55,54,58,43,60);
        Jogador cmed2 = new Medio("Fernando Pimenta",31,55,65,56,50,55,50,49,53,58,55);
        Jogador cmed3 = new Medio("Telmo Monteiro",  38,65,75,66,60,65,60,59,63,48,65);
        Jogador cmed4 = new Medio("Patrício Mamona", 45,58,68,59,53,58,53,52,56,41,58);

        //AVANÇADOS
        Jogador cavan1 = new Avancado("Nicole Faíscas",   76,70,55,40,65,50,60,60,60,65,60);
        Jogador cavan2 = new Avancado("Aurélio Fernandes",80,65,50,35,60,45,55,55,55,60,55);
        Jogador cavan3 = new Avancado("Fernando Pessoa",  82,75,60,45,70,55,65,65,65,70,65);
        Jogador cavan4 = new Avancado("Alberto Caeiro",   90,71,56,41,66,51,61,61,61,66,61);
        Jogador cavan5 = new Avancado("Benício Freitas",  91,69,54,39,66,51,61,61,61,66,61);

        try {
            celoricense.addJogador(cgr1,data);  celoricense.addJogador(cgr2,data);
            celoricense.addJogador(cdef1,data); celoricense.addJogador(cdef2,data); celoricense.addJogador(cdef3,data);
            celoricense.addJogador(cmed1,data); celoricense.addJogador(cmed2,data); celoricense.addJogador(cmed3,data); celoricense.addJogador(cmed4,data);
            celoricense.addJogador(cavan1,data);celoricense.addJogador(cavan2,data);celoricense.addJogador(cavan3,data);celoricense.addJogador(cavan4,data);celoricense.addJogador(cavan5,data);
        }
        catch (JogadorJaExistenteException e) {}

        return celoricense;
    }

    private static Equipa laceiras(LocalDate data){
        Equipa laceiras = new Equipa("Laceiras");

        Estadio lac = new Estadio("Laceiras Stadium","Rua Lages",500);
        laceiras.setEstadio(lac);;

        //TREINADORES
        Treinador t1 = new Treinador("José Fernandes",37,Treinador.PRINCIPAL);
        Treinador t2 = new Treinador("Marco Paulo",19,Treinador.ADJUNTO);
        try {laceiras.addTreinador(t1); laceiras.addTreinador(t2);}
        catch (TreinadorJaExistenteException Et){}

        //GUARDA-REDES
        Jogador lgr1 = new GuardaRedes("Manel Pinto",6,20,10,46,50, 4,11,30,45,60,40);
        Jogador lgr2 = new GuardaRedes("João Fraga", 3,16,9, 56,46, 6, 7,34,48,46,70);
        Jogador lgr3 = new GuardaRedes("Tiago Silva",8,19,16,49,53,11,16,53,57,43,48);

        //DEFESAS - a partir do segundo defesa os numeros são iguais
        Jogador ldef1 = new Defesa("João Lopes",      10,40, 15, 20, 60, 10, 10, 50, 45, 10, 12);
        Jogador ldef2 = new Defesa("Ricardo Alves",   11,45, 20, 25, 65, 15, 15, 55, 50, 15, 17);
        Jogador ldef3 = new Defesa("Cristiano Maia",   5,35, 10, 15, 55,  5,  5, 45, 40,  5,  7);
        Jogador ldef4 = new Defesa("Henrique Coelho", 20,60, 35, 40, 67, 30, 34, 68, 65, 30, 32);

        //MEDIOS
        Jogador lmed1 = new Medio("Vitor Alves",       30,50, 63, 60, 70, 20, 40, 78, 80, 50, 50);
        Jogador lmed2 = new Medio("Herculano Carvalho",31,55, 68, 65, 75, 25, 45, 83, 85, 55, 55);
        Jogador lmed3 = new Medio("Paulo Medeiros",    32,45, 57, 55, 65, 15, 35, 72, 75, 45, 45);
        Jogador lmed4 = new Medio("Miguel Novais",     33,60, 73, 70, 80, 40, 50, 60, 76, 60, 60);

        //AVANÇADOS
        Jogador lavan1 = new Avancado("Eduardo Lopes",  70,50, 63, 60, 70, 20, 40, 78, 81, 79, 90);
        Jogador lavan2 = new Avancado("João Canário",   71,55, 68, 65, 75, 25, 45, 83, 50, 70, 60);
        Jogador lavan3 = new Avancado("Sérgio Carvalho",72,45, 57, 55, 65, 15, 35, 72, 51, 69, 70);
        Jogador lavan4 = new Avancado("Hélio Gustavo",  73,45, 57, 55, 65, 15, 35, 72, 56, 64, 78);
        Jogador lavan5 = new Avancado("Marco Esperança",74,60, 73, 70, 80, 40, 50, 60, 54, 75, 57);


        try{
            laceiras.addJogador(lgr1,data);  laceiras.addJogador(lgr2,data);  laceiras.addJogador(lgr3,data);
            laceiras.addJogador(ldef1,data); laceiras.addJogador(ldef2,data); laceiras.addJogador(ldef3,data); laceiras.addJogador(ldef4,data);
            laceiras.addJogador(lmed1,data); laceiras.addJogador(lmed2,data); laceiras.addJogador(lmed3,data); laceiras.addJogador(lmed4,data);
            laceiras.addJogador(lavan1,data);laceiras.addJogador(lavan2,data);laceiras.addJogador(lavan3,data);laceiras.addJogador(lavan4,data);laceiras.addJogador(lavan5,data);
        }
        catch (JogadorJaExistenteException Ej){}

        return laceiras;
    }
}
