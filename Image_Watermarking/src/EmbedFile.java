import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameAdapter;

@SuppressWarnings("serial")
public class EmbedFile extends JInternalFrame implements ActionListener {
    Embed embed;
    Sender send;
    File dataFile, outFile, inFile;
    Image_Watermark steg;
    long requiredFileSize;
    final static int HEADER = 128;
    @SuppressWarnings("unused")
	private boolean isEmbeddable, inFileExists, outFileExists, dataFileExists;
    private JLabel label[] = new JLabel[7];
    private JButton button[] = new JButton[6];
    private JTextField filename[] = new JTextField[2];
    private JSeparator sep1 = new JSeparator(), sep2 = new JSeparator();
    private JFileChooser filechoose = new JFileChooser();
    private GridBagLayout gbl;
    private GridBagConstraints gbc;

    public EmbedFile(Image_Watermark stegano) {
        super("Embed File",
              false, //resizable
              true, //closable
              false, //maximizable
              true); //iconifiable
        steg = stegano;
        steg.win2_live = true;
        setFrameIcon(new ImageIcon("resource/embed.gif"));
        setSize(600, 380);
        setLocation(10, 10);
        isEmbeddable = false;
        inFileExists = false;
        outFileExists = false;
        dataFileExists = false;
        label[0] = new JLabel("Embed File");
        label[1] = new JLabel("Input File");
        label[3] = new JLabel("Data File");
        label[4] = new JLabel(
                "                                                                   ");
        label[5] = new JLabel("", 10);
        label[6] = new JLabel("", 10);
        label[5].setBackground(Color.BLACK);
        label[6].setBackground(Color.BLACK);
        filename[0] = new JTextField(30);
        filename[1] = new JTextField(30);
        button[0] = new JButton("Browse");
        button[1] = new JButton("Browse");
        button[2] = new JButton("Embed");
        button[3] = new JButton("Reset");
        button[4] = new JButton("Send");
        button[5] = new JButton("Close");
        button[4].setEnabled(false);
        for (int i = 0; i <= 5; i++) {
            if (i < 2) {
                filename[i].setFont(new Font("Courier", Font.PLAIN, 14));
                filename[i].setEditable(false);
                filename[i].setForeground(new Color(0, 0, 255));
                filename[i].setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            button[i].addActionListener(this);
        }

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

        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        jp.add(filename[0], gbc);

        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        jp.add(button[0], gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(label[5], gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10);
        jp.add(label[3], gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        jp.add(filename[1], gbc);

        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        jp.add(button[1], gbc);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jp.add(label[6], gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 0, 50);
        jp.add(sep2, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        buttonPanel.setBackground(new Color(218, 201, 233));
        buttonPanel.add(button[2]);
        buttonPanel.add(button[3]);
        buttonPanel.add(button[4]);
        buttonPanel.add(button[5]);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 50, 10, 50);
        jp.add(buttonPanel, gbc);

        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.add(label[4]);
        statusPanel.setBackground(new Color(218, 201, 233));
        gbc.insets = new Insets(0, 50, 10, 50);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 3;
        jp.add(statusPanel, gbc);

        Container cp = getContentPane();
        cp.setBackground(new Color(218, 201, 233));
        cp.setLayout(gbl);
        gbc.insets = new Insets(5, 10, 5, 10);
        cp.add(jp, gbc);
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                steg.win2_live = false;
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == button[0]) {
            filechoose.addChoosableFileFilter(new InputFileFilter());
            if (filechoose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                inFile = filechoose.getSelectedFile();
                filename[0].setText(inFile.getAbsolutePath());
                label[5].setText((inFile.length() / 1024 + 1) + " KB");
                inFileExists = inFile.exists();
                updateEmbedability();
            }
        } else if (ae.getSource() == button[1]) {
            filechoose.addChoosableFileFilter(new TextFileFilter());
            if (filechoose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                dataFile = filechoose.getSelectedFile();
                filename[1].setText(dataFile.getAbsolutePath());
                label[6].setText((dataFile.length() / 1024 + 1) + " KB");
                dataFileExists = dataFile.exists();
                updateEmbedability();
            }
        } else if (ae.getSource() == button[2] ||
                   ae.getSource() == steg.toolbutton[5]) {
            String f1 = filename[0].getText();
            String f2 = filename[1].getText();
            if (validInput()) {
                operationStarted();
                embed = new Embed();
                boolean result = embed.encodeFile(f1, f2);
                operationComplete();
                if (result) {
                    JOptionPane.showInternalMessageDialog(steg.desk,
                                                  "Data file embeded successfully in file :\n" +
                                                  f1,
                                                  "Operation Successful",
                                                  JOptionPane.
                                                  INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showInternalMessageDialog(steg.desk, embed.getMessage(),
                                                  "Operation Failed",
                                                  JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (ae.getSource() == button[3]) {
            for (int i = 0; i < 2; i++) {
                filename[i].setText("");
            }
            for (int i = 4; i < 7; i++) {
                label[i].setText("");
            }
            isEmbeddable = false;
            inFileExists = false;
            outFileExists = false;
            dataFileExists = false;
            button[4].setEnabled(false);
        } else if (ae.getSource() == button[4] ||
                   ae.getSource() == steg.toolbutton[7]) {
            if (!steg.win5_live) {
                steg.send = new Sender(new File(filename[0].getText()), steg);
                steg.desk.add(steg.send);
                try {
                    steg.send.setVisible(true);
                    steg.send.setSelected(true);
                    this.dispose();
                } catch (Exception e) {}
            } else {
                try {
                    steg.send.setFile(new File(filename[0].getText()));
                    steg.send.setIcon(false);
                    steg.send.moveToFront();
                    steg.send.setSelected(true);
                } catch (Exception e) {}
            }
            steg.win2_live = false;
            this.dispose();
        } else if (ae.getSource() == button[5]) {
            steg.win2_live = false;
            this.dispose();
        }
    }


    private void updateEmbedability() {
        //required= header,4 bytes for filename size, filenamelength x 4 ,16 bytes for file length
        //          and filelength x 4 and key x 4
        if (dataFileExists) {
            requiredFileSize = HEADER +
                               (1 + dataFile.getName().length() + 4 + 1 +
                                dataFile.length()) * 4;
        } else {
            requiredFileSize = 0;
        }
        label[4].setText("Minimum input file size required: " +
                         requiredFileSize + " B (" +
                         (requiredFileSize / 1024 + 1) + " Kb)");
        if (inFileExists && dataFileExists) {
            if (inFile.length() > requiredFileSize) {
                isEmbeddable = true;
            } else {
                isEmbeddable = false;
            }
        }
    }

    private boolean validInput() {

        if (!inFileExists) {
            JOptionPane.showInternalMessageDialog(steg.desk,
                                          "Please choose the input file!",
                                          "Input file required.",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!dataFileExists) {
            JOptionPane.showInternalMessageDialog(steg.desk,
                                          "Please choose the data file!",
                                          "Data file required.",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!isEmbeddable) {
            JOptionPane.showInternalMessageDialog(steg.desk,
                                          "Data file is too large to be embedded in input file\n" +
                                          "Please choose a larger input file.",
                                          "Data File Unembeddable!",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void operationStarted() {
        label[4].setText("Embeding the file ..... ");
        for (int i = 0; i <= 3; i++) {
            button[i].setEnabled(false);
        }
        steg.toolbutton[5].setEnabled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)) ;
    }

    private void operationComplete() {
        for (int i = 0; i <= 4; i++) {
            button[i].setEnabled(true);
        }
        steg.toolbutton[5].setEnabled(true);
        label[4].setText("");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) ;
    }
}
