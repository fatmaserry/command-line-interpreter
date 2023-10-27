public class App {

        public static void main(String[] args) {
            Terminal terminal = new Terminal();
    
            // Test cd method
            System.out.println("Current Directory: " + terminal.pwd());
    
            // Change to the previous directory
//            String[] cdArgs = {".."};
//            terminal.cd(cdArgs);
//            System.out.println("Current Directory after cd: " + terminal.pwd());
//
//            // Attempt to go to the previous directory from the root directory
//            terminal.cd(cdArgs);
    
            // Change to the home directory
//            String[] cdHomeArgs = {};
//            terminal.cd(cdHomeArgs);
//            System.out.println("Current Directory after cd home: " + terminal.pwd());

            terminal.mkdir("C:\\Users\\abdel\\fcai-year 3\\OS\\assignment1 repo\\command-line-interpreter\\test1","\\test2");


            terminal.rmdir("C:\\Users\\abdel\\fcai-year 3\\OS\\assignment1 repo\\command-line-interpreter\\test1");

        }
    }
    

