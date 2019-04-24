import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreJungler {
    private final String url = "jdbc:postgresql://localhost/postgres?user=postgres&password=1";
    private Connection connection;

    PostgreJungler() {
        try {
            reconnectIfDropped();
            make();
        } catch (SQLException ex) {
            System.out.println("\nConnection is failed\nError: " + ex.getMessage() + "\nAsk the administrator for help.");
        }
    }

    private void reconnectIfDropped() throws SQLException {
        if (connection.isClosed() || connection == null) {
            connection = DriverManager.getConnection(url);
        }
    }

    private void make() throws SQLException {
        reconnectIfDropped();
        Statement st = connection.createStatement();
        st.execute("CREATE TABLE Peasdfghrsons (\n" +
                "    PersonID int,\n" +
                "    LastName varchar(255),\n" +
                "    FirstName varchar(255),\n" +
                "    Address varchar(255),\n" +
                "    City varchar(255)\n" +
                ");");
        st.close();
    }
}
