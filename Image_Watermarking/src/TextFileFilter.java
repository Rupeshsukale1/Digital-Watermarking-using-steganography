import java.io.*;
class TextFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File fileobj){
        String extension="";
        if(fileobj.getPath().lastIndexOf('.')>0)
            extension=fileobj.getPath().substring(fileobj.getPath().lastIndexOf('.')+1).toLowerCase();
        if(extension!="")
            return extension.equals("txt") ;
        else
            return fileobj.isDirectory() ;
    }
    public String getDescription() {
        return "Text Files(*.txt)";
    }
}
