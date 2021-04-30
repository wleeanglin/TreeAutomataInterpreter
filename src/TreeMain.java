import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TreeMain {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
        TreeMain m = new TreeMain();
        m.start();
    }

    public TreeMain(){

    }

    public void start(){
        //in case needed
        String[] args = {};
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                System.out.println(ANSI_GREEN + "[g]ui or [c]onsole" + ANSI_RESET);
                String input = reader.readLine();
                if(input.toLowerCase().equals("g") || input.toLowerCase().equals("gui")){
                    GUI.main(args);
                    System.exit(0);
                } else if(input.toLowerCase().equals("c") || input.toLowerCase().equals("console")){
                    Console c = new Console(reader);
                    System.exit(0);
                } else{
                    System.out.println(ANSI_RED + "Command not recognized" + ANSI_RESET);
                    continue;
                }
            }
        } catch(IOException e){
            System.out.println(e);
        }
    }
}
