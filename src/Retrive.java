import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Retrive{
    Decrypt d = new Decrypt();
    DataInputStream dis;
    DataOutputStream dos;
    Object[] ob = new Object[2];
    String filename,file, message, path = null;
    char mes[];
    int i, j, filesize;
    byte byt, filebyte, key;
    final static int HEADER = 128;
    public JFileChooser filechoose = new JFileChooser();
    public JOptionPane jop = new JOptionPane();
    RetriveFile retFile;
    short msgsize,temp;
    public Retrive(){

    }
    public Retrive(RetriveFile retFile){
        this.retFile =retFile;
    }
    public String getMessage() {
        return message;
    }
    public boolean retriveMessage(String file){
        this.file = file;
        try {
            dis = new DataInputStream(new FileInputStream(file));
            for (int i = 0; i <= HEADER; i++) {
                dis.readByte();
            }
            msgsize = 0;

            //Retrive message length.
            for (int i = 14; i >= 0; i -= 2) {
                filebyte = dis.readByte();
                temp = (short) filebyte;
                temp &= 0x03;
                temp <<= i;
                msgsize |= temp;
            }
            if (msgsize <= 0) {
                message = "File does not contain any message";
                return false;
            }
            key = 0;
            for (int j = 6; j >= 0; j -= 2) {
                filebyte = dis.readByte();
                filebyte &= 0x03;
                filebyte <<= j;
                key |= filebyte;
            }
            mes = new char[msgsize];
            for (int i = 0; i < msgsize; i++) {
                byt = 0;
                for (int j = 6; j >= 0; j -= 2) {
                    filebyte = dis.readByte();
                    filebyte &= 0x03;
                    filebyte <<= j;
                    byt |= filebyte;
                }
                mes[i] = ((char) byt);
            }
            Decrypt d = new Decrypt();
            message = d.decryptMessage(key, new String(mes));
        } catch (EOFException eof) {} catch (Exception e) {
            message = e.toString();
            return false;
        } finally {
            try {
                dis.close();
            } catch (Exception fe) {
                message = fe.toString();
                return false;
            }
        }
        return true;
    }


    public String retriveFile(String inputfile) {
        try {
            dis = new DataInputStream(new FileInputStream(inputfile));
            for (int i = 0; i <= HEADER; i++)
                dis.readByte();

            //Retrive filename size into byte variable
            byt = 0;
            for (i = 6; i >= 0; i -= 2) {
                filebyte = dis.readByte();
                filebyte &= 0x03;
                filebyte <<= i;
                byt |= filebyte;
            }
            byte filenamesize = byt;

            //Retrive the data file length
            filesize = 0;
            for (i = 30; i >= 0; i -= 2) {
                filebyte = dis.readByte();
                j = (int) filebyte;
                j &= 0x00000003;
                j <<= i;
                filesize |= j;
            }

            //Retrive the key
            key = 0;
            for (j = 6; j >= 0; j -= 2) {
                filebyte = dis.readByte();
                filebyte &= 0x03;
                filebyte <<= j;
                key |= filebyte;
            }

            //Retrive name of the data file
            mes = new char[filenamesize];
            for (i = 0; i < filenamesize; i++) {
                byt = 0;
                for (j = 6; j >= 0; j -= 2) {
                    filebyte = dis.readByte();
                    filebyte &= 0x03;
                    filebyte <<= j;
                    byt |= filebyte;
                }
                mes[i] = (char) byt;
            }
            filename = new String(mes);

            boolean val = true;
            while (val) {
                val = inform();
                if (val == true) {
                    int result = JOptionPane.showConfirmDialog(retFile,
                            "This will cancel the retrive operation\n" +
                            "Do you want to cancel this operation",
                            "Operation cancelation",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        message = "Operation aborted by user";
                        return "Failed";
                    } else if (result == JOptionPane.NO_OPTION)
                        val = true;
                }
            }

            dos = new DataOutputStream(new FileOutputStream(path + "/" +
                    filename));
            //Write contents retrived to the data file
            for (i = 0; i < filesize; i++) {
                byt = 0;
                for (j = 6; j >= 0; j -= 2) {
                    filebyte = dis.readByte();
                    filebyte &= 0x03;
                    filebyte <<= j;
                    byt |= filebyte;
                }
                dos.writeByte(byt);
            }

        } catch (EOFException eof) {} catch (Exception e) {
            message = "Error :" + e.toString();
            return "Failed";
        } finally {
            try {
                dis.close();
                dos.close();
            } catch (Exception ex) {
                message = "Oops!!\nError: " + ex.toString();
                return "Failed";
            }
        }
        message = path + "/" + filename;
        d.decryptFile(key, message);
        return message;
    }
    public boolean inform() {
        Object[] ob = new Object[2];
        ob[0] = "Select directory for saving the file to be retrived";
        ob[1] = new JTextField(path, 20); //jtf;//
        JButton jb = new JButton("Browse");
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            }
        });
        String[] but = new String[3];
        but[0] = "Browse";
        but[1] = "OK";
        but[2] = "Cancel";
        while (true) {
            int result = JOptionPane.showOptionDialog(
                    retFile,					// the parent that the dialog blocks
                    ob,									// the dialog message array
                    "Directory Selection",				// the title of the dialog window
                    JOptionPane.DEFAULT_OPTION,			// option type
                    JOptionPane.INFORMATION_MESSAGE,	// message type
                    null,								// optional icon, use null to use the default icon
                    but,								// options string array, will be made into buttons
                    but[2]								// option that should be made into a default button
                         );
            switch (result) {

            case 0:		// Browse
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showDialog((Component)null, "Attach");
                File file = fc.getSelectedFile();
                if (file != null && returnVal == JFileChooser.APPROVE_OPTION)
                    path = file.getAbsolutePath();
                ob[1] = new JTextField(path);
                break;
            case 1:		// OK
                JTextField jtf=new JTextField() ;
                jtf=(JTextField)ob[1];
                path=jtf.getText();
                if (path.length()<=0) {
                    JOptionPane.showMessageDialog(retFile,
                                                  "Please select the path for saving the file" +
                                                  "\nPath field empty !",
                                                  "Empty path field",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                    break;
                }
                else
                    return false;
            default:	//Cancel
                return true;
            }
        }
    }
}
