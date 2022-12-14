import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWorker implements Runnable {

    /** Transmissor */
    private SRVTransmitter transmitter;
    /** Registo de Voos e Reservas */
    private SRVRegistry srvRegistry;
    /** Registo de Utilizadores */
    private UserRegistry userRegistry;
    /** Registo de Log */
    private ServerLog log;

    /**
     * Construtor da Thread simples
     * @param s Socket a utilizar
     * @param srvRegistry Registo de Voos e Reservas
     * @param userRegistry Registo de Utilizadores
     */
    public ServerWorker(Socket s,SRVRegistry srvRegistry,UserRegistry userRegistry,ServerLog log){
        this.transmitter = new SRVTransmitter(s);
        this.srvRegistry = srvRegistry;
        this.userRegistry = userRegistry;
        this.log = log;
    }

    /**
     * Método Run da Thread. Apenas recebe packets e procesa a sua resposta.
     */
    public void run() {

        while (true){

            try {
                /** Recebe a packet */
                SRVPacket packet = transmitter.readPacket();
                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+packet.getCommand().toString());

                /** Processa dependendo do comando */
                switch (packet.getCommand()) {

                    /** COMANDO LOGIN */
                    case Login:
                        try {
                            /** Efetua Login e envia uma confirmação */
                            String id = userRegistry.login(packet.getArgumentIdx(0), packet.getArgumentIdx(1));
                            log.logAndPrintMessage("User Logged in, ID: "+id);
                            transmitter.writePacket(SRVPacket.confirma(id, packet.getCommand(), new ArrayList<>()));
                        }
                        /** Caso a informação de login esteja incorreta */
                        catch (InvalidLoginException e) {
                            log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                            transmitter.writePacket(SRVPacket.erro("Nxxxxx", SRVPacket.Erros.ErroGenerico, "Informação de Login inválida!"));
                        }
                        break;

                    /** COMANDO LISTAR VOOOS */
                    case ListarVoos:
                        /** Verifica a validade da sessão */
                        if(checkLogin(packet.getIdSession())){
                            try{
                                /** Obtém a informação dos voos pedidos e envia confirmação*/
                                List<String> res = srvRegistry.infoAllFlightsFromTo(packet.getArgumentIdx(0),packet.getArgumentIdx(1));
                                transmitter.writePacket(SRVPacket.confirma(packet.getIdSession(),packet.getCommand(),res));
                            }
                            /** Caso não haja voos */
                            catch (NoFlightsAvailableException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.SemVoos);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.SemVoos,""));
                            }
                        }
                        break;


                    case ReservarVoo:
                        /** Verifica a validade da sessão */
                        if(checkLogin(packet.getIdSession())){
                            try{
                                /** Encontra o nome do utilizador responsável pela sessão, necessário para efetuar reservas */
                                String username = userRegistry.getUsername(packet.getIdSession());
                                /** Processa os argumentos */
                                List<String> args = packet.getArgumentsList();
                                List<String> route = new ArrayList<>();
                                for(int i = 3;i < args.size();i++)
                                    route.add(args.get(i));

                                /** Gera a reserva */
                                String revId= srvRegistry.reservarVoo(username,args.get(0),args.get(1),args.get(2),route);

                                /** Processa e envia a confirmação */
                                List<String> output = new ArrayList<>();
                                output.add(revId);
                                transmitter.writePacket(SRVPacket.confirma(packet.getIdSession(),packet.getCommand(),output));
                            }
                            /** Caso não haja voos disponíveis */
                            catch (NoFlightsAvailableException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.SemVoos);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.SemVoos,"Sem voos"));
                            }
                            /** Caso o dia já esteja encerrado */
                            catch (DayClosedException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.SemVoos);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.SemVoos,"Dia Encerrado"));
                            }
                            /** Caso o ID da sessão seja inválido */
                            catch (InexistentSessionException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.ErroGenerico,"Unexpected Error"));
                            }
                        }
                        break;


                    case CancelarReserva:
                        /** Verifica a validade da sessão */
                        if(checkLogin(packet.getIdSession())){
                            try{
                                /** Encontra o nome do utilizador responsável pela sessão, necessário para efetuar reservas */
                                String username = userRegistry.getUsername(packet.getIdSession());
                                /** Cancela a reserva e confirma */
                                srvRegistry.cancelReservation(username,packet.getArgumentIdx(0));
                                transmitter.writePacket(SRVPacket.confirma(packet.getIdSession(),packet.getCommand(),new ArrayList<>()));
                            }
                            /** Caso a reserva seja inválida */
                            catch (InexistentReservation e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.ErroGenerico,"Reserva inválida"));
                            }
                            /** Caso o dia já esteja encerrado */
                            catch (DayClosedException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.ErroGenerico,"Dia Encerrado"));
                            }
                            /** Caso o ID da sessão seja inválido */
                            catch (InexistentSessionException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.ErroGenerico,"Unexpected Error"));
                            }
                        }
                        break;


                    case ListarReservas:
                        /** Verifica a validade da sessão */
                        if(checkLogin(packet.getIdSession())){
                            try{
                                /** Encontra o nome do utilizador responsável pela sessão, necessário para efetuar a listagem de reservas */
                                String username = userRegistry.getUsername(packet.getIdSession());
                                transmitter.writePacket(SRVPacket.confirma(packet.getIdSession(),packet.getCommand(),this.srvRegistry.infoAllReservationsUsername(username)));
                            }
                            catch (InexistentSessionException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.ErroGenerico,"Unexpected Error"));
                            }
                        }
                        break;


                    case NovoVoo:
                        /** Verifica a validade da sessão */
                        if(checkLogin(packet.getIdSession())){
                            /** Verifica privilégios de administrador e envia erro caso não possua */
                            if(!packet.getIdSession().contains("A")) {
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.SemPermissao);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.SemPermissao, "Não possui privilégios de administrador"));
                            }
                            try{
                                /** Adiciona voo e confirma */
                                srvRegistry.addFlight(packet.getArgumentIdx(0),packet.getArgumentIdx(1),packet.getArgumentIdx(2),Integer.parseInt(packet.getArgumentIdx(3)));
                                transmitter.writePacket(SRVPacket.confirma(packet.getIdSession(),packet.getCommand(),new ArrayList<>()));
                            }
                            /** Caso algum argumento seja inválido (Capacidade) */
                            catch (IllegalArgumentException e){
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.ErroGenerico,"Erro na inserção"));
                            }
                        }
                        break;


                    case EncerrarDia:
                        /** Verifica a validade da sessão */
                        if(checkLogin(packet.getIdSession())){
                            /** Verifica privilégios de administrador e envia erro caso não possua */
                            if(!packet.getIdSession().contains("A")) {
                                log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.SemPermissao);
                                transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.SemPermissao, "Não possui privilégios de administrador"));
                            }
                            /** Encerra o dia */
                            srvRegistry.closeDay(packet.getArgumentIdx(0));
                            transmitter.writePacket(SRVPacket.confirma(packet.getIdSession(),packet.getCommand(),new ArrayList<>()));
                        }
                        break;

                    default:
                        /** Erro inesperado */
                        log.logAndPrintMessage("ID:"+packet.getIdSession()+" $ "+SRVPacket.Erros.ErroGenerico);
                        transmitter.writePacket(SRVPacket.erro(packet.getIdSession(), SRVPacket.Erros.ErroGenerico,"Unexpected Error"));
                        break;
                }

            }

            /** Packet lida inválida/Corrumpida */
            catch (IOException e){
                return;
            }
            catch (InvalidSRVPacketException e){
                log.logAndPrintMessage("Invalid/Corrupted Packet");
            }

        }
    }

    /**
     * Método auxiliar para verificação de sessão válida.
     * Caso seja inválido envia automaticamente uma packet de erro.
     * @param idSession ID de sessão
     * @return boolean - true se válido
     * @throws IOException Erro de IO na escrita da packet.
     */
    private boolean checkLogin(String idSession) throws IOException{
        try{
            userRegistry.checkAndUpdateSession(idSession);
            return true;
        } catch (InexistentSessionException e){
            log.logMessage("User Session Expired: "+idSession);
            transmitter.writePacket(SRVPacket.erro(idSession, SRVPacket.Erros.NaoAutenticado,""));
            return false;
        }
    }
}
