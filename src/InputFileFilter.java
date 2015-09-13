import java.io.*;
class InputFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File fileobj){
        String extension="";
        if(fileobj.getPath().lastIndexOf('.')>0)
            extension=fileobj.getPath().substring(fileobj.getPath().lastIndexOf('.')+1).toLowerCase();
        if(extension!="")
            return (extension.equals("gif") || extension.equals("jpg") || extension.equals("bmp")||extension.equals("mp3")||extension.equals("avi"));
        else
            return fileobj.isDirectory() ;
    }
    public String getDescription() {
        return "Image, Audio & Video Files(*.jpg,*.gif,*.bmp,*.mp3,*.avi)";
    }
}
