import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
public class Encrypt{
    private int val;
    private String message,cpyfile;
    private FileReader fr;
    private FileWriter fw;
    private File file1,file2;

    public File encryptFile(byte key, String infile){
        try {
            cpyfile=infile+"cpy";
            file1=new File(infile) ;
            file2=new File(cpyfile) ;
            fr = new FileReader(file1);
            fw=new FileWriter(file2) ;
            byte n = key;
            while((val=fr.read())!=-1){
                val=val+n;
                if ((126 - val) < 0)
                    fw.write(Math.abs(126 - val));
                else
                    fw.write(val);
                n++;
                if (n > 126) n = 0;
            }
        }
        catch(Exception e){
            message="Oops! error in encryption\n" + e.toString() ;
            return null;
        }
        finally{
            try{
                fr.close();
                fw.close();
            }
            catch(Exception ex){
                message="Oops! error in encryption process \n" + ex.toString() ;
                return null;
            }
        }
        message="encypted";
        return file2;
    }

    public String encryptMessage(byte key, String msg) {
        int n = key;
        this.message = msg;
        char c[] = new char[message.length()];
        for (int i = 0; i < message.length(); i++) {
            val = (int) message.charAt(i);
            val = val + n;
            if ((126 - val) < 0)
                c[i] = (char) (Math.abs(126 - val));
            else
                c[i] = (char) val;
            n++;
            if (n > 126) n = 0;
        }
        message = new String(c);
        return message;
    }
}
