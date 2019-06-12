import dummy.DummyItem;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
    private JButton runServerButton;
    private JTextPane messagePane;
    private PostgresJuggler postgresJuggler;

    MainForm(PostgresJuggler postgresJuggler) {
        this.postgresJuggler = postgresJuggler;
        setTitle("Manager");
        setSize(370, 310);
        add(panel1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
        try {
            ArrayList<Language> languages = postgresJuggler.getLanguages();
            for (Language language : languages) {
                languagesBox.addItem(language.getLanguage());
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
        runServerButton.addActionListener(new OnRunServerBtnClick());
    }


    private static JSONObject jsonObject = null;
    private static String json = "";




    private JComboBox setBoxFromDB(ArrayList<String> list) {
        String[] arr = null;
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        JComboBox box = new JComboBox(arr);
        return box;
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
                boolean wasCreated = postgresJuggler.createNewSet(language, topic, name, description, ArticleParser.readFile(fileField.getText()), cost);
                System.out.println("\nlanguage: " + language + "\nname: " + name + "\ndescription: " + description + "\ncost: " + cost + "\ntopic: " + topic + "\ncreated: " + wasCreated);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    class OnRunServerBtnClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Runnable threadServerRunnable = new ServerRunnable();
            Thread serverThread = new Thread(threadServerRunnable);
            serverThread.start();
        }
    }

    class ServerRunnable implements Runnable {
        private static final int SEND_LANGUAGES = 3;
        private static final int SEND_TOPICS = 2;
        private static final int SEND_BLOCKS = 1;
        private static final String ID_KEY = "id";
        private static final String NAME_KEY = "name";
        private static final String DESCRIPTION_KEY = "description";
        private static final String TOPIC_KEY = "topic";
        private static final String LANGUAGE_KEY = "language";
        private static final String COST_KEY = "cost";
        private static final String DATA_KEY = "data";
        private static final String LANG_NAME = "lang";
        private static final String LANG_KEY = "lang_key";

        private ServerSocket serverSocket;
        private BufferedReader incomingReader;
        private BufferedWriter outgoingWriter;

        @Override
        public synchronized void run() {
            try {
                serverSocket = new ServerSocket(4004);
                messagePane.setText("Сервер запущен!");
                while (true) {
                    Socket socket = serverSocket.accept();
                    incomingReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    outgoingWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    while(!incomingReader.ready()) { }
                    switch (incomingReader.read()) {
                        case SEND_BLOCKS:
                            JSONArray blocksArrayList = new JSONArray();
                            List<DummyItem> wordBlocks = postgresJuggler.getWordBlocks();
                            for (DummyItem localDummy : wordBlocks) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(ID_KEY, localDummy.id);
                                jsonObject.put(NAME_KEY, localDummy.name);
                                jsonObject.put(DESCRIPTION_KEY, localDummy.description);
                                jsonObject.put(TOPIC_KEY, localDummy.topic);
                                jsonObject.put(LANGUAGE_KEY, localDummy.language);
                                jsonObject.put(COST_KEY, localDummy.cost);
                                jsonObject.put(DATA_KEY, localDummy.data);
                                blocksArrayList.put(jsonObject);

                            }
                            outgoingWriter.write(blocksArrayList.toString());
                            break;
                        case SEND_TOPICS:
                            System.out.println("\n\n\n\n\n\n\n\n" + SEND_TOPICS);
                            break;
                        case SEND_LANGUAGES:


                            JSONArray langArrayList = new JSONArray();
                            ArrayList<Language> languages = postgresJuggler.getLanguages();
                            for (Language localLang : languages) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(LANG_NAME, localLang.getLanguage());
                                jsonObject.put(LANG_KEY, localLang.getKey());
                                langArrayList.put(jsonObject);
                            }
                            outgoingWriter.write(langArrayList.toString());

                            /*ArrayList<Language> languagesArray = postgresJuggler.getLanguages();
                            for (Language language : languagesArray) {
                                outgoingWriter.write( language + "\n");
                                System.out.println(language.getLanguage() + " " + language.getKey());
                            }*/
                            break;
                    }
                    //incomingReader.close();
                    outgoingWriter.flush();
                    outgoingWriter.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            } catch (SQLException ex) {
                System.err.println(ex);
            } finally {
                try {
                    messagePane.setText("Сервер закрыт!");
                    serverSocket.close();
                } catch (IOException ex) {
                    System.err.println(ex);
                }

            }
        }
    }
}
