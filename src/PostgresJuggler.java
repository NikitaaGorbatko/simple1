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
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }
        statement.execute("INSERT INTO topics (top) VALUES ('" + topicName +"')");
        statement.close();
        return true;
    }

    public ArrayList<String> getTopics() throws SQLException {
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }
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
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }
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
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }

        statement.execute("CREATE TABLE IF NOT EXISTS languages (" +
                "lang VARCHAR(100) PRIMARY KEY);\n");

        statement.execute("CREATE TABLE IF NOT EXISTS topics (" +
                "top VARCHAR(100) UNIQUE NOT NULL PRIMARY KEY);\n");

        //statement.execute("INSERT INTO topics (top) VALUES ('base');");

        statement.execute("CREATE TABLE IF NOT EXISTS word_sets (" +
            "word_set_id SERIAL PRIMARY KEY,\n" +
            "lang_id VARCHAR(100) REFERENCES languages(lang),\n" +
            "topic_id VARCHAR(100) REFERENCES topics(top),\n" +
            "name VARCHAR(120) NOT NULL,\n" +
            "description VARCHAR(250) UNIQUE NOT NULL,\n" +
            "data TEXT[] NOT NULL,\n" +
            "cost INTEGER);\n");

        statement.execute("INSERT INTO word_sets (lang_id, topic_id, name, description, data, cost) " +
                "VALUES ('english', (SELECT top FROM topics WHERE top='dancing'), 'english baseg', 'english base descriptiofdfdn', '{\"apple\"," +
                "\"hello\", \"goodbye\", \"hi\", \"go\"}', 150);");

        statement.close();
    }

    public void getBaseWordBlock(String language) throws SQLException {
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }
        statement.executeQuery("SELECT data");
    }

    public boolean createNewSet(String language, String topic, String name, String description, ArrayList<String> data, int cost) throws SQLException {
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }
        String dataString = "{";
        for (String base : data) {
            dataString += "\"" + base + "\",";
        }
        dataString = dataString.substring(0, dataString.length() - 1);
        dataString += "}";
        boolean a = statement.execute("INSERT INTO word_sets (lang_id, topic_id, name, description, data, cost) VALUES ('" + language + "', '" + topic + "', '" + name + "', '" + description +"', '" + dataString + "', '" + cost + "');");
        return a;
    }
}
