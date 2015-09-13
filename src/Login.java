import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

@SuppressWarnings("serial")
public class Login extends JInternalFrame implements ActionListener{
    private JLabel head,user,password;
    private JTextField userField;
    private JPasswordField passField;
    public JButton ok,cancel;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private boolean valid=false;
    private JSeparator sep1=new JSeparator(),sep2=new JSeparator();
    private Image_Watermark steg;
    public Server server;
    private DataInputStream datInStrm = null;

    public boolean isValid(){
        return valid;
    }

    public Login(Image_Watermark stegano){
        super("Login",
             false,           //resizable
             true,            //closable
             false,           //maximizable
             false);          //iconifiable
        setFrameIcon(new ImageIcon("resource/login.gif"));
        setLocation(10, 10);
        steg = stegano;
        steg.log_live = true ;
        setSize(450,250);
        head = new JLabel("Login");
        user = new JLabel("User name");
        password = new JLabel("Password");
        userField = new JTextField(20);
        passField = new JPasswordField(20);
        ok = new JButton("    Ok    ");
        cancel = new JButton("Close");
        ok.addActionListener(this) ;
        cancel.addActionListener(this) ;
        //userField.setFont(new Font("Courier", Font.PLAIN, 14));
        userField.setForeground(Color.BLUE);
        userField.setBorder(BorderFactory.createLineBorder(Color.red));
        userField.setCaretColor(Color.BLUE);

        //passField.setFont(new Font("Courier", Font.PLAIN, 14));
        passField.setForeground(Color.BLUE);
        passField.setEchoChar('-');
        passField.setCaretColor(Color.BLUE);
        passField.setBorder(BorderFactory.createLineBorder(Color.red));

        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();

        JPanel jp = new JPanel();
        jp.setBackground(new Color(218, 201, 233));//
        jp.setBorder(BorderFactory.createRaisedBevelBorder());
        jp.setLayout(gbl);

        //Add heading to the form
        head.setFont(new Font("Times-Roamn", Font.BOLD, 24));
        head.setForeground(Color.RED);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(head, gbc);

        //Constraints for Seprator
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(sep1, gbc);

        //Add file label,textfields and browse button
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10);
        jp.add(user, gbc);

        gbc.insets = new Insets(10, 10, 10, 50);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(userField, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10);
        jp.add(password, gbc);

        gbc.insets = new Insets(10, 10, 10, 50);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(passField, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 0, 50);
        jp.add(sep2, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(218, 201, 233));
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(buttonPanel, gbc);

        Container cp = getContentPane();
        cp.setBackground(new Color(218, 201, 233));
        cp.setLayout(gbl);
        gbc.insets = new Insets(5, 10, 5, 10);
        cp.add(jp,gbc) ;
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                if(!valid)
                    steg.fileItem[0].setEnabled(true);
                steg.log_live = false;
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==ok){
            valid=check();
            if(valid){
                this.dispose();
                for (int i = 0; i < 6; i++) {
                    steg.winItem[i].setEnabled(true);
                    steg.toolbutton[i].setEnabled(true);
                }
                steg.fileItem[0].setEnabled(false);
                steg.fileItem[3].setEnabled(true);
                steg.toolbutton[6].setEnabled(true);
                JOptionPane.showInternalMessageDialog(steg.desk,
                              "You have successfully signed in.\nYou can now fully access this application.",
                              "Valid User",
                              JOptionPane.
                              INFORMATION_MESSAGE);
                server = new Server(steg);
            }
        }
        else if(ae.getSource()==cancel){
            if(!valid)
                steg.fileItem[0].setEnabled(true);
            steg.log_live = false;
            dispose() ;
        }
    }

    public boolean check(){
        
        String login = userField.getText();
        String pass = passField.getText();
        boolean found = false;
            //Reading values

            if (login.equals("admin") && pass.equals("admin")) {
                     found = true;
             }
            if (!found) {
            JOptionPane.showInternalMessageDialog(steg.desk,
                                          "Improper user id or password.\nPlease enter proper details.",
                                          "Invalid User",
                                          JOptionPane.
                                          INFORMATION_MESSAGE);
            userField.setText("");
            passField.setText("");
            userField.requestFocus() ;
            return (found);
        }
        return found;
    }
}
