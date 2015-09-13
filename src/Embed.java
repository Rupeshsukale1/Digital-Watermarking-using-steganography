import java.io.*;

class Embed{
    Encrypt enc;
    Decrypt dec;
    DataInputStream dis, data;
    DataOutputStream dos;
    FileWriter fw;
    byte byt, mbyte, filebyte, key;
    short msgsize, size;
    int i, j, filesize;
    String mesg, message, cpyfile;
    File file1, file2, file3;
    final static int HEADER = 128;


    public String getMessage() {
        return message;
    }

    public boolean encodeMessage(String infile, String mesg) {
        file1 = new File(infile);
        cpyfile = infile + "cpy";
        file2 = new File(cpyfile);
        key = (byte) (126 * Math.random());
        enc = new Encrypt();
        mesg = enc.encryptMessage(key, mesg);
        try {
            fw = new FileWriter(file2);
            fw.close();
            dis = new DataInputStream(new FileInputStream(file1));
            dos = new DataOutputStream(new FileOutputStream(file2));

            for (int i = 0; i <= HEADER; i++)
                dos.writeByte(dis.readByte());

            //Embed the message length size
            msgsize = (short) mesg.length();
            for (int i = 14; i >= 0; i -= 2) {
                size = msgsize;
                size >>= i;
                mbyte = (byte) size;
                mbyte &= 0x03;
                filebyte = dis.readByte();
                filebyte &= 0xFC;
                filebyte |= mbyte;
                dos.writeByte(filebyte);
            }

            //Embed key
            for (int j = 6; j >= 0; j -= 2) {
                mbyte = key;
                mbyte >>= j;
                mbyte &= 0x03;
                filebyte = dis.readByte();
                filebyte &= 0xFC;
                filebyte |= mbyte;
                dos.writeByte(filebyte);
            }

            //Embed actual message
            System.out.println(mesg.length());
            //Embed the data file name
           for (i = 0; i < mesg.length(); i++) {
               byt = (byte) mesg.charAt(i);
               for (j = 6; j >= 0; j -= 2) {
                   mbyte = byt;
                   mbyte >>>= j;
                   //mbyte &= 0x03;
                   filebyte = dis.readByte();
                   filebyte &= 0xFC;
                   filebyte |= mbyte;
                   dos.writeByte(filebyte);
               }
           }


            while (true) {
                byte fb = dis.readByte();
                dos.writeByte(fb);
            }
        } catch (EOFException eof) {} catch (Exception e) {
            message = "Oops!!\nError: " + e.toString();
            return false;
        } finally {
            try {
                dos.close();
                dis.close();
                dos = new DataOutputStream(new FileOutputStream(file1));
                dis = new DataInputStream(new FileInputStream(file2));
                while (true)
                    dos.writeByte(dis.readByte());
            } catch (EOFException eof) {
                try {
                    dos.close();
                    dis.close();
                    file2.delete() ;
                } catch (Exception ex) {
                    message = "Oops!!\nError: " + ex.toString();
                }
            } catch (Exception fe) {
                message = "Oops!!\nError: " + fe.toString();
                return false;
            }
        }
        message = "Message Embeded successfully";
        return true;
    }

    public boolean encodeFile(String infile, String datafile) {
        file1 = new File(infile);
        cpyfile = infile + "cpy";
        file2 = new File(cpyfile);
        key = (byte) (126 * Math.random());
        enc = new Encrypt();
        File encFile=enc.encryptFile(key, datafile);
        try {
            fw = new FileWriter(file2);
            fw.close();
            dis = new DataInputStream(new FileInputStream(file1));
            dos = new DataOutputStream(new FileOutputStream(file2));
            data = new DataInputStream(new FileInputStream(encFile));

            for (int i = 0; i <= HEADER; i++)
                dos.writeByte(dis.readByte());

            File msgfile = new File(datafile);
            String filename = msgfile.getName();
            msgsize = (short) filename.length();

            //Embed the data file name size
            for (i = 6; i >= 0; i -= 2) {
                size = msgsize;
                size >>= i;
                mbyte = (byte) size;
                mbyte &= 0x03;
                filebyte = dis.readByte();
                filebyte &= 0xFC;
                filebyte |= mbyte;
                dos.writeByte(filebyte);
            }

            //Embed the data file length
            filesize = (int) msgfile.length();
            for (i = 30; i >= 0; i -= 2) {
                j = filesize;
                j >>= i;
                mbyte = (byte) j;
                mbyte &= 0x03;
                filebyte = dis.readByte();
                filebyte &= 0xFC;
                filebyte |= mbyte;
                dos.writeByte(filebyte);
            }

            //Embed the key
            for (int j = 6; j >= 0; j -= 2) {
                mbyte = key;
                mbyte >>= j;
                mbyte &= 0x03;
                filebyte = dis.readByte();
                filebyte &= 0xFC;
                filebyte |= mbyte;
                dos.writeByte(filebyte);
            }

            //Embed the data file name
            for (i = 0; i < filename.length(); i++) {
                byt = (byte) filename.charAt(i);
                for (j = 6; j >= 0; j -= 2) {
                    mbyte = byt;
                    mbyte >>= j;
                    mbyte &= 0x03;
                    filebyte = dis.readByte();
                    filebyte &= 0xFC;
                    filebyte |= mbyte;
                    dos.writeByte(filebyte);
                }
            }

            //Embed the content of the data file
            for (i = 0; i < filesize; i++) {
                mbyte = data.readByte();
                for (j = 6; j >= 0; j -= 2) {
                    byt = mbyte;
                    byt >>= j;
                    byt &= 0x03;
                    filebyte = dis.readByte();
                    filebyte &= 0xFC;
                    filebyte |= byt;
                    dos.writeByte(filebyte);
                }
            } while (true) {
                byte fb = dis.readByte();
                dos.writeByte(fb);
            }
        }
        catch (EOFException eof) {}
        catch (Exception e) {
            message = "Oops!!\nError: " + e.toString();
            return false;
        }
        finally {
            try {
                dos.close();
                dis.close();
                data.close() ;
                System.out.println("tem enc data del:"+encFile.delete()) ;
                dos = new DataOutputStream(new FileOutputStream(file1));
                dis = new DataInputStream(new FileInputStream(file2));
                while (true){
                    dos.writeByte(dis.readByte());
                }
            } catch (EOFException eof) {
                try {
                    dos.close();
                    dis.close();
                    System.out.println("tem img del:"+file2.delete()) ;
                } catch (Exception ex) {
                    message = "Oops!!\nError: " + ex.toString();
                }
            } catch (Exception fe) {
                message = fe.toString();
                return false;
            }
        }
        message = "Message Embeded successfully";
        return true;
    }
}

