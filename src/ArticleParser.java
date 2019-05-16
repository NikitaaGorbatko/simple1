import java.io.*;

public abstract class ArticleParser {
    final static String PARSED_FILE_PREFIX = "parsed_";

    public static File parseArticle(File articleFile) throws IOException {
       File parsedFile = new File(articleFile.getAbsolutePath().substring(0,
               articleFile.getAbsolutePath().length() - articleFile.getName().length())
               + PARSED_FILE_PREFIX + articleFile.getName());
       String line;
       BufferedReader articleReader = new BufferedReader(new FileReader(articleFile));
       BufferedWriter articleWriter = new BufferedWriter(new FileWriter(parsedFile));
       while ((line = articleReader.readLine()) != null) {
           for (int i = 0; i < line.length(); i++) {
               char a = line.charAt(i);
               if (!Character.isLetter(a) && a != '’')
                   line = line.replace(a,' ');
           }
           for (String token : line.split(" ")) {
               if (!token.equals("") && token.length() > 3) {
                   articleWriter.write(" statement.execute(\"INSERT INTO Languages (lang)\\n\" +\n" +
                           "\" VALUES ('" + token.toLowerCase().trim() +"'); \\n\");" + "\n");
               }
           }
       }
       articleWriter.flush();
       articleReader.close();
       articleWriter.close();
       return parsedFile;
    }
}
