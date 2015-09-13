import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Decrypt{
    private int val;
    private String message,cpyfile;
    private FileReader fr;
    private FileWriter fw;
    private File file1, file2,file3;


    public boolean decryptFile(byte key, String datafile) {
        cpyfile = datafile + "cpy";
        file1=new File(datafile) ;
        file2=new File(cpyfile) ;
        try {
            fr = new FileReader(file1);
            fw = new FileWriter(file2);
            byte n = key;
            while ((val = fr.read()) != -1) {
                val = (short) (val - n);
                if (val < 0)
                    fw.write(126 + val);
                else
                    fw.write(val);
                n++;
                if (n > 126) n = 0;
            }
        } catch (Exception e) {
            message = "Oops! error in decryption\n" + e.toString();
            return false;
        } finally {
            try {
                fr.close();
                fw.close();
                file3=file1;
                file1.delete() ;
                file2.renameTo(file3);
            } catch (Exception ex) {
                message = "Oops! error in decryption final \n" + ex.toString();
                return false;
            }
        }
        message="Decrypted";
        return true;
    }

    public String decryptMessage(byte key, String msg) {
        int n = key;
        this.message = msg;
        char c[] = new char[message.length()];
        for (int i = 0; i < message.length(); i++) {
            val = (int) message.charAt(i);
            val = val - n;
            if (val < 0)
                c[i] = (char) (126 + val);
            else
                c[i] = (char) val;
            n++;
            if (n > 126) n = 0;
        }
        message = new String(c);
        return message;
    }

}
