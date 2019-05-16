import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FileChooserDemo extends JPanel {
    static private final String newline = "\n";
    private JButton openButton, saveButton;
    private JTextArea log;

    public FileChooserDemo() {
        super(new BorderLayout());
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
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
}
