import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameAdapter;

@SuppressWarnings("serial")
public class EmbedMessage extends JInternalFrame implements ActionListener, KeyListener {
    Embed embed;
    Sender send;
    Image_Watermark steg;
    File file;
    JLabel label[] = new JLabel[6];
    JButton button[] = new JButton[6];
    JTextField filename;
    JTextArea txtMessage;
    JSeparator sep1 = new JSeparator(),
                      sep2 = new JSeparator();
    JFileChooser filechoose = new JFileChooser();
    JScrollPane scrollPane;
    GridBagLayout gbl;
    GridBagConstraints gbc;
    String message, msg;
    private boolean isEmbeddable;
    long inputFileSize;

    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        EmbedMessage em = new EmbedMessage(null);
        em.setSize(670, 525);
        em.setVisible(true);
        em.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public EmbedMessage(Image_Watermark stegano) {
        super("Embed Message",
              false, //resizable
              true, //closable
              false, //maximizable
              true); //iconifiable
        steg = stegano;
        steg.win1_live = true;
        setFrameIcon(new ImageIcon("resource/embed.gif"));
        Container cp = getContentPane();
        setSize(660, 480);
        setLocation(10, 10);
        label[0] = new JLabel("Embed Message");
        label[1] = new JLabel("Input File");
        label[2] = new JLabel("Output File");
        label[3] = new JLabel("Message : ");
        label[4] = new JLabel(
                "                                                     ");
        label[5] = new JLabel(" ", 10);
        filename = new JTextField(30);
        button[0] = new JButton("Browse");
        button[1] = new JButton("Browse");
        button[2] = new JButton("Embed");
        button[3] = new JButton("Reset");
        button[4] = new JButton("Send");
        button[5] = new JButton("Close");
        button[4].setEnabled(false);
        txtMessage = new JTextArea(10, 40);
        txtMessage.setForeground(Color.BLUE);
        txtMessage.setCaretColor(Color.BLUE);
        txtMessage.addKeyListener(this);
        scrollPane = new JScrollPane(txtMessage);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        for (int i = 0; i < 6; i++) {
            if (i != 0 && i < 4) label[i].setFont(new Font("Times-Roamn",
                    Font.CENTER_BASELINE, 12));
            button[i].addActionListener(this);
        }
        filename.setFont(new Font("Courier", Font.PLAIN, 14));
        filename.setEditable(false);
        filename.setForeground(new Color(0, 0, 255));
        filename.setBorder(BorderFactory.createLineBorder(Color.RED));
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();

        JPanel jpanel = new JPanel();
        jpanel.setBackground(new Color(218, 201, 233));
        jpanel.setBorder(BorderFactory.createRaisedBevelBorder());
        jpanel.setLayout(gbl);

        //Add heading to the form
        label[0].setFont(new Font("Times-Roamn", Font.BOLD, 24));
        label[0].setForeground(Color.RED);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jpanel.add(label[0], gbc);

        //Constraints for Seprator
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        jpanel.add(sep1, gbc);

        //Add file label,textfields and browse button
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 80, 10, 10);
        jpanel.add(label[1], gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        jpanel.add(filename, gbc);

        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        jpanel.add(button[0], gbc);

        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jpanel.add(label[5], gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 0, 50);
        jpanel.add(sep2, gbc);

        gbc.insets = new Insets(10, 50, 10, 50);
        jpanel.add(scrollPane, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(218, 201, 233));
        buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        buttonPanel.add(button[2]);
        buttonPanel.add(button[3]);
        buttonPanel.add(button[4]);
        buttonPanel.add(button[5]);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 50, 10, 50);
        jpanel.add(buttonPanel, gbc);

        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.add(label[4]);
        statusPanel.setBackground(new Color(218, 201, 233));
        gbc.insets = new Insets(0, 50, 10, 50);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 3;
        jpanel.add(statusPanel, gbc);

        cp.setBackground(new Color(218, 201, 233));
        cp.setLayout(gbl);
        gbc.insets = new Insets(5, 10, 5, 10);
        cp.add(jpanel, gbc);
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                steg.win1_live = false;
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == button[0]) {
            filechoose.addChoosableFileFilter(new InputFileFilter());
            if (filechoose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                file = filechoose.getSelectedFile();
                filename.setText(file.getAbsolutePath());
                label[5].setText((file.length() / 1024 + 1) + " KB");
                inputFileSize = new File(file.getAbsolutePath()).length();
            }
        } else if (ae.getSource() == button[2]||ae.getSource() == steg.toolbutton[5]) {
            String f1 = filename.getText();
            msg = txtMessage.getText();
            if (validInput()) {
                operationStarted();
                embed = new Embed();
                boolean result = embed.encodeMessage(f1, msg);
                operationComplete();
                if (result)
                    JOptionPane.showMessageDialog(steg.desk,
                                                  "Message embeded successfully in file:\n " +
                                                  f1,
                                                  "Operation Successful",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(steg.desk, embed.getMessage(),
                                                  "Operation Failed",
                                                  JOptionPane.WARNING_MESSAGE);
            }
        } else if (ae.getSource() == button[3]) {
            filename.setText("");
            txtMessage.setText("");
            label[4].setText(" ");
            label[5].setText(" ");
            button[4].setEnabled(false);
        } else if (ae.getSource() == button[4]||ae.getSource() == steg.toolbutton[7]) {
            if (!steg.win5_live) {
                steg.send = new Sender(new File(filename.getText()), steg);
                steg.desk.add(steg.send);
                try {
                    steg.send.setVisible(true);
                    steg.send.setSelected(true);
                    this.dispose();
                } catch (Exception e) {}
            }else {
                try {
                    steg.send.setFile(new File(filename.getText())) ;
                    steg.send.setIcon(false);
                    steg.send.moveToFront();
                    steg.send.setSelected(true);
                } catch (Exception e) {}
            }
            steg.win1_live = false;
            this.dispose();
        } else if (ae.getSource() == button[5]) {
            steg.win1_live = false;
            this.dispose();
        }
    }

    public void keyPressed(KeyEvent k) {}

    public void keyReleased(KeyEvent k) {}

    public void keyTyped(KeyEvent k) {
        message = txtMessage.getText();
        int size = message.length();
        if (size >= 32766) {
            JOptionPane.showMessageDialog(steg.desk,
                                          "This is the Maximum possible length of the message!",
                                          "Message limit",
                                          JOptionPane.INFORMATION_MESSAGE);
            txtMessage.setText(message.substring(0, 32765));
        }
        updateEmbedability();
    }

    private void updateEmbedability() {
        message = txtMessage.getText();
        int messageSize = message.length();
        int requiredSize=(128 + messageSize*4 + 2*4 + 1*4 );
        label[4].setText("Minimum input file size required: " +
                         requiredSize + " B (" +
                         (requiredSize / 1024 + 1) + " Kb)");
        if (filename.getText().length() > 0 && messageSize > 0) {
            if (inputFileSize  > requiredSize)
                isEmbeddable = true;
            else
                isEmbeddable = false;
        }
    }

    private boolean validInput() {
        if (filename.getText().length() <= 0) {
            JOptionPane.showMessageDialog(steg.desk,
                                          "Please choose the input file!",
                                          "Input file required.",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtMessage.getText().trim().length() <= 0) {
            JOptionPane.showMessageDialog(steg.desk, "Please key in the message!",
                                          "Message is empty.",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!isEmbeddable) {
            JOptionPane.showMessageDialog(steg.desk,
                                          "Message is too large to be embedded in input file\nPlease choose a larger input file.",
                                          "Message Unembeddable!",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    void operationStarted() {
        for (int i = 0; i < 4; i++)
            button[i].setEnabled(false);
        steg.toolbutton[5].setEnabled(false) ;
        txtMessage.setEditable(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)) ;
    }

    void operationComplete() {
        for (int i = 0; i <= 4; i++)
            button[i].setEnabled(true);
        steg.toolbutton[5].setEnabled(true) ;
        txtMessage.setEditable(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) ;
    }
}



