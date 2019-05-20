import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FrameBuilder extends Frame {

    private JFrame frame;
    private JLabel fileLabel, createTopicLabel, pickTopicLabel, parsedFileLabel, pickLangLabel;
    private JTextField fileField, newTopicField, parsedFileFiled;
    private PostgresJuggler postgresJuggler;
    private JScrollPane topicsListScrollPane, languagesListScrollPane;
    private DefaultListModel topicNames, langList;
    private JList topicsList, languagesList;


    FrameBuilder(String name, PostgresJuggler postgresJuggler) {
        this.postgresJuggler = postgresJuggler;
        Font font = new Font("", Font.BOLD,18);
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,580);
        frame.setResizable(false);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);

        frame.setLocation(x, y);
        frame.setLayout(new FlowLayout(FlowLayout.LEADING));

        fileField = new JTextField(21);
        parsedFileFiled = new JTextField(21);
        newTopicField = new JTextField(21);
        fileLabel = new JLabel("file direction");
        parsedFileLabel = new JLabel("parsed file direction");
        pickTopicLabel = new JLabel("pick a topic from the list below");
        pickLangLabel = new JLabel("pick a language from the list below");
        createTopicLabel = new JLabel("create new topic");
        //name descrioption cost


        pickLangLabel.setFont(font);
        pickTopicLabel.setFont(font);
        fileLabel.setFont(font);
        fileField.setFont(font);
        parsedFileFiled.setFont(font);
        newTopicField.setFont(font);
        parsedFileLabel.setFont(font);
        createTopicLabel.setFont(font);

        topicNames = new DefaultListModel();
        try {
            ArrayList<String> array = postgresJuggler.getTopics();
            for (String item : array) {
                topicNames.addElement(item);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        langList = new DefaultListModel();
        try {
            ArrayList<String> array = postgresJuggler.getLanguages();
            for (String item : array) {
                langList.addElement(item);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        topicsList = new JList(topicNames);
        topicsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        topicsList.setSelectedIndex(0);
        topicsList.setVisibleRowCount(5);
        topicsList.setFont(font);
        topicsListScrollPane = new JScrollPane(topicsList);
        topicsListScrollPane.setWheelScrollingEnabled(true);
        topicsListScrollPane.setWheelScrollingEnabled(true);

        languagesList = new JList(langList);
        languagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        languagesList.setSelectedIndex(0);
        languagesList.setVisibleRowCount(5);
        languagesList.setFont(font);
        languagesListScrollPane = new JScrollPane(languagesList);
        languagesListScrollPane.setWheelScrollingEnabled(true);
        languagesListScrollPane.setWheelScrollingEnabled(true);

        JButton parseBtn = new JButton("Parse");
        JButton addTopicBtn = new JButton("Add new topic");
        JButton searchBtn = new JButton("Search File");

        parseBtn.setFont(font);
        searchBtn.setFont(font);
        addTopicBtn.setFont(font);

        Container frameContainer = frame.getContentPane();
        frameContainer.add(fileLabel);
        frameContainer.add(fileField);
        frameContainer.add(parseBtn);
        frameContainer.add(searchBtn);
        frameContainer.add(createTopicLabel);
        frameContainer.add(newTopicField);
        frameContainer.add(addTopicBtn);
        frameContainer.add(pickTopicLabel);
        frameContainer.add(topicsListScrollPane);
        frameContainer.add(parsedFileLabel);
        frameContainer.add(parsedFileFiled);
        frameContainer.add(languagesListScrollPane);


        parseBtn.addActionListener(new OnParseBtnClick());
        searchBtn.addActionListener(new OnFileChooserBtnClick());
        addTopicBtn.addActionListener(new OnAddTopicBtnCLick());

        frame.setVisible(true);
        topicsList.setFixedCellWidth(newTopicField.getWidth() - 10);
        languagesList.setFixedCellWidth(newTopicField.getWidth() - 10);

    }

    class OnParseBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String fileDirection = fileField.getText();
            try {
                final String parsedFileString = ArticleParser.parseArticle(new File(fileDirection), postgresJuggler, "").getAbsolutePath();
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
                parsedFileFiled.setText(parsedFileString);
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
                    topicNames.addElement(topicText);
                    topicsList = new JList(topicNames);
                    topicsListScrollPane.revalidate();
                    topicsListScrollPane.repaint();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(isDone);
        }
    }

    class OnFileChooserBtnClick extends JFileChooser implements ActionListener{
        JFileChooser fileChooser = new JFileChooser();
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fileChooser.showOpenDialog(this.getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            } else {
                System.out.println("Open command was cancelled by user.");
            }
        }
    }
}
