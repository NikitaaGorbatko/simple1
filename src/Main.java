import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        char a = 0;
        /*ArticleParser.parseArticle(new File("code.txt"));
        ArticleParser.parseArticle(new File("base.txt"));
        ArticleParser.parseArticle(new File("business.txt"));
        ArticleParser.parseArticle(new File("business2.txt"));*/
        start();
    }

    private static void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //UIManager.put("swing.boldMetal", Boolean.FALSE);
                FrameBuilder as = new FrameBuilder("Choose txt file to parse");
            }
        });
    }
}
