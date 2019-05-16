import java.sql.*;
import java.util.ArrayList;

public class PostgresJuggler {
    private final String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=1";
    private Connection connection;
    private Statement statement;

    PostgresJuggler() {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            System.out.println("\nConnection is failed\nError: " + ex.getMessage() + "\nAsk the administrator for help.");
        }
    }


    public boolean createNewTopic(String topicName) throws SQLException {
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }
        //connection.setAutoCommit(false);
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

    public void createTables() throws SQLException {
        if (statement.isClosed()) {
            statement = connection.createStatement();
        }

        statement.execute("CREATE TABLE IF NOT EXISTS languages (" +
                "lang VARCHAR(100) PRIMARY KEY);\n");

        statement.execute("CREATE TABLE IF NOT EXISTS topics (" +
                "top VARCHAR(100) UNIQUE NOT NULL PRIMARY KEY);\n");

        statement.execute("CREATE TABLE IF NOT EXISTS word_set (" +
                "word_set_id SERIAL PRIMARY KEY,\n" +
                "lang_id VARCHAR(100) REFERENCES languages(lang),\n" +
                "topic_id VARCHAR(100) REFERENCES topics(top),\n" +
                "name VARCHAR(120) NOT NULL,\n" +
                "description VARCHAR(250) UNIQUE NOT NULL,\n" +
                "data TEXT[] NOT NULL,\n" +
                "cost INTEGER);\n");

        statement.execute("INSERT INTO word_set (lang_id, name, description, data, cost) " +
                "VALUES ('english', 'business', 'The most common english words about business!', '{\"breaasdkfast\", \"consdfdfulting\"}', 150);");

        statement.close();
    }

}
