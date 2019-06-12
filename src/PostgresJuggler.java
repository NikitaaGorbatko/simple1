
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dummy.DummyItem;

public class PostgresJuggler {
    private final String URL = "jdbc:postgresql://localhost/postgres?user=postgres&password=1";
    private Connection connection;
    private Statement statement;

    PostgresJuggler() throws SQLException{
        connection = DriverManager.getConnection(URL);
        statement = connection.createStatement();
    }

    public boolean createNewTopic(String topicName) throws SQLException {
        checkStatement();
        statement.execute("INSERT INTO topics (top) VALUES ('" + topicName +"')");
        statement.close();
        return true;
    }

    public ArrayList<String> getTopics() throws SQLException {
        checkStatement();
        connection.setAutoCommit(false);
        ArrayList<String> topicsArrayList = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.topics;");
        while (resultSet.next())
        {
            topicsArrayList.add(resultSet.getString(1));
        }
        connection.setAutoCommit(true);
        resultSet.close();
        statement.close();
        return topicsArrayList;
    }

    public ArrayList<Language> getLanguages() throws SQLException {
        checkStatement();
        connection.setAutoCommit(false);
        ArrayList<Language> langsArray = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.languages;");
        while (resultSet.next())
        {
            langsArray.add(new Language(resultSet.getString(1), resultSet.getString(2)));
        }
        connection.setAutoCommit(true);
        resultSet.close();
        statement.close();
        return langsArray;
    }

    public void createTables() throws SQLException {
        checkStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS languages (" +
                "lang VARCHAR(100) PRIMARY KEY," +
                "key VARCHAR(5));\n");

        statement.execute("CREATE TABLE IF NOT EXISTS topics (" +
                "top VARCHAR(100) UNIQUE NOT NULL PRIMARY KEY);\n");

        //statement.execute("INSERT INTO topics (top) VALUES ('base');");

        statement.execute("CREATE TABLE IF NOT EXISTS word_sets (" +
            "word_set_id SERIAL PRIMARY KEY,\n" +
            "lang_id VARCHAR(100) REFERENCES languages(lang),\n" +
            "topic_id VARCHAR(100) REFERENCES topics(top),\n" +
            "name VARCHAR(120) UNIQUE NOT NULL,\n" +
            "description VARCHAR(250) UNIQUE NOT NULL,\n" +
            "data TEXT[] NOT NULL,\n" +
            "cost INTEGER);\n");

        statement.close();
    }

    public List<String> getBaseWordBlock(String language) throws SQLException {
        checkStatement();
        connection.setAutoCommit(false);
        List<String> baseOfLanguage = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT unnest(data) FROM word_sets WHERE lang_id LIKE '" + language + "' AND topic_id LIKE 'base';");
        while (resultSet.next())
        {
            baseOfLanguage.add(resultSet.getString(1));
        }
        connection.setAutoCommit(true);
        resultSet.close();
        statement.close();
        return baseOfLanguage;
    }

    public List<DummyItem> getWordBlocks() throws SQLException {
        checkStatement();
        connection.setAutoCommit(false);
        List<DummyItem> wordBlocks = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM word_sets;");
        while (resultSet.next())
        {
            wordBlocks.add(new DummyItem(resultSet.getString(1),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    resultSet.getInt(7),
                    resultSet.getString(2),
                    resultSet.getString(6)));
        }
        connection.setAutoCommit(true);
        resultSet.close();
        statement.close();
        return wordBlocks;
    }

    private void checkStatement() throws SQLException {
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }
    }

    public boolean createNewSet(String language, String topic, String name, String description, List<String> data, int cost) throws SQLException {
        checkStatement();
        String dataString = "{";
        for (String base : data) {
            dataString += "\"" + base + "\",";
        }
        dataString = dataString.substring(0, dataString.length() - 1);
        dataString += "}";
        boolean done = statement.execute("INSERT INTO word_sets (lang_id, topic_id, name, description, data, cost) VALUES ('" + language + "', '" + topic + "', '" + name + "', '" + description +"', '" + dataString + "', '" + cost + "');");
        statement.close();
        return done;
    }
}
