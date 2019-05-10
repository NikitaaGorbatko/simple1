

public class Main {
    public static void main(String[] args) {
        Runnable threadServerRunnable = new ServerRunnable();
        Thread serverThread = new Thread(threadServerRunnable);
        serverThread.start();
        FrameBuilder frameBuilder = new FrameBuilder("asf");
    }
}
