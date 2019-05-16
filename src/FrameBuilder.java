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
    private JLabel fileLabel, createTopicLabel;
    private JTextField fileField, newTopicField;
    private PostgresJuggler postgresJuggler;
    private JScrollPane topicsListScrollPane;
    private DefaultListModel topicNames;
    private JList topicsList;


    FrameBuilder(String name, PostgresJuggler postgresJuggler) {
        this.postgresJuggler = postgresJuggler;
        Font font = new Font("", Font.BOLD,18);
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,280);
        frame.setResizable(false);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setLayout(new FlowLayout(FlowLayout.LEADING));
        fileField = new JTextField(14);
        newTopicField = new JTextField(12);
        fileLabel = new JLabel("file direction");
        fileLabel.setFont(font);
        fileField.setFont(font);
        newTopicField.setFont(font);

        createTopicLabel = new JLabel("create new topic");
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
        topicsList = new JList(topicNames);
        topicsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        topicsList.setSelectedIndex(0);
        topicsList.setVisibleRowCount(10);
        topicsList.setFont(font);

        topicsListScrollPane = new JScrollPane(topicsList);

        JButton parseBtn = new JButton("Parse");
        JButton addTopicBtn = new JButton("Add new topic");
        JButton searchBtn = new JButton("Search File");

        parseBtn.setFont(font);
        searchBtn.setFont(font);
        addTopicBtn.setFont(font);

        Container frameContainer = frame.getContentPane();
        frameContainer.add(fileLabel, 0);
        frameContainer.add(fileField, 1);
        frameContainer.add(parseBtn, 2);
        frameContainer.add(searchBtn, 3);
        frameContainer.add(createTopicLabel, 4);
        frameContainer.add(newTopicField, 5);
        frameContainer.add(addTopicBtn, 6);
        frameContainer.add(topicsListScrollPane, 7);
        parseBtn.addActionListener(new OnParseBtnClick());
        searchBtn.addActionListener(new OnFileChooserBtnClick());
        addTopicBtn.addActionListener(new OnAddTopicBtnCLick());
        frame.setVisible(true);
    }
    class OnParseBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String fileDirection = fileField.getText();
            try {
                File parsedFile = ArticleParser.parseArticle(new File(fileDirection));
                switch (System.getProperty("os.name").toLowerCase()) {
                    case "windows":
                        Runtime.getRuntime().exec("notepad " + parsedFile.getAbsolutePath());
                        break;
                    case "linux":
                        Runtime.getRuntime().exec("gedit " + parsedFile.getAbsolutePath());
                        break;
                }

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
        JFileChooser fc = new JFileChooser();
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(this.getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileField.setText(fc.getSelectedFile().getAbsolutePath());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    }
}
