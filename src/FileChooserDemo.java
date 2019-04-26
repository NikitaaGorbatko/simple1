import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;

public class FileChooserDemo extends JPanel {
    static private final String newline = "\n";
    private JButton openButton, saveButton;
    private JTextArea log;
    //private JFileChooser fc;

    public FileChooserDemo() {
        super(new BorderLayout());
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        //fc = new JFileChooser();
        //fc.setMultiSelectionEnabled(true);
        //fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //fc.setControlButtonsAreShown(Boolean.FALSE);
        openButton = new JButton("Open a File...");
        openButton.addActionListener(new OpenBtnClick());
        saveButton = new JButton("Save a File...");
        saveButton.addActionListener(new SaveBtnClick());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }


    class FileValChecker {
        protected File checkFileVal() {
            //int returnVal = fc.showOpenDialog(FileChooserDemo.this);
            //File file = null;
            ///if (returnVal == JFileChooser.APPROVE_OPTION) {
            //    file = fc.getSelectedFile();
            //    System.out.println("Opening: " + file.getName() + "." + newline);
            //} else {
            //    System.out.println("Open command cancelled by user." + newline);
            //}
            return null;
        }
    }

    class OpenBtnClick extends FileValChecker implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File file = checkFileVal();
        }
    }

    class SaveBtnClick extends FileValChecker implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File file = checkFileVal();
        }
    }


    /*private static void createAndShowGUI() {
        JFrame frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new FileChooserDemo());
        frame.pack();
        frame.setVisible(true);
    }*/

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }*/
}
