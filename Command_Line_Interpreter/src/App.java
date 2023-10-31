import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // Terminal terminal = new Terminal();
        // // Test cd method
        // System.out.println("Current Directory: " + terminal.pwd());
        // Change to the previous directory
        // String[] cdArgs = {".."};
        // terminal.cd(cdArgs);
        // System.out.println("Current Directory after cd: " + terminal.pwd())
        // // Attempt to go to the previous directory from the root directory
        // terminal.cd(cdArgs);
        // Change to the home directory
        // String[] cdHomeArgs = {};
        // terminal.cd(cdHomeArgs);
        // System.out.println("Current Directory after cd home: " + terminal.pwd());

        // terminal.mkdir("C:\\Users\\abdel\\fcai-year 3\\OS\\assignment1
        // repo\\command-line-interpreter\\test1","\\test2");
        // terminal.rmdir("C:\\Users\\abdel\\fcai-year 3\\OS\\assignment1
        // repo\\command-line-interpreter\\test1");

        System.out.println("Enter command: ");
        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser();
        Terminal terminal = new Terminal();

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }


            if (parser.parse(input)) {
                String commandName = parser.getCommandName();
                String[] commandArgs = parser.getArgs();

                // Check if the command is 'cp' and it includes '-r' as an argument
                if (commandName.equals("cp") && commandArgs.length > 0 && commandArgs[0].equals("-r")) {
                    // Remove the '-r' from the arguments
                    String[] newArgs = new String[commandArgs.length - 1];
                    System.arraycopy(commandArgs, 1, newArgs, 0, commandArgs.length - 1);
                    // Call the cp_r method
                    terminal.cp_r(newArgs);
                } else {
                    // Handle commands based on commandName
                    switch (commandName) {
                        case "echo":
                            String output = terminal.echo(commandArgs);
                            System.out.println(output);
                            break;
                        case "pwd":
                            String currentDirectory = terminal.pwd();
                            System.out.println(currentDirectory);
                            break;
                        case "cd":
                            terminal.cd(commandArgs);
                            break;
                        case "ls":
                            List<String> fileList = terminal.ls();
                            fileList.forEach(System.out::println);
                            break;
                        case "cp":
                            Terminal.cp(commandArgs);
                            break;
                        case "cat":
                            terminal.cat(commandArgs);
                            break;
                        case "rm":
                            terminal.rm(commandArgs);
                            break;
                        default:
                            System.out.println("Command not supported: " + commandName);
                            break;
                    }
                }
            } else {
                System.out.println("Invalid command format.");
            }
        }
        scanner.close();
    }
}