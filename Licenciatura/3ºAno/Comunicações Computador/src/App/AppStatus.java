import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class AppStatus {

    /** Mapa das conexões */
    private Map<String,ConnectionInfo> connections;
    /** Lock de concorrência */
    private ReentrantLock lock;

    /**
     * Enumeração dos vários estados possíveis
     */
    public enum Estados {Initializing,Listening,Working,Sleeping,SendingData,ReceivingData,Retransmission,Success,Fail,NoConfirmation,Connecting,Updating,NotCreated}

    /**
     * Construtor por omissão
     */
    AppStatus(){
        connections = new HashMap<>();
        lock = new ReentrantLock();
    }

    /**
     * Método de adição de entradas no AppStatus
     * @param key Chave de acesso
     * @param estado Estado
     * @param thread Nome da Thread
     * @param info Informação adicional
     */
    public void addEntry(String key,Estados estado,String thread,String info){
        try{
            lock.lock();
            ConnectionInfo con = new ConnectionInfo(key,estado.toString(),thread,info);
            connections.put(key,con);
        } finally{lock.unlock();}

    }

    /**
     * Método de atualização do estado de uma conexão
     * @param key Chave da conexão
     * @param estado Estado a atualizar
     */
    public void updateEntry(String key, Estados estado){
        try{
            lock.lock();
            connections.get(key).updateEstado(estado.toString());
        } finally{lock.unlock();}
    }

    /**
     * Método para criação de "chaves" para aceder ao estado de cada thread.
     * @param ip Ip de conexão
     * @param Server Flag Servidor ou Cliente
     * @param data Flag Transferência de Ficheiro
     * @param num Número de Transferencia
     * @return Key para aceder a AppStatus
     */
    public static String getKey(InetAddress ip,Boolean Server,Boolean data,int num){
        String key = ""+ip.toString()+"-";
        if (Server) key += "S";
        else key += "C";

        if (data) key += "-DT"+num;
        return key;
    }

    /**
     * Método para converter o objeto "AppStatus" no formato HTML. Começa por listar as threads de atendimento, de seguida as threads
     * Servidor e Cliente e por último as transferências.
     * @return String HTML.
     */
    public String toHtml(){
        try {
            lock.lock();
            String res = "";
            res += "<br>";

            /** FORMATA THREAD DE ATENDIMENTO */
            res += "<p>"+connections.get("HTTP").toHtml()+"</p>";
            res += "<p>"+connections.get("Service Socket").toHtml()+"</p>";

            /** FORMATA THREADS CLIENTE-SERVIDOR */
            res+= "<div class=\"subtitulo\"> Estado Servidores-Clientes </div>";
            res += "<br>";
            for(String key : this.connections.keySet()){
                String[] dt = key.split("-");
                if (dt.length == 2){
                    res += "<p>"+connections.get(key).toHtml()+"</p>";
                }
            }

            /** FORMATA THREADS DE TRANSFERÊNCIA */
            res += "<div class=\"subtitulo\"> Estado Transmissões </div>";
            res += "<br>";

            for(String key : this.connections.keySet()){
                String[] dt = key.split("-");
                if (dt.length == 3){
                    res += "<p>"+connections.get(key).toHtml()+"</p>";
                }
            }

            return res;
        } finally {lock.unlock();}
    }

    private class ConnectionInfo{
        /** Chave de acesso */
        private String key;
        /** Estado */
        private String estado;
        /** Nome da Thread */
        private String thread;
        /** Informação adicional */
        private String info;

        /**
         * Construtor de conexão
         * @param key Chave de acesso
         * @param es Estado
         * @param th Nome da Thread
         * @param in Informação adicional
         */
        ConnectionInfo(String key,String es,String th,String in){
            this.key = key;
            this.estado = es;
            this.thread = th;
            this.info = in;
        }

        /**
         * Método para atualizar o Estado na conexão
         * @param estado
         */
        public void updateEstado (String estado){
            this.estado = estado;
        }
        public void updateThread (String thread){
            this.thread = thread;
        }

        /**
         * Método para criação de "chaves" para aceder ao estado de cada thread.
         * @return
         */
        public String keyToString(){
            String res = "";
            if (this.key.equals("HTTP") ) return "HTTP";
            if (this.key.equals("Service Socket")) return "Service Socket";
            String[] split = this.key.split("-");

            if(split[1].equals("C")) res += "Receiving from IP ";
            else res+= "Sending to IP ";

            res += split[0];

            if(split.length == 3) res+= " (File)";

            return res;
        }


        /**
         * Método para converter uma conexão em formato objeto no formato HTML.
         * @return String HTML.
         */
        public String toHtml(){
            String res = "<b>"+this.keyToString()+":</b> <a>"+this.thread+" | Info: "+this.info+" | Estado: ";

            /** GREEN COLOR STATUS */
            if(this.estado.equals(Estados.Success.toString()) ||
                    this.estado.equals(Estados.Sleeping.toString()) ||
                    this.estado.equals(Estados.Listening.toString())) {
                res += "<g>" + this.estado + "</g>";
            }
            /** RED COLOR STATUS */
            else if(this.estado.equals(Estados.Fail.toString()) ||
                    this.estado.equals(Estados.NotCreated.toString())){
                res += "<r>" + this.estado + "</r>";
            }
            /** ELSE YELLOW */
            else {
                res += "<y>" + this.estado + "</y>";
            }

            res +="</a>";
            return res;
        }
    }
}
