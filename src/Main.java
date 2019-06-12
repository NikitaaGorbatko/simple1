import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        //System.getProperties().list(System.out);
        //ArticleParser.parseArticle(new File("/home/nikita/languages.txt"));//It was just only for sql... It was so fun... Deadline is coming...
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
            pj.createTables();
            //pj.getWordBlocks();

        } catch (SQLException ex) {
            System.out.println("\nConnection is failed\nError: " + ex.getMessage() + "\nAsk the administrator for help.");
        }
    }
}
