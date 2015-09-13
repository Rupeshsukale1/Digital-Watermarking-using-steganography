import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameAdapter;

@SuppressWarnings("serial")
public class RetriveFile extends JInternalFrame implements ActionListener{
    private File file;
    private JLabel label[]=new JLabel[6];
    private JButton button[]=new JButton[4];
    private JTextField filename=new JTextField(30);
    private JSeparator sep1=new JSeparator(),sep2=new JSeparator();
    private JFileChooser filechoose=new JFileChooser();
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private Image_Watermark steg;

    public static void main(String args[]){
        JFrame.setDefaultLookAndFeelDecorated(true);
        RetriveFile rf = new RetriveFile(null);
        rf.setSize(600, 250);
        rf.setVisible(true);
        rf.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public RetriveFile(Image_Watermark stegano){
        super("Retrive File",
              false, //resizable
              true, //closable
              false, //maximizable
              true); //iconifiable
        steg = stegano;
        steg.win4_live = true;
        setFrameIcon(new ImageIcon("resource/retrive.gif"));
        setSize(600, 250);
        setLocation(10, 10);
        Container cp = getContentPane();
        label[0] = new JLabel("Retrive File");
        label[1] = new JLabel("Input File");
        button[0] = new JButton("Browse");
        button[1] = new JButton("Retrive");
        button[2] = new JButton("Reset");
        button[3] = new JButton("Close");
        button[0].setToolTipText("Click to browse the file you want to select");
        button[1].setToolTipText("Click to retrive the message from input file");
        button[2].setToolTipText("Click to clear all text fields");
        button[3].setToolTipText("Click to close the Form");
        label[1].setFont(new Font("Times-Roamn", Font.CENTER_BASELINE, 14));
        for (int i = 0; i < 4; i++) {
            button[i].addActionListener(this);
        }
        filename.setFont(new Font("Courier", Font.PLAIN, 14));
        filename.setForeground(Color.BLUE);
        filename.setEditable(false);
        filename.setBorder(BorderFactory.createLineBorder(Color.RED));

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

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        jp.add(filename, gbc);

        gbc.insets = new Insets(10, 10, 10, 50);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(button[0], gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 0, 50);
        jp.add(sep2, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(218, 201, 233));
        buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        buttonPanel.add(button[1], FlowLayout.LEFT);
        buttonPanel.add(button[2], FlowLayout.CENTER);
        buttonPanel.add(button[3], FlowLayout.RIGHT);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(buttonPanel, gbc);

        cp.setBackground(new Color(218, 201, 233));
        cp.setLayout(gbl);
        gbc.insets = new Insets(5, 10, 5, 10);
        cp.add(jp, gbc);
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                steg.win3_live = false;
            }
        });
    }
    public void actionPerformed(ActionEvent ae){
        filechoose.addChoosableFileFilter(new InputFileFilter());
        if(ae.getSource()==button[0]){
            int returnVal=filechoose.showOpenDialog(this);
            file=filechoose.getSelectedFile();
            if(file!=null && returnVal==JFileChooser .APPROVE_OPTION )
                filename.setText(file.getAbsolutePath());
        }
        else if(ae.getSource()==button[1]||ae.getSource() == steg.toolbutton[6]){
            String f=filename.getText() ;
            Retrive dmsg=new Retrive(this) ;
            if((filename.getText().length())>0){
                operationStarted();
                String result=dmsg.retriveFile(f);
                operationComplete();
                if(result!="Failed"){
                    JOptionPane.showInternalMessageDialog(steg.desk,
                        "File retrived successfully from file " + f +
                        "\nand saved as " + dmsg.getMessage(),
                                              "Operation Successful",
                                              JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showInternalMessageDialog(steg.desk, dmsg.getMessage(),
                                              "Operation Failed",
                                              JOptionPane.WARNING_MESSAGE);
                }
            }
            else
                JOptionPane.showInternalMessageDialog(steg.desk,"Please select input file for Retrive Operation",
                                              "File field Empty",
                                              JOptionPane.INFORMATION_MESSAGE);
        }
        else if(ae.getSource()==button[2]){
            filename.setText(" ") ;
        }
        else if(ae.getSource()==button[3]){
            steg.win4_live=false;
            this.dispose();
        }
    }
    void operationStarted() {
        for (int i = 0; i < 4; i++)
            button[i].setEnabled(false);
        steg.toolbutton[6].setEnabled(false) ;
    }
    void operationComplete() {
        for (int i = 0; i < 4; i++)
            button[i].setEnabled(true);
        steg.toolbutton[6].setEnabled(true);
    }
}
