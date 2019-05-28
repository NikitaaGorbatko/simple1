import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException{
        //System.getProperties().list(System.out);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainForm mainForm = new MainForm(new PostgresJuggler());
                    mainForm.setVisible(true);

                } catch (SQLException ex) {

                }
            }
        });

        try {
            PostgresJuggler pj = new PostgresJuggler();
            //pj.createTables();
            //pj.getWordBlocks();

        } catch (SQLException ex) {
            System.out.println("\nConnection is failed\nError: " + ex.getMessage() + "\nAsk the administrator for help.");
        }
    }
}
