package reverseenginerring;

public class Main {

    public static void main(String args[]) {
        ReverseEngineering erd = new ReverseEngineering();
        try {
            erd.reverseEngineering("Database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
