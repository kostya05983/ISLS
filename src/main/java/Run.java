import GUI.Main;

public class Run {
    public static void main(String[] args) {
        Main main=new Main();
        Thread thread=new Thread(main);
        thread.start();
    }
}
