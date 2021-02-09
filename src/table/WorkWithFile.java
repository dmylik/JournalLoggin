package table;
import java.io.*;

public class WorkWithFile {
    public static String read(String fileName) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        ifExists(fileName);

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public static String readByte(String fileName) throws IOException {
        InputStream in = new FileInputStream(fileName);

        if (in.available() > 0) {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            return new String(buffer, "utf-8");
        }

        throw new IOException("НЕ УДАЛОСЬ ПРОЧИТАТЬ СОДЕРЖИМОЕ ФАЙЛА");
    }

    private static void ifExists(String fileName) throws FileNotFoundException {
        File queryIn = new File(fileName);
        if (!queryIn.exists()){
            throw new FileNotFoundException(queryIn.getName());
        }
    }
}
