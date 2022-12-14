import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SerializeAux {

    /**
     * Serializa um Short no formato correto
     * @param n Short Alvo
     * @return Short Serializado
     */
    public static byte[] serialize(short n){
        ByteBuffer num = ByteBuffer.allocate(Short.BYTES);
        num.putShort(n);
        return num.array();
    }

    /**
     * Serializa um Long no formato correto
     * @param n Long Alvo
     * @return Long Serializado
     */
    public static byte[] serialize(long n){
        ByteBuffer num = ByteBuffer.allocate(Long.BYTES);
        num.putLong(n);
        return num.array();
    }

    /**
     * Serializa um Int no formato correto
     * @param n Int Alvo
     * @return Int Serializado
     */
    public static byte[] serialize(int n){
        ByteBuffer num = ByteBuffer.allocate(Integer.BYTES);
        num.putInt(n);
        return num.array();
    }

    /**
     * Serializa um Float no formato correto
     * @param n Float Alvo
     * @return Float Serializado
     */
    public static byte[] serialize(float n){
        ByteBuffer num = ByteBuffer.allocate(Float.BYTES);
        num.putFloat(n);
        return num.array();
    }

    /**
     * Serializa uma String no formato correto. (UTF_8)
     * @param n String Alvo
     * @return String Serializada
     */
    public static byte[] serialize(String n){
        return n.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Deserializa uma String.
     * @param reader ByteBuffer, localizado no início da String
     * @return String lida
     */
    public static String deserialize(ByteBuffer reader){
        return StandardCharsets.UTF_8.decode(reader).toString();
    }
}
