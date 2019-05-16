import java.sql.SQLException;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        //System.getProperties().list(System.out);
        try {
            new PostgresJuggler().createTables();
        } catch (SQLException ex) {
        }

        new FileChooserDemo();
        FrameBuilder frameBuilder = new FrameBuilder("asf", new PostgresJuggler());
    }
}
