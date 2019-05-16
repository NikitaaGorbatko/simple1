import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

public class FrameBuilder extends Frame {
    private JFrame frame;
    private JLabel fileLabel, createTopicLabel;
    private JTextField fileField, newTopicField;
    private PostgresJuggler postgresJuggler;

    FrameBuilder(String name, PostgresJuggler postgresJuggler) {
        this.postgresJuggler = postgresJuggler;
        Font font = new Font("", Font.BOLD, 18);//
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,200);
        frame.setResizable(false);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        //FlowLayout flowLayout = ;
        frame.setLayout(new FlowLayout(FlowLayout.LEADING));
        fileField = new JTextField(14);
        newTopicField = new JTextField(12);
        fileLabel = new JLabel("file direction");
        fileLabel.setFont(font);
        fileField.setFont(font);
        newTopicField.setFont(font);

        createTopicLabel = new JLabel("create new topic");
        createTopicLabel.setFont(font);

        DefaultListModel topicNames = new DefaultListModel();
        try {
            String[] array = postgresJuggler.getTopics();
            for (String item : array) {
                topicNames.addElement(item);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


        JList fruitList = new JList(topicNames);
        fruitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fruitList.setSelectedIndex(0);
        fruitList.setVisibleRowCount(3);

        JButton parseBtn = new JButton("Parse");
        JButton addTopicBtn = new JButton("Add new topic");
        JButton searchBtn = new JButton("Search File");
        parseBtn.setFont(font);
        searchBtn.setFont(font);
        addTopicBtn.setFont(font);
        //frame.setLayout(new FlowLayout());
        Container frameContainer = frame.getContentPane();

        frameContainer.add(fileLabel);
        frameContainer.add(fileField);
        frameContainer.add(parseBtn);
        frameContainer.add(searchBtn);
        frameContainer.add(createTopicLabel);
        frameContainer.add(newTopicField);
        frameContainer.add(addTopicBtn);
        frameContainer.add(fruitList);
        parseBtn.addActionListener(new OnParseBtnClick());
        searchBtn.addActionListener(new OnFileChooserBtnClick());
        addTopicBtn.addActionListener(new OnAddTopicBtnCLick());
        frame.setVisible(true);
    }

    class OnParseBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArticleParser.parseArticle(new File(fileField.getText()));
        }
    }

    class OnAddTopicBtnCLick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isDone = false;
            try {
                isDone = postgresJuggler.createNewTopic(newTopicField.getText());
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
            int returnVal = fc.showOpenDialog(new FileChooserDemo());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileField.setText(fc.getSelectedFile().getAbsolutePath());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    }
}
