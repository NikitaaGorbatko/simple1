import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<String> getLanguages() throws SQLException {
        checkStatement();
        connection.setAutoCommit(false);
        ArrayList<String> topicsArrayList = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.languages;");
        while (resultSet.next())
        {
            topicsArrayList.add(resultSet.getString(1));
        }
        connection.setAutoCommit(true);
        resultSet.close();
        statement.close();
        return topicsArrayList;
    }

    public void createTables() throws SQLException {
        checkStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS languages (" +
                "lang VARCHAR(100) PRIMARY KEY);\n");

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

        statement.execute("INSERT INTO word_sets (lang_id, topic_id, name, description, data, cost) " +
                "VALUES ('english', (SELECT top FROM topics WHERE top='dancing'), 'english baseg', 'english base descriptiofdfdn', '{\"apple\"," +
                "\"hello\", \"goodbye\", \"hi\", \"go\"}', 150);");

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

    public void getWordBlocks() throws SQLException {
        checkStatement();
        connection.setAutoCommit(false);
        List<String> baseOfLanguage = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM word_sets;");
        while (resultSet.next())
        {
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
            System.out.println(resultSet.getString(4));
            System.out.println(resultSet.getString(5));
            System.out.println(resultSet.getString(6));
            System.out.println(resultSet.getString(7));
            //baseOfLanguage.add(resultSet.getString(1));
        }
        connection.setAutoCommit(true);
        resultSet.close();
        statement.close();
        //return baseOfLanguage;
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
