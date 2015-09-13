import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

@SuppressWarnings("serial")
public class RetriveMessage extends JInternalFrame implements ActionListener {
    Image icon;
    Retrive dmsg;
    Image_Watermark steg;
    File file;
    JLabel label[] = new JLabel[4];
    JButton button[] = new JButton[5];
    JTextField filename = new JTextField(30);
    JTextArea txtMessage = new JTextArea(10, 40);
    JSeparator sep1 = new JSeparator(),
                      sep2 = new JSeparator();
    JFileChooser filechoose = new JFileChooser();
    GridBagLayout gbl;
    GridBagConstraints gbc;

    public RetriveMessage(Image_Watermark stegano){
        super("Retrive Message",
              false, //resizable
              true, //closable
              false, //maximizable
              true); //iconifiable
        steg = stegano;
        steg.win3_live = true;
        setFrameIcon(new ImageIcon("resource/retrive.gif"));
        setSize(660, 500);
        setLocation(10, 10);
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        label[0] = new JLabel("Retrive Message");
        label[1] = new JLabel("Input File");
        label[2] = new JLabel("Message : ");
        label[3] = new JLabel(" ");
        button[0] = new JButton("Browse");
        button[1] = new JButton("Retrive");
        button[2] = new JButton("Save");
        button[3] = new JButton("Reset");
        button[4] = new JButton("Close");
        button[0].setToolTipText("Click to browse the file you want to select");
        button[1].setToolTipText("Click to retrive the message from input file");
        button[2].setToolTipText("Click to save the message on your disk");
        button[3].setToolTipText("Click to clear all text fields");
        button[4].setToolTipText("Click to close the Form");
        button[2].setEnabled(false);
        for (int i = 0; i <= 4; i++) {
            if (i != 0 && i < 4) label[i].setFont(new Font("Times-Roamn",
                    Font.CENTER_BASELINE, 12));
            button[i].addActionListener(this);
        }
        filename.setFont(new Font("Courier", Font.PLAIN, 14));
        filename.setEditable(false);
        filename.setForeground(Color.BLUE);
        filename.setBorder(BorderFactory.createLineBorder(Color.RED));
        txtMessage.setForeground(Color.BLUE);
        txtMessage.setCaretColor(Color.BLUE);
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createRaisedBevelBorder());
        jp.setBackground(new Color(218, 201, 233));
        jp.setLayout(gbl);

        //Add heading to the form
        label[0].setFont(new Font("Times-Roamn", Font.BOLD, 24));
        label[0].setForeground(Color.RED);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 0, 0);
        jp.add(label[0], gbc);

        //Constraints for Seprator
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(sep1, gbc);

        //Add file label,textfields and browse button
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 100, 10, 10);
        jp.add(label[1], gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        jp.add(filename, gbc);

        gbc.insets = new Insets(10, 10, 10, 100);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(button[0], gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 0, 50);
        jp.add(sep2, gbc);

        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(createMessagePanel(), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(218, 201, 233));
        buttonPanel.add(button[1], FlowLayout.LEFT);
        buttonPanel.add(button[2], FlowLayout.CENTER);
        buttonPanel.add(button[4], FlowLayout.RIGHT);
        buttonPanel.add(button[3], FlowLayout.RIGHT);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(buttonPanel, gbc);

        gbc.insets = new Insets(0, 50, 10, 50);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 3;
        //jp.add(label[3], gbc);

        Container cp = getContentPane();
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
    JPanel createMessagePanel(){
        JPanel mesgPanel=new JPanel(new GridBagLayout());
        mesgPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        mesgPanel.setBackground(new Color(218,201,233)) ;
        GridBagConstraints gbc=new GridBagConstraints() ;
        gbc.fill=GridBagConstraints.HORIZONTAL ;
        gbc.insets =new Insets(10,10,0,0);
        gbc.gridwidth =GridBagConstraints.REMAINDER ;
        gbc.anchor =GridBagConstraints.WEST ;
        mesgPanel.add(label[2],gbc);
        gbc.insets =new Insets(5,10,5,0) ;
        mesgPanel.add(new JScrollPane(txtMessage),gbc);
        return mesgPanel;
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==button[0]){
            filechoose.addChoosableFileFilter(new InputFileFilter());
            int source = filechoose.showOpenDialog(this);
            file = filechoose.getSelectedFile();
            if (file != null && source == JFileChooser.APPROVE_OPTION)
                filename.setText(file.getAbsolutePath());
        }
       else if(ae.getSource()==button[1]||ae.getSource() == steg.toolbutton[6]){
           String f=filename.getText() ;
           dmsg=new Retrive() ;
           operationStarted();
           boolean result=dmsg.retriveMessage(f);
           operationComplete() ;
           if((filename.getText().length())>0){
               if(result){
                   txtMessage.setText(dmsg.getMessage());
                   JOptionPane.showInternalMessageDialog(steg.desk,
                                                 "Message retrived successfully from file \n" +
                                                 f,
                                                 "Operation Successful",
                                                 JOptionPane.
                                                 INFORMATION_MESSAGE);
                   button[2].setEnabled(true);
                   steg.fileItem[1].setEnabled(true) ;
               }
               else{
                   JOptionPane.showInternalMessageDialog(steg.desk, dmsg.getMessage(),
                                             "Operation Failed",
                                             JOptionPane.WARNING_MESSAGE);
               }
           }
           else
               JOptionPane.showInternalMessageDialog(steg.desk,
                                             "Please select input file for Retrive Operation",
                                             "File field Empty",
                                             JOptionPane.INFORMATION_MESSAGE);
       }
       else if(ae.getSource()==button[2]||ae.getSource()==steg.fileItem[1]){
           String text=txtMessage.getText();
           if(text.length() > 0){
               filechoose.addChoosableFileFilter(new TextFileFilter());
               filechoose.setSelectedFile(new File("")) ;
               if (filechoose.showSaveDialog(this) ==
                   JFileChooser.APPROVE_OPTION) {
                   file = filechoose.getSelectedFile();
                   try {
                       FileWriter fw = new FileWriter(file.getAbsolutePath());
                       fw.write(text);
                       fw.close();
                       JOptionPane.showInternalMessageDialog(steg.desk,
                               "File saved successfully at\n" +
                               file.getAbsolutePath(),
                               "File Saved",
                               JOptionPane.WARNING_MESSAGE);

                   } catch (Exception e) {
                       JOptionPane.showInternalMessageDialog(steg.desk, e.toString(),
                               "Save Operation Failed",
                               JOptionPane.WARNING_MESSAGE);
                   }
               }
           }
           else{
               JOptionPane.showInternalMessageDialog(steg.desk,
                                             "There is no messge in the message pane to save",
                                             "No message to save",
                                             JOptionPane.WARNING_MESSAGE);
               button[2].setEnabled(false) ;
               steg.fileItem[1].setEnabled(false) ;
           }
       }
       else if(ae.getSource()==button[3]){
           filename.setText("") ;
           txtMessage.setText("") ;
           label[3].setText("") ;
           button[2].setEnabled(false) ;
           steg.fileItem[1].setEnabled(false) ;
        }
        else if(ae.getSource() == button[4]){
            steg.fileItem[1].setEnabled(false) ;
            steg.win3_live = false;
            this.dispose() ;
        }
    }
    void operationStarted() {
        for (int i = 0; i < 4; i++)
            button[i].setEnabled(false);
        steg.toolbutton[6].setEnabled(false) ;
    }
    void operationComplete() {
        for (int i = 0; i <= 4; i++)
            button[i].setEnabled(true);
        steg.toolbutton[6].setEnabled(true);
    }
}
