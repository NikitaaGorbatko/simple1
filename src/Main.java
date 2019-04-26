import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        char a = 0;
        /*ArticleParser.parseArticle(new File("code.txt"));
        ArticleParser.parseArticle(new File("base.txt"));
        ArticleParser.parseArticle(new File("business.txt"));
        ArticleParser.parseArticle(new File("business2.txt"));*/
        //start();
        try {
            ServerSocket serverSocket = new ServerSocket(8082);
            Thread serverThread = new Thread(() -> {
                while (true) {
                    try {
                        Socket connection = serverSocket.accept();
                        BufferedReader serverReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        Writer serverWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                        serverWriter.write("hello, " + serverReader.readLine() + "\n");
                        serverWriter.flush();
                    } catch (IOException ex) {

                    }
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();
        }catch (IOException ex) {

        }

        // client code.

        try {
            Socket clientSocket = new Socket("127.0.0.1", 8082);
            Writer clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientWriter.write("jewelsea\n");
            clientWriter.flush();
            String response = clientReader.readLine();
            System.out.println(response);
        }  catch ( IOException ex){

        }
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
