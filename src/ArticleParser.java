import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ArticleParser {
    final private static String PARSED_FILE_PREFIX = "parsed_";

    public static File parseArticle(File articleFile, PostgresJuggler postgresJuggler, String language) throws IOException {
       File parsedFile = new File(articleFile.getAbsolutePath().substring(0,
               articleFile.getAbsolutePath().length() - articleFile.getName().length())
               + PARSED_FILE_PREFIX + articleFile.getName());
       BufferedReader articleReader = new BufferedReader(new FileReader(articleFile));
       BufferedWriter articleWriter = new BufferedWriter(new FileWriter(parsedFile));
       List<String> wordList = retrieveWordsFromBufferedReader(articleReader);
       wordList = removeDuplicates(wordList);
       try {
           wordList = removeDublicates(wordList, postgresJuggler.getBaseWordBlock(language));
       } catch (SQLException ex) {
           System.out.println(ex.getMessage());
       }
       for (String waiter : wordList)
           articleWriter.write(waiter + "\n");
       //articleWriter.flush();
       articleWriter.close();
       articleReader.close();
       return parsedFile;
    }

    public static File parseArticle(File articleFile) throws IOException {
        File parsedFile = new File(articleFile.getAbsolutePath().substring(0,
                articleFile.getAbsolutePath().length() - articleFile.getName().length())
                + PARSED_FILE_PREFIX + articleFile.getName());
        BufferedReader articleReader = new BufferedReader(new FileReader(articleFile));
        BufferedWriter articleWriter = new BufferedWriter(new FileWriter(parsedFile));
        List<String> wordList = retrieveWordsFromBufferedReader(articleReader);


        for (int i = 0; i < wordList.size() - 1; i+=2) {
            String sql = "";
            if (wordList.get(i).equals("hill")) {
                sql = "INSERT INTO languages (lang, key) VALUES ('"+ wordList.get(i) + " " + wordList.get(i+1) + "', '"+ wordList.get(i + 2) + "');";
                i++;
            } else {
                sql = "INSERT INTO languages (lang, key) VALUES ('"+ wordList.get(i) + "', '"+ wordList.get(i + 1) + "');";
            }

            articleWriter.write(sql + "\n");
        }
        //articleWriter.flush();
        articleWriter.close();
        articleReader.close();
        return parsedFile;
    }

    private static List<String> retrieveWordsFromBufferedReader(BufferedReader articleReader) throws IOException {
        List<String> list = new ArrayList<>();
        String line;
        while ((line = articleReader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                char a = line.charAt(i);
                if (!Character.isLetter(a) && a != '’')
                    line = line.replace(a,' ');
            }
            for (String token : line.split(" ")) {
                if (!token.equals("")) {
                    list.add(token.toLowerCase());
                }
            }
        }
        return list;
    }

    public static ArrayList<String> retrieveWordsFromDB(String line) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        line = line.substring(1, line.length() - 1);
        for (String token : line.split(" ")) {
            if (!token.equals("")) {
                list.add(token.toLowerCase());
            }
        }
        return list;
    }


    public static List<String> readFile(String fileDirection) throws IOException{
        File file = new File(fileDirection);
        BufferedReader articleReader = new BufferedReader(new FileReader(file));
        List<String> wordList = retrieveWordsFromBufferedReader(articleReader);
        wordList = removeDuplicates(wordList);
        articleReader.close();
        return wordList;
    }

    private static List<String> removeDuplicates(List<String> base) {
        return base.stream().distinct().collect(Collectors.toList());
    }

    private static List<String> removeDublicates(List<String> base, List<String> subtrahend) {
        base = base.stream().distinct().collect(Collectors.toList());
        base.removeAll(subtrahend);
        return base;
    }
}
