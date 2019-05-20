import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField fileField;
    private JButton parseFileButton;
    private JComboBox languagesBox;
    private JTextField newTopicField;
    private JButton addTopicBtn;
    private JTextField costField;
    private JComboBox topicsBox;
    private JButton saveSetButton;
    private JTextField nameField;
    private JTextField descriptionField;
    private PostgresJuggler postgresJuggler;

    MainForm(PostgresJuggler postgresJuggler) {
        this.postgresJuggler = postgresJuggler;
        setTitle("asfd");
        setSize(370, 310);
        add(panel1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        try {
            ArrayList<String> languages = postgresJuggler.getLanguages();
            for (String language : languages) {
                languagesBox.addItem(language);
            }
            ArrayList<String> topics = postgresJuggler.getTopics();
            for (String topic : topics) {
                topicsBox.addItem(topic);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        languagesBox.setSelectedIndex(-1);
        languagesBox.addActionListener(new OnLanguageBox());
        parseFileButton.addActionListener(new OnParseBtnClick());
        addTopicBtn.addActionListener(new OnAddTopicBtnCLick());
        saveSetButton.addActionListener(new OnSaveBtn());
    }

    class OnLanguageBox implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            parseFileButton.setEnabled(true);
        }
    }

    class OnParseBtnClick implements ActionListener {
        JFileChooser fileChooser = new JFileChooser();
        @Override
        public void actionPerformed(ActionEvent e) {
            String fileDirection = "";
            int returnVal = fileChooser.showOpenDialog(getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileDirection = fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                System.out.println("Open command was cancelled by user.");
            }
            try {
                List<String> languageBaseList = new ArrayList<>();
                final String parsedFileString = ArticleParser.parseArticle(new File(fileDirection), postgresJuggler, languagesBox.getSelectedItem().toString()).getAbsolutePath();
                String osDependentCommand = "";
                switch (System.getProperty("os.name").toLowerCase()) {
                    case "windows":
                        osDependentCommand = "notepad ";
                        break;
                    case "linux":
                        osDependentCommand = "gedit ";
                        break;
                }
                Runtime.getRuntime().exec(osDependentCommand + parsedFileString);
                fileField.setText(parsedFileString);
            } catch (IOException ex) {
                System.out.println("file '" + fileDirection  + "' was not parsed.");
                System.out.println("Runtime problem");
            }
        }
    }

    class OnAddTopicBtnCLick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isDone = false;
            try {
                String topicText = newTopicField.getText();
                newTopicField.setText("");
                if (topicText.length() > 2) {
                    isDone = postgresJuggler.createNewTopic(topicText);
                    topicsBox.addItem(topicText);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(isDone);
        }
    }

    class OnSaveBtn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String language = (String) languagesBox.getSelectedItem();
                String name = nameField.getText();
                String description = descriptionField.getText();
                int cost = Integer.valueOf(costField.getText());
                String topic = (String) topicsBox.getSelectedItem();
                System.out.println("\nlanguage: " + language + "\nname: " + name + "\ndescription: " + description + "\ncost: " + cost + "\ntopic: " + topic);
                postgresJuggler.createNewSet(language, topic, name, description, ArticleParser.readFile(fileField.getText()), cost);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
