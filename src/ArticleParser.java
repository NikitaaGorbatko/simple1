import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ArticleParser {
    final static String PARSED_FILE_PREFIX = "parsed_";

    public static File parseArticle(File articleFile) throws IOException {
       File parsedFile = new File(articleFile.getAbsolutePath().substring(0,
               articleFile.getAbsolutePath().length() - articleFile.getName().length())
               + PARSED_FILE_PREFIX + articleFile.getName());
       String line;
       BufferedReader articleReader = new BufferedReader(new FileReader(articleFile));
       BufferedWriter articleWriter = new BufferedWriter(new FileWriter(parsedFile));
       List<String> wordList = new ArrayList<>();
       while ((line = articleReader.readLine()) != null) {
           for (int i = 0; i < line.length(); i++) {
               char a = line.charAt(i);
               if (!Character.isLetter(a) && a != '’')
                   line = line.replace(a,' ');
           }
           for (String token : line.split(" ")) {
               if (!token.equals("")) {
                   //articleWriter.write(token.toLowerCase().trim() + "\n");
                   //bufferString += token.toLowerCase().trim() + "\n";
                   wordList.add(token.toLowerCase());
                   /*articleWriter.write(" statement.execute(\"INSERT INTO Languages (lang)\\n\" +\n" +
                           "\" VALUES ('" + token.toLowerCase().trim() +"'); \\n\");" + "\n");*/
               }
           }
       }
       wordList = removeDuplicates(wordList);

       for (String waiter : wordList) {
           articleWriter.write(waiter + "\n");
       }
       List<String> list2 = new ArrayList<>();
       removeDublicates(wordList, list2);
       articleWriter.flush();
       articleReader.close();
       articleWriter.close();
       return parsedFile;
    }



    private static List<String> removeDuplicates(List<String> base) {
        return base.stream().distinct().collect(Collectors.toList());
    }

    public static ArrayList<String> readFile(String fileDirection) throws IOException{
        File file = new File(fileDirection);
        String line;
        BufferedReader articleReader = new BufferedReader(new FileReader(file));
        List<String> wordList = new ArrayList<>();

        while ((line = articleReader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                char a = line.charAt(i);
                if (!Character.isLetter(a) && a != '’')
                    line = line.replace(a,' ');
            }
            for (String token : line.split(" ")) {
                if (!token.equals("")) {
                    wordList.add(token.toLowerCase());
                }
            }
        }
        ArrayList<String> wordsArray = new ArrayList<>();
        wordList = removeDuplicates(wordList);
        for (String waiter : wordList) {
            wordsArray.add(waiter);
        }

        return wordsArray;

    }
    private static List<String> removeDublicates(List<String> base, List<String> subtrahend) {
        base = base.stream().distinct().collect(Collectors.toList());
        base.removeAll(subtrahend);
        for (String waiter : base) {
            System.out.println(waiter);//apparently works!! this overloaded method is for erasing all mathses from lists.
        }
        return base;
    }
}
