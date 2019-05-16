import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        FrameBuilder frameBuilder = new FrameBuilder("asf", new PostgresJuggler());
    }
}
