import java.io.*;

public abstract class ArticleParser {
    public static File parseArticle(File articleFile) {
       File parsedFile = new File(articleFile.getAbsolutePath().substring(0,
               articleFile.getAbsolutePath().length() - articleFile.getName().length())
               + "parsed_" + articleFile.getName());
       char previousChar = ' ';
       try {
           String line;
           BufferedReader articleReader = new BufferedReader(new FileReader(articleFile));
           BufferedWriter articleWriter = new BufferedWriter(new FileWriter(parsedFile));
           while ((line = articleReader.readLine()) != null) {
               for (int i = 0; i < line.length(); i++) {
                   char a = line.charAt(i);
                   if (previousChar == ' ' && a == '-' || !Character.isLetter(a) && a != '’' && a != '-')
                       line = line.replace(a,' ');
                   previousChar = a;
               }
               for (String token : line.split(" ")) {
                   if (!token.equals("")) {
                       articleWriter.write(token.toLowerCase().trim() + "\n");
                   }
               }
           }
           articleWriter.flush();
           articleReader.close();
           articleWriter.close();
       } catch (IOException ex) {
           System.out.println("file '" + articleFile.getName() + "' was not parsed.");
       }
       return parsedFile;
    }
}
