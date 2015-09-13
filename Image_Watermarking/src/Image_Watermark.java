import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.KeyStroke;

public class Image_Watermark extends JFrame implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JWindow splashScreen = null;
    private JLabel splashLabel = null  ;
    private EmbedMessage embedmsg;
    private EmbedFile embedfile;
    private WaterMark wmark;
    private Image_morph morph;
    private RetriveMessage retrivemsg;
    private RetriveFile retrivefile;
    public Sender send;
    private Login log;
    private Help hlp;
    private JMenuBar menu;
    private JMenu file,win,help;
    private JMenuItem helpItem1,helpItem2;
    public JMenuItem fileItem[] = new JMenuItem[4],
                     setItem[] =new JMenuItem[2],
              winItem[]=new JMenuItem[6] ;
    public JDesktopPane desk;
    private JToolBar toolBar;
    private Image icon;
    public JButton toolbutton[]=new JButton[10];
    public boolean win1_live ,win2_live,win3_live,win4_live,win5_live,win6_live,log_live,help_live;
    int width=800,height=570;

    public Image_Watermark(String str) {
        super(str);
        createSplashScreen();
        showSplashScreen();
        icon = Toolkit.getDefaultToolkit().getImage(
                "resource/s3.gif");
        setIconImage(icon);
        desk = new JDesktopPane();
        menu = new JMenuBar();
        toolBar = new JToolBar("Steg",JToolBar.VERTICAL);
        helpItem1 = new JMenuItem("About Morphing");
        helpItem2 = new JMenuItem("Help  ");
        fileItem[0] = new JMenuItem("Login");
        fileItem[1] = new JMenuItem("Save msg");
        fileItem[2] = new JMenuItem("Exit ");
        fileItem[3] = new JMenuItem("Change Username & Password");
        winItem[0] = new JMenuItem("Embed Message");
        winItem[1] = new JMenuItem("Embed File");
        winItem[2] = new JMenuItem("Retrive Message");
        winItem[3] = new JMenuItem("Retrive File");
        winItem[4] = new JMenuItem("Send File");
        winItem[5] = new JMenuItem("Water Mark Image");
        win1_live=false ;
        win2_live=false ;
        win3_live=false ;
        win4_live=false ;
        win5_live=false ;
        win6_live=false ;
        log_live=false;
        help_live=false;
        addToolButtons();
        getContentPane().setLayout(new BorderLayout()) ;
        getContentPane().add(toolBar,BorderLayout.WEST) ;
        getContentPane().add(desk,BorderLayout.CENTER) ;
        menu.add(createFileMenu());
        menu.add(createWindowMenu());
        menu.add(createHelpMenu());
        setJMenuBar(menu);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize() ;
        setLocation((d.width - width)/2,(d.height - height)/2);
        setSize(width, height);
        for(int i=0;i<6;i++){
            winItem[i].setEnabled(false) ;
            winItem[i].addActionListener(this) ;
            toolbutton[i].setEnabled(false) ;
            toolbutton[i].addActionListener(this) ;
            //if(i>=2)toolbutton[i+4].addActionListener(this) ;
            toolbutton[i].addActionListener(this) ;
            if(i<4)fileItem[i].addActionListener(this) ;
        }
        toolbutton[6].setEnabled(false) ;
        toolbutton[6].addActionListener(this) ;
        toolbutton[1].setEnabled(false) ;
        toolbutton[2].addActionListener(this) ;
        fileItem[1].setEnabled(false) ;
        fileItem[3].setEnabled(false) ;
        helpItem1.addActionListener(this);
        helpItem2.addActionListener(this);
        log = new Login(this);
        addWindowListener(new WindowAdapter (){
            public void windowClosing(WindowEvent we){
                int result = JOptionPane.showInternalConfirmDialog(
                        desk,
                        "Are you sure you want to exit ?",
                        "Close conformation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    if(log.isValid())
                        log.server.stopServer() ;
                    dispose();
                }
            }
        }) ;
        hideSplash();
    }

    public void addToolButtons() {
        toolbutton[0] = new JButton("<html><center>Embed<br>Message</center></html>",new ImageIcon("resource/embed.gif"));
        toolbutton[1] = new JButton("<html><center>Embed<br>File</center></html>",new ImageIcon("resource/embed.gif"));
        toolbutton[2] = new JButton("<html><center>Retrive<br>Message</center></html>",new ImageIcon("resource/retrive.gif"));
        toolbutton[3] = new JButton("<html><center>Retrive<br>File</center></html>",new ImageIcon("resource/retrive.gif"));
        toolbutton[4] = new JButton("<html><center>Send<br>File</center></html>",new ImageIcon("resource/send.gif"));
        toolbutton[5] = new JButton("<html><center>Water<br>Mark</center></html>",new ImageIcon("resource/watermark.gif"));
        toolbutton[6] = new JButton("<html><center>Morphing<br></center></html>") ;
        //toolbutton[7] = new JButton("<html><center>Retrive</center></html>") ;
       // toolbutton[8] = new JButton("<html><center>Send</center></html>") ;
       // toolbutton[7] = new JButton("<html><center>About ..</center></html>") ;
        JLabel start=new JLabel("     FORMS  ");
        start.setForeground(Color.BLUE) ;
        //JLabel oper=new JLabel("<html><center><B>OPERATIONS</B></center></html>");
        //oper.setFont(new Font("Dialog", Font.BOLD, 12)) ;
        //JLabel separator=new JLabel("<html><center>________");
        //separator.setFont(new Font("Dialog", Font.BOLD, 16)) ;
        //.separator.setForeground(new Color(99,99,156)) ;
        //oper.setForeground(Color.BLUE) ;
        toolBar.add(start);
        toolBar.addSeparator() ;
        for(int i=0 ; i<7 ;i++){
            toolbutton[i].setBorder(BorderFactory.createLineBorder(new Color(99,99,156))) ;
            toolbutton[i].setVerticalTextPosition(AbstractButton.BOTTOM);
            toolbutton[i].setHorizontalTextPosition(AbstractButton.CENTER);
            toolBar.add(toolbutton[i]);
            if(i==5){
              //  toolBar.add(separator);
               // toolBar.add(oper);
            }
            if(i>5)toolbutton[i].setFont(new Font("Dialog", Font.CENTER_BASELINE, 14)) ;
            toolBar.addSeparator() ;
        }
        toolBar.setBorder(BorderFactory.createRaisedBevelBorder()) ;
        toolBar.setFloatable(false) ;
        toolBar.setRollover(true);
    }

    private JMenu createFileMenu(){
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F) ;
        fileItem[0].setMnemonic(KeyEvent.VK_L);
        fileItem[0].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        fileItem[1].setMnemonic(KeyEvent.VK_S);
        fileItem[1].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileItem[2].setMnemonic(KeyEvent.VK_X);
        fileItem[2].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, ActionEvent.ALT_MASK));
        file.add(fileItem[0]);
        file.add(fileItem[1]);
        file.add(fileItem[3]);
        file.addSeparator();
        file.add(fileItem[2]);
        return file;
    }

    private JMenu createWindowMenu(){
        win = new JMenu("Window");
        win.setMnemonic(KeyEvent.VK_W) ;
        winItem[0].setMnemonic(KeyEvent.VK_F1);
        winItem[0].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F1, ActionEvent.CTRL_MASK));
        winItem[1].setMnemonic(KeyEvent.VK_F2);
        winItem[1].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F2, ActionEvent.CTRL_MASK));
        winItem[2].setMnemonic(KeyEvent.VK_F3);
        winItem[2].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F3, ActionEvent.CTRL_MASK));
        winItem[3].setMnemonic(KeyEvent.VK_F4);
        winItem[3].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
        winItem[4].setMnemonic(KeyEvent.VK_F5);
        winItem[4].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
        winItem[5].setMnemonic(KeyEvent.VK_F6);
        winItem[5].setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F6, ActionEvent.CTRL_MASK));
        for(int i=0;i<6;i++)
            win.add(winItem[i]);
        return win;
    }
    private JMenu createHelpMenu(){
        help = new JMenu("Help");
        helpItem1.setMnemonic(KeyEvent.VK_F2);
        helpItem1.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F2, ActionEvent.SHIFT_MASK));
        helpItem2.setMnemonic(KeyEvent.VK_F1);
        helpItem2.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F1, ActionEvent.SHIFT_MASK));
        help.add(helpItem1) ;
        help.add(helpItem2) ;
        return help;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == winItem[0]||ae.getSource() == toolbutton[0]) {
            if(!win1_live) {
                embedmsg = new EmbedMessage(this);
                desk.add(embedmsg, BorderLayout.CENTER);
                try {
                    embedmsg.setVisible(true);
                    embedmsg.setSelected(true);
                } catch (Exception e) {}
            }
            else {
                try {
                    embedmsg.setIcon(false);
                    embedmsg.moveToFront();
                    embedmsg.setSelected(true);
                } catch (Exception e) {}
            }
        }
        else if (ae.getSource() == winItem[1]||ae.getSource() == toolbutton[1]) {
            if (!win2_live) {
                embedfile = new EmbedFile(this);
                desk.add(embedfile);
                try {
                    embedfile.setVisible(true);
                    embedfile.setSelected(true);
                } catch (Exception e) {}
            } else {
                try {
                    embedfile.setIcon(false);
                    embedfile.moveToFront();
                    embedfile.setSelected(true);
                } catch (Exception e) {}
            }
        }
        else if (ae.getSource() == winItem[2]||ae.getSource() == toolbutton[2]) {
            if (!win3_live) {
                retrivemsg = new RetriveMessage(this);
                desk.add(retrivemsg);
                try {
                    retrivemsg.setVisible(true);
                    retrivemsg.setSelected(true);
                } catch (Exception e) {}
            } else {
                try {
                    retrivemsg.setIcon(false);
                    retrivemsg.moveToFront();
                    retrivemsg.setSelected(true);
                } catch (Exception e) {}
            }
       }
       else if (ae.getSource() == winItem[3]||ae.getSource() == toolbutton[3]) {
           if (!win4_live) {
               retrivefile = new RetriveFile(this);
               desk.add(retrivefile);
               try {
                   retrivefile.setVisible(true);
                   retrivefile.setSelected(true);
               } catch (Exception e) {}
           } else {
                try {
                    retrivefile.setIcon(false);
                    retrivefile.moveToFront();
                    retrivefile.setSelected(true);
                } catch (Exception e) {}
            }
        }
        else if (ae.getSource() == winItem[4]||ae.getSource() == toolbutton[4]) {
            if (!win5_live) {
                send = new Sender((File)null, this);
                desk.add(send);
                try {
                    send.setVisible(true);
                    send.setSelected(true);
                } catch (Exception e) {}
            } else {
                try {
                    send.setIcon(false);
                    send.moveToFront();
                    send.setSelected(true);
                } catch (Exception e) {}
            }
        }
        else if (ae.getSource() == winItem[5]||ae.getSource() == toolbutton[5]) {
            if (!win6_live) {
                 wmark = new WaterMark(this);
                 desk.add(wmark, BorderLayout.CENTER);
                 try {
                	 wmark.setVisible(true);
                	 wmark.setSelected(true);
                 } catch (Exception e) {}
             }
             else {
                 try {
                	 wmark.setIcon(false);
                	 wmark.moveToFront();
                	 wmark.setSelected(true);
                 } catch (Exception e) {}
            }
        }
        else if (ae.getSource() == toolbutton[6]) {
            if (!win6_live) {
                 morph = new Image_morph(this);
                 desk.add(morph, BorderLayout.CENTER);
                 try {
                	 morph.setVisible(true);
                	 morph.setSelected(true);
                 } catch (Exception e) {}
             }
             else {
                 try {
                	 morph.setIcon(false);
                	 morph.moveToFront();
                	 morph.setSelected(true);
                 } catch (Exception e) {}
            }
        }
        else if (ae.getSource() == fileItem[0]){
            fileItem[0].setEnabled(false) ;
            if(!log_live)
                log=new Login(this) ;
            desk.add(log);
            try {
                log.setVisible(true);
                log.setSelected(true);
            } catch (Exception e) {}
        }
        else if (ae.getSource() == fileItem[1]){
            retrivemsg.actionPerformed(ae) ;
        }
        else if (ae.getSource() == fileItem[2]){
            int result = JOptionPane.showInternalConfirmDialog(
                    desk,
                    "Are you sure you want to exit ?",
                    "Close conformation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                if (log.isValid())
                    log.server.stopServer();
                this.dispose();
            }
        }
        else if (ae.getSource() == fileItem[3]){
            if (changeUser())
                JOptionPane.showInternalMessageDialog(desk,
                                              "User Id and password changed successfully.",
                                              "Id and Password changed.",
                                              JOptionPane.
                                              INFORMATION_MESSAGE);
        }
        else if (ae.getSource() == helpItem1||ae.getSource() == toolbutton[8]){
            inform();
        }
        else if (ae.getSource() == helpItem2){
            if (!help_live) {
                hlp = new Help(this);
                desk.add(hlp);
                try {
                    hlp.setVisible(true);
                    hlp.setSelected(true);
                } catch (Exception e) {}
            }else {
                try {
                    hlp.setIcon(false);
                    hlp.moveToFront();
                } catch (Exception e) {}
            }
        }
        else if (ae.getSource() == toolbutton[6]){
            if(win1_live){
                if(!embedmsg.isIcon() && embedmsg.isSelected())
                    embedmsg.actionPerformed(ae) ;
            }
            if(win2_live){
                if(!embedfile.isIcon() && embedfile.isSelected())
                    embedfile.actionPerformed(ae) ;
            }
        }
        else if (ae.getSource() == toolbutton[7]){
            if(win3_live){
                if(!retrivemsg.isIcon() && retrivemsg.isSelected())
                    retrivemsg.actionPerformed(ae) ;
            }
            if(win4_live){
                if(!retrivefile.isIcon() && retrivefile.isSelected())
                    retrivefile.actionPerformed(ae) ;
            }
        }
        else if (ae.getSource() == toolbutton[8]){
            if(win5_live){
                if (!send.isIcon() && send.isSelected())
                    send.actionPerformed(ae);
            }
            if(win1_live){
                if(!embedmsg.isIcon() && embedmsg.isSelected())
                    embedmsg.actionPerformed(ae) ;
            }
            if(win2_live){
                if(!embedfile.isIcon() && embedfile.isSelected())
                    embedfile.actionPerformed(ae) ;
            }
            if(win6_live){
                if(!wmark.isIcon() && wmark.isSelected())
                	wmark.actionPerformed(ae) ;
            }
        }
    }

    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        Image_Watermark steganoFrame = new Image_Watermark("Image Morphing-1.0.0");
        steganoFrame.setVisible(true);
    }

    public void inform() {
        JLabel title = new JLabel("Image Morphing Implementation ");
        title.setFont(new Font("Times-Roman", Font.BOLD, 24));
        title.setForeground(Color.RED);
        JLabel name1=new JLabel("<html><center>Hariharan, Dilpreet, ");
        name1.setFont(new Font("Monotype Corsiva", Font.BOLD, 20)) ;
        JLabel name2=new JLabel("<html><center>") ;
        name2.setFont(new Font("Monotype Corsiva", Font.BOLD, 20)) ;
        Object[] ob = new Object[10];
        ob[0] = (Object)title;
        ob[1] = new JSeparator();
        ob[2] = "\nThis application \"Image Morphing\" ";
               
        ob[3] = new JSeparator();
        ob[4] = new JLabel(" ");
        ob[5] = new JLabel(" ");
        ob[6] = null;
        ob[7] = new String("Developers :");
        ob[8] = (Object)name1;
        ob[9] = (Object)name2;
        String[] but = new String[1];
        but[0] = "OK";
        JOptionPane.showInternalOptionDialog(
                    desk,					        // the parent that the dialog blocks
                    ob,							// the dialog message array
                    "About Image_Morphing",				// the title of the dialog window
                    JOptionPane.DEFAULT_OPTION,			        // option type
                    JOptionPane.INFORMATION_MESSAGE,	                // message type
                    new ImageIcon("resource/s2.gif"),			// optional icon, use null to use the default icon
                    but,						// options string array, will be made into buttons
                    but[0]						// option that should be made into a default button
                         );
    }


	public boolean changeUser() {
        DataOutputStream datOutStrm;
        String user, pass;
        JTextField jtf = new JTextField(20);
        JPasswordField jpf = new JPasswordField(20);
        Object[] ob = new Object[4];
        ob[0] = "Enter new User ID below";
        ob[1] = jtf;
        ob[2] = "Enter new password below";
        ob[3] = jpf;
        String[] but = new String[2];
        but[0] = "Close";
        but[1] = "OK";
        while (true) {
            int result = JOptionPane.showInternalOptionDialog(
                    desk,							// the parent that the dialog blocks
                    ob,								// the dialog message array
                    "Change User ID and Password",  // the title of the dialog window
                    JOptionPane.DEFAULT_OPTION,		// option type
                    JOptionPane.INFORMATION_MESSAGE,// message type
                    null,							// optional icon, use null to use the default icon
                    but,							// options string array, will be made into buttons
                    but[1]							// option that should be made into a default button
                         );
            switch (result) {
            case 0: //Close
                return false;
            default: // OK
                jtf = (JTextField) ob[1];
                user = jtf.getText();
                jpf = (JPasswordField) ob[3];
                pass = jpf.getText();
                if (user.length() <= 0) {
                    JOptionPane.showInternalMessageDialog(desk,
                                                  "Please enter user id.",
                                                  "User id required.",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                    break;
                }
                if (pass.length() <= 0) {
                    JOptionPane.showInternalMessageDialog(desk,
                                                  "Please enter the password.",
                                                  "Password required.",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                    break;
                }
                try {
                    datOutStrm = new DataOutputStream(new
                            FileOutputStream("resource/user.log"));
                    Encrypt enc = new Encrypt();
                    pass = enc.encryptMessage((byte) 125, pass);
                    user = enc.encryptMessage((byte) 125, user);
                    datOutStrm.writeUTF(user);
                    datOutStrm.writeUTF(pass);
                    datOutStrm.close();
                } catch (FileNotFoundException fnfe) {
                    JOptionPane.showInternalMessageDialog(desk,
                                                  "Cannot find the log file of users.",
                                                  "File not found",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                    return false;
                } catch (Exception e) {
                    JOptionPane.showInternalMessageDialog(desk,
                                                  "Error in reading the log file.\nTry again.",
                                                  "Error occured",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                    return false;
                }
                return true;
            }
        }
    }

    public void createSplashScreen() {
        splashLabel = new JLabel(new ImageIcon("resource/Splash.jpg", "Splash.accessible_description"));
        splashScreen = new JWindow();
        splashScreen.getContentPane().add(splashLabel);
        splashScreen.pack();
        Rectangle rect=splashScreen.getBounds() ;
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize() ;
        splashScreen.setLocation((int)(d.width - rect.getWidth())/2,(int)(d.height - rect.getHeight())/2);
    }

    public void showSplashScreen() {
        splashScreen.setVisible(true) ;
    }
    public void hideSplash(){
        splashScreen.dispose();
    }
}
