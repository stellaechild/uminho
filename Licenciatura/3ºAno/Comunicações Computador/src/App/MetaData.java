import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MetaData {
    /** Lista de ficheiros, Cada ficheiro tem uma lista com o seu nome e última data de modificação*/
    private List<List<String>> fields;
    /** Número de ficheiros presentes na pasta atual */
    private int numberOfFiles;

    /**
     * Construtor por Omissão
     */
    public MetaData(){
        fields = new ArrayList<>();
    }

    /**
     * Método Get, Retorna a data de um dado ficheiro.
     * @param fileName Nome do ficheiro
     * @return Data Do Ficheiro
     */
    public String getDataFile(String fileName){
        for(List<String> par : fields){
            if (par.get(0).equals(fileName))
                return par.get(1);
        }
        return "";
    }

    /**
     * Método para transferência de Metadados entre clientes. Converte o objeto num array de bytes para serem enviados.
     * @return Byte Array para envio
     * @throws IOException Falha na escrita do buffer.
     */
    public byte[] serialize() throws IOException{
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        /** SERIALIZE NUMBER OF FILES */
        buff.write(SerializeAux.serialize(this.numberOfFiles));

        /** SERIALIZE EACH FILENAME */
        for(int i = 0;i<this.numberOfFiles;i++){
            buff.write(SerializeAux.serialize(this.fields.get(i).get(0)));
            buff.write(SerializeAux.serialize("\n"));
            buff.write(SerializeAux.serialize(this.fields.get(i).get(1)));
            buff.write(SerializeAux.serialize("\n"));
        }

        return buff.toByteArray();
    }

    /**
     * Método para a receção de Metadados de clientes. Converte um array de bytes no objeto Metadados para serem avaliados.
     * @param data Byte Array, posicionado no início dos metados.
     * @return Metadados lidos
     */
    public static MetaData deserialize(byte[] data){
        MetaData res = new MetaData();
        ByteBuffer buff = ByteBuffer.wrap(data);
        /** DESERIALIZE NUMBER OF FILES */
        res.numberOfFiles = buff.getInt();
        String[] seps = SerializeAux.deserialize(buff).split("\n");

        /** DESERIALIZE EACH FILENAME */
        for(int i = 0;i< res.numberOfFiles*2;i += 2){
            List<String> file = new ArrayList<>();
            file.add(seps[i]);
            file.add(seps[i+1]);
            res.fields.add(file);
        }
        return res;
    }

    /**
     * Método de comparação de metadatas. O objeto chamador da função irá realizar a comparação
     * com os metadados fornecidos e irá dar uma lista de ficheiros que este tem em falta, em relação aos fornecidos.
     * Ficheiros com o mesmo nome são comparados pela sua data de modificação dando prioridade aos mais recentes.
     * @param outside Metadados de clientes recebidos
     * @return Lista com os nomes dos ficheiros em falta
     */
    public List<String> compareMetadata(MetaData outside){
        List<String> diff = new ArrayList<>();
        if(outside == null) return diff;
        /** FOR EACH FILE ON OUTSIDE METADATA */
        for(List<String> outsideFile : outside.fields){
            boolean found = false;
            boolean add = false;
            String outsideFilename = outsideFile.get(0);
            String outsideData = outsideFile.get(1);

            /** CHECK IF VALID NAME (ILLEGAL WINDOWS CHARACTERS AND SIZE) */
            if(outsideFilename.length() < FTRP_Packet.MAXDATASIZE &&
                    (!System.getProperty("os.name").toLowerCase().contains("win") || !illegalFilenameWin(outsideFilename)))
            {
                for (List<String> insideFile : this.fields) {
                    String insideFilename = insideFile.get(0);
                    String insideData = insideFile.get(1);

                    /** IF NAME IS EQUAL, COMPARE DATE */
                    if (outsideFilename.equals(insideFilename)) {
                        int comp = outsideData.compareTo(insideData);
                        found = true;
                        if (comp > 0) add = true;
                    }
                }
                /** IF FILE NOT FOUND ON OWN METADATA, OR FOUND BUT NEEDS UPDATING, ADDS */
                if (!found || (found && add)) diff.add(outsideFilename);
            }
        }
        return diff;
    }

    /**
     * Método de Teste para nomes illegais. Windows não suporta certos nomes illegais.
     * @param file Nome do ficheiro a ser testado
     * @return True if illegal
     */
    private Boolean illegalFilenameWin(String file){
        char[] illegalChars = {'<','>',':','"','\\','|','?','*'};
        for(int i = 0;i < illegalChars.length;i++){
            String charTest = ""+illegalChars[i];
            if(file.contains(charTest)) return true;
        }
        return false;
    }

    /**
     * Função de escrita inicial de metadados. Atualiza os metadados, dado uma diretoria.
     * Este executa um comando à parte e trata os dados recebidos. Recupera os ficheiros nas subdiretorias
     * @param folder Diretoria da pasta alvo
     * @throws Exception Erros de execução e/ou escrita
     */
    public void updateMetadata(String folder) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        String out = executeLS(folder,os);

        this.fields = new ArrayList<>();
        if (os.contains("win"))
            processMetaDataWin(out);
        else
            processMetaDataLinux(out);
    }

    /**
     * Método responsável pelo processamento da informação obtida no comando de recolha de ficheiros. LINUX
     * @param out output do comando
     */
    public void processMetaDataLinux(String out){
        /** BLANK OUTPUT */
        if(out.isBlank()) {
            this.numberOfFiles = 0;
            return;
        }
        /** ERASE \r AND SPLIT \n's */
        out = out.replace("\r","");
        String[] field = out.split("\n");

        for(int i = 0; i < field.length;i++){
            /** SPLIT EACH LINE */
            String[] fileInfo = field[i].split(";");
            String filename;
            String date;

            /** IF FILE HAS ADDITIONAL SUB-DIRECTORIES, ADD THEM TO THE FILENAME */
            if(fileInfo[0].contains("/")){
                String[] paths = fileInfo[0].split("/",2);
                filename = paths[1]+"/"+fileInfo[1];
            }
            else filename = fileInfo[1];

            String[] times = fileInfo[2].split("\\.");
            date = times[0];

            /** CHECK FILE NAME SIZE, ADD */
            if(filename.length()<FTRP_Packet.MAXDATASIZE) {
                List<String> fileData = new ArrayList<>();
                fileData.add(filename);
                fileData.add(date);

                this.fields.add(fileData);
            }
        }
        this.numberOfFiles = this.fields.size();
    }

    /**
     * Método responsável pelo processamento da informação obtida no comando de recolha de ficheiros. WINDOWS
     * @param out output do comando
     */
    public void processMetaDataWin(String out){
        /** ERASE \r AND SPLIT \n's */
        out = out.replace("\r","");
        String[] field = out.split("\n");

        for(int i = 1; i < field.length;i++){
            /** SPLIT EACH LINE */
            String[] infoField = field[i].split(";");

            /** IF "IS DIRECTORY" IS FALSE, PROCESS, ELSE IGNORE */
            if(infoField[0].equals("FALSE")){
                /** REPLACE WINDOWS \ WITH / */
                String filename = infoField[1].substring(3).replace("\"","").replace("\\","/");
                String[] yearHolder = infoField[2].split("/");
                /** INVERSE DATE FORMAT DD-MM-YYYY_hh:mm:ss -> YYYY-MM-DD_hh:mm:ss */
                String data = ""+yearHolder[2]+"-"+yearHolder[1]+"-"+yearHolder[0]+"_"+infoField[3];

                /** CHECK FILE NAME SIZE, ADD */
                if(filename.length()<FTRP_Packet.MAXDATASIZE) {
                    List<String> fileData = new ArrayList<>();
                    fileData.add(filename);
                    fileData.add(data);

                    this.fields.add(fileData);
                }
            }
        }
        this.numberOfFiles = this.fields.size();
    }

    /**
     * Comando privado, responsável pela execução do comando "ls" e retorno da informação obtida
     * @param folder Diretoria alvo
     * @param os Sistema Operativo
     * @return Output do comando "ls"
     * @throws Exception Erro de Execução do comando
     */
    private String executeLS(String folder,String os) throws Exception {

        /** EXECUTE COMMAND, WINDOWS OR LINUX */
        Process p;
        if(os.contains("win")) {
            folder = folder.replace("/","");
            p = Runtime.getRuntime().exec("cmd /c forfiles /P "+folder+" /S /c \"cmd /c echo @isdir;@relpath;@fdate;@ftime\"");
        }
        else p = Runtime.getRuntime().exec("find "+folder+" -type f -printf %h;%f;%TY-%Tm-%Td_%TT\\n");
        p.waitFor();

        /** GET OUTPUT STRING */
        DataInputStream input = new DataInputStream(p.getInputStream());
        ByteBuffer bb = ByteBuffer.wrap(input.readAllBytes());

        /** CONVERT DOS FORMAT TO UTF-8 */
        if(os.contains("win")) {
            CharBuffer cs = Charset.forName("IBM850").decode(bb);
            bb = Charset.forName("UTF-8").encode(cs);
        }
        return SerializeAux.deserialize(bb);
    }

}
