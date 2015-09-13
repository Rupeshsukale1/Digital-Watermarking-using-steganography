import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameAdapter;

@SuppressWarnings("serial")
class Sender extends JInternalFrame implements ActionListener {
    private static final int portNumber=8000;
    private static Socket connection = null;
    private static ObjectOutputStream objectOutStream = null;
    private static ObjectInputStream objectInStream = null;
    private JLabel label[]=new JLabel[4];
    private JTextField sendField,fileField;
    private JButton button[]=new JButton[4];
    private JFileChooser filechoose = new JFileChooser();
    private JSeparator sep1=new JSeparator(),sep2=new JSeparator();
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private InetAddress ia;
    private File file=null;
    private Image_Watermark steg;

    public static void main (String [] args) throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        Sender s=new Sender((File)null,null);
        s.setVisible(true) ;
        s.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public Sender (File file,Image_Watermark stegano) {
        super("Send File",
              false,           //resizable
              true,            //closable
              false,            //maximizable
              true);           //iconifiable
        steg = stegano;
        steg.win5_live = true;
        this.file =file;
        setFrameIcon(new ImageIcon("resource/send.gif"));
        setSize(600, 350);
        setLocation(10,10) ;
        sendField = new JTextField(30);
        if(file!=null)
            fileField = new JTextField(file.getAbsolutePath(),30);
        else
            fileField = new JTextField(30);
        label[0] = new JLabel("Send File");
        label[1] = new JLabel("Send To :");
        label[2] = new JLabel("File :");
        button[0] = new JButton("Browse");
        button[1] = new JButton("Send");
        button[2] = new JButton("Reset");
        button[3] = new JButton("Close");
        button[0].setToolTipText("Click to browse the file you want to select");
        button[1].setToolTipText("Click to send file");
        button[2].setToolTipText("Click to clear all text fields");
        button[3].setToolTipText("Click to close the Form");
        for(int i=1;i<3;i++){
            label[i].setFont(new Font("Times-Roamn", Font.CENTER_BASELINE, 14));
            label[i].setForeground(Color.BLUE);
        }
        for (int i = 0; i < 4; i++) {
            button[i].addActionListener(this);
        }
        sendField.setFont(new Font("Courier", Font.PLAIN, 14));
        sendField.setForeground(new Color(0, 0, 255));
        sendField.setBorder(BorderFactory.createLineBorder(Color.RED));
        fileField.setFont(new Font("Courier", Font.PLAIN, 14));
        fileField.setForeground(new Color(0, 0, 255));
        fileField.setBorder(BorderFactory.createLineBorder(Color.RED));

        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        JPanel jp = new JPanel();
        jp.setBackground(new Color(218, 201, 233));
        jp.setBorder(BorderFactory.createRaisedBevelBorder());
        jp.setLayout(gbl);

        //Add heading to the form
        label[0].setFont(new Font("Times-Roamn", Font.BOLD, 24));
        label[0].setForeground(Color.RED);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(label[0], gbc);

        //Constraints for Seprator
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(sep1, gbc);

        //Add file label,textfields and browse button
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10);
        jp.add(label[1], gbc);

        gbc.insets = new Insets(10, 0, 10, 50);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(sendField, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10);
        jp.add(label[2], gbc);

        gbc.gridwidth = 2 ;
        gbc.insets = new Insets(10, 0, 10, 10);
        jp.add(fileField, gbc);

        gbc.insets = new Insets(10, 10, 10, 50);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(button[0], gbc);


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 0, 50);
        jp.add(sep2, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        buttonPanel.setBackground(new Color(218, 201, 233));
        buttonPanel.add(button[1]);
        buttonPanel.add(button[2]);
        buttonPanel.add(button[3]);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(buttonPanel, gbc);

        Container cp = getContentPane();
        cp.setBackground(new Color(218, 201, 233));
        cp.setLayout(gbl);
        gbc.insets = new Insets(5, 10, 5, 10);
        cp.add(jp, gbc);
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                steg.win5_live = false;
            }
        });
    }

    public void actionPerformed (ActionEvent ae) {
        byte[] array = null;
        if (ae.getSource() == button[0]) {
            if(filechoose.showOpenDialog(this) == JFileChooser .APPROVE_OPTION ){
                file = filechoose.getSelectedFile();
                fileField.setText(file.getAbsolutePath());
            }
        }
        if (ae.getSource() == button[1]||ae.getSource() == steg.toolbutton[7]) {
            if(validInput()){
                String fileName = file.getName() ;
                try {
                    connection = new Socket(sendField.getText(), portNumber);
                    objectOutStream = new ObjectOutputStream(connection.getOutputStream());
                    objectOutStream.flush();
                    objectInStream = new ObjectInputStream(connection.getInputStream());
                    FileInputStream fileInStream = new FileInputStream(file);
                    int size = fileInStream.available();
                    array = new byte[size];
                    fileInStream.read(array, 0, size);
                    objectOutStream.writeObject("file");
                    objectOutStream.flush();
                    objectOutStream.writeObject(fileName);
                    objectOutStream.flush();
                    String result = new String("ACCEPT");
                    if (result.equals((String) objectInStream.readObject())){
                        objectOutStream.writeObject(array);
                        objectOutStream.flush();
                        JOptionPane.showInternalMessageDialog(steg.desk, "File Sent! ",
                                "Confirmation",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                        JOptionPane.showInternalMessageDialog(steg.desk,
                                "File rejected by reciever ", "Confirmation",
                                JOptionPane.INFORMATION_MESSAGE);
                    objectInStream.close() ;
                    objectOutStream.close() ;
                } catch (ConnectException ce) {
                    JOptionPane.showInternalMessageDialog(steg.desk,
                            "Cannot find the path for the " +
                                                  sendField.getText(),
                                                  "Unknown Host",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                } catch (UnknownHostException uhe) {
                    JOptionPane.showInternalMessageDialog(steg.desk,
                            "Cannot find the path for the " +
                                                  sendField.getText(),
                                                  "Unknown Host",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                }
                catch(Exception ex){
                    System.out.println(ex.toString());
                }
            }
        }
        else if(ae.getSource() == button[2]) {
            sendField.setText("") ;
            fileField.setText("") ;
        }
        else if(ae.getSource() == button[3]) {
            steg.win5_live = false;
            this.dispose() ;
        }
    }

    public void setFile(File file){
        this.file = file;
        fileField.setText(file.getAbsolutePath());
    }
    private boolean validInput(){
        try{
            ia = InetAddress.getLocalHost();
            if(ia.equals(InetAddress.getByName(sendField.getText()))){
                JOptionPane.showInternalMessageDialog(steg.desk,
                                              "You have entered your machine name.\n " +
                                              "Please enter others machine name !",
                                              "Home Destination",
                                              JOptionPane.WARNING_MESSAGE);
                sendField.setText("") ;
                sendField.requestFocus() ;
                return false;
            }
        }
        catch(Exception ex){}
        if(sendField.getText().length()<=0){
            JOptionPane.showInternalMessageDialog(steg.desk,
                                          "Please choose the sender !",
                                          "Sender required",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(fileField.getText().length()<=0){
            JOptionPane.showInternalMessageDialog(steg.desk,
                                          "Please choose the file to transfer !",
                                          "File field empty",
                                          JOptionPane.WARNING_MESSAGE);
            return false;

        }
        File temp=new File(fileField.getText()) ;
        if (!temp.exists()) {
            JOptionPane.showInternalMessageDialog(steg.desk,
                                          "The file " + temp.getName() +
                                          " does not exists\nPlease choose any other file",
                                          "File dos not exist",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
