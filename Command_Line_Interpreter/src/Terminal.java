import java.nio.file.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("ALL")
/**
 * The Terminal class is used to handle the user input and execute the commands.
 */
public class Terminal {
    /**
     * The parser object is used to parse the user input into a command name and arguments.
     */
    Parser parser;
    /**
     * The commandHistory list is used to store the commands entered by the user.
     */
    private List<String> commandHistory = new ArrayList<>();

    /**
     * The Terminal constructor initializes the parser object.
     */
    public Terminal() {
        parser = new Parser();
    }
    
  
    /**
     * Concatenates the arguments and prints them.
     * 
     * @param args An array of strings representing arguments.
     * @return String
     * 
     */
    public String echo(String[] args) {
        String output = "";
        for (int i = 0; i < args.length; i++) {
            output += args[i] + " ";
        }
        return output;
    }

  
    /**
     * Returns the current directory.
     * @return String which is the current directory
     */
    public String pwd() {
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory;
    }

    /**
     * Changes the current directory to the specified directory.
     *
     * @param  args An array of strings representing arguments.
     * @return void
     * 
     */
    public void cd(String[] args) {
        /*
        * cd takes no arguments and changes the current path to the path
        * of your home directory
        */
        if (args.length == 0) {
            System.setProperty("user.dir", System.getProperty("user.home"));
        }
        /*
        * cd takes 1 argument which is “..” (e.g. cd ..) and changes the
        * current directory to the previous directory.
        * example:
        * Current directory: /home/user/Documents
        * Using the command: cd ..
        * New directory: /home/user
        */
        else if (args[0].equals("..")) {
            String currentDirectory = pwd();
            File file = new File(currentDirectory);
            String parentDirectory = file.getParent();
            if (parentDirectory != null) {
                System.setProperty("user.dir", parentDirectory);
            }
        }
        /*
        *cd takes 1 argument which is a path to a directory (e.g. cd
        * /home/user/Documents) and changes the current directory to the
        * specified directory.
        */
        else {
            /* Change the current directory to the specified directory */
            String newDirectory = args[0];
            File targetDirectory = new File(newDirectory);
            if (targetDirectory.isAbsolute()) {
                /* If it's an absolute path, set it directly */
                System.setProperty("user.dir", newDirectory);
            } else {
                /* If it's a relative path, resolve it from the current directory*/ 
                String currentDirectory = System.getProperty("user.dir");
                File resolvedDirectory = new File(currentDirectory, newDirectory);
                if (resolvedDirectory.exists() && resolvedDirectory.isDirectory()) {
                    System.setProperty("user.dir", resolvedDirectory.getAbsolutePath());
                } else {
                    System.err.println("Directory '" + newDirectory + "' not found.");
                }
            }
        }
        String currentDirectory = pwd();
        System.out.println(currentDirectory);

    }

    /**
    * lists the contents of the current directory
    * sorted alphabetically.
    * @return list of string which is the data in the directory sorted
    */
    public List<String> ls() {
        List<String> fileList = new ArrayList<>();
        File directoryPath = new File(pwd());
        String[] contents = directoryPath.list();
        Arrays.sort(contents);
        for (int i = 0; i < contents.length; i++) {
            fileList.add(contents[i]);
        }
        return fileList;
    }

    /**
    * lists the contents of the current directory
    * sorted alphabetically in reverse order.
    * @return list of string which is the data in the directory sorted in reverse order
    */
    public List<String> ls_r() {
        List<String> fileList =  ls();
        List<String> fileList_reverse =  new ArrayList<>() ;
        for (int i=fileList.size()-1;i>=0;i--){
                fileList_reverse.add(fileList.get(i));
        }
        return fileList_reverse;
    }

    /**
     * 
     * @param str the string to be checked if it is a full path or not
     * @return
     */
    public boolean FullPath(String str) {
        return str.contains("C:\\Users");
    }

    /**
    * ... for creating any number of paths
    * @param paths array of strings
    * @return void
    */
    public void mkdir(String... paths) {
//        for (String path : paths) {
//            File directory = new File(path);
//            if (!directory.exists()) {
//                directory.mkdir();
//            } else {
//                System.out.println("mkdir: cannot create directory " + path + ": File exists");
//            }
//        }
        for (String path : paths) {
            boolean flag;
            if (!FullPath(path)) {
                /**
                * if the given string is directory name not a full path
                * a new directory is created in the current directory.
                */
                String new_directory = pwd() + path;
                flag = new File(new_directory).mkdir();
            } else {
                /*In case the given string is the full path, create the directory*/
                flag = new File(path).mkdir();
            }

            if (!flag) {
                System.out.println("Directory is not created");
            }
        }
    }

    /**
     *  remove empty files in a directory
     *  @param current_directory the directory to be checked
     *  @return void
     */
    public void RemoveEmptyFiles(File current_directory) {
        /* exception is thrown if the directory is empty */
        for (File file : Objects.requireNonNull(current_directory.listFiles())) {
            if (file.isDirectory() && Objects.requireNonNull(file.list()).length == 0) {
                boolean deleted = file.delete();
                if (!deleted)
                    System.out.println("Directory " + file + " is not deleted");
            }
        }
    }

    /**
     * removes a directory
     * @param path string of the directory name
     * @return void
     */
    public void rmdir(String... paths) {
        /*
         * if the given argument = "*"
         * remove all the empty directories in the current directory
         */
        File current_directory;
        if (paths[0].equals("*")) {
            current_directory = new File(pwd());
            RemoveEmptyFiles(current_directory);
        } else {
//            for (String path: paths) {
//                File directory = new File(path);
//                if (directory.exists() && directory.isDirectory()) {
//                    if (directory.list().length == 0) {
//                        if (!directory.delete()) {
//                            System.err.println("Failed to delete: " + path);
//                        }
//                    } else {
//                        System.err.println("rmdir: failed to remove " + path + ": Directory not empty");
//                    }
//                } else {
//                    System.err.println("rmdir: failed to remove " + path + ": No such directory");
//                }
//            }
            for (String path : paths) {
                if (FullPath(path)) {
                    current_directory = new File(path);
                    current_directory.delete();
                }
                String full_path = new File(path).getAbsolutePath();
                current_directory = new File(full_path);
                boolean deleted = current_directory.delete();
                if (!deleted)
                    System.out.println("Directory " + path + " is not deleted");
            }
        }
    }

    /**
     * 
     * @param path the path of the file
     * @return void
     */
    public void touch(String fileName) {
        Path filePath = Path.of(fileName);
        try {
            if (Files.exists(filePath)) {
                // if the file exists update its access to append
                Files.write(filePath, new byte[0], StandardOpenOption.APPEND);
            } else {
                // create file if not exists
                Files.createFile(filePath);
            }
            System.out.println("File created or updated: " + filePath.toString());
        } catch (IOException e) {
            System.err.println("An error occurred");
        }
    }

    /**
     * Copies one or more files to a directory.
     *
     * If only two files are given, it copies the first onto the second.
     * It is an error if the last argument is not a directory and more than two files are given.
     *
     * @param arguments An array of strings representing arguments.
     *                  The first element should be the source file, the second element
     *                  should be the destination file or directory, and the last element
     *                  (if present) should be the destination directory.
     * @return void
     */

    public static void cp(String[] arguments) {
        if (arguments.length < 2) {
            System.out.println("Usage: cp <source> <destination> [destination_directory]");
            return;
        }

        String sourcePath = arguments[0];
        String destinationPath = arguments[1];

        /**
         * Check if the source exists
         */
        Path source = Paths.get(sourcePath);
        if (!Files.exists(source)) {
            System.out.println("cp: cannot stat " + sourcePath + " : No such file or directory");
            return;
        }

        /* Check if the last argument is a directory */
        boolean isLastArgDirectory = false;
        Path destination = Paths.get(destinationPath);

        if (arguments.length > 2) {
            String lastArg = arguments[arguments.length - 1];
            Path lastPath = Paths.get(lastArg);
            if (Files.isDirectory(lastPath)) {
                destination = lastPath;
                isLastArgDirectory = true;
            }
        }

        try {
            if (arguments.length == 2) {
                if (isLastArgDirectory) {
                    /**
                     * Error: Cannot copy multiple files to a directory with only two arguments
                     */
                    System.out.println("Error: Cannot copy multiple files to a directory with only two arguments.");
                } else {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                /**
                 * Multiple source files provided
                 */
                if (!isLastArgDirectory) {
                    /**
                     * Error: Last argument is not a directory
                     */
                    System.out.println("Error: Last argument is not a directory.");
                } else {
                    /*Copy the source files into the destination directory */ 
                    for (int i = 0; i < arguments.length - 1; i++) {
                        String file = arguments[i];
                        Path sourceFile = Paths.get(file);
                        Path targetFile = destination.resolve(sourceFile.getFileName());
                        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies a directory from the source to the destination.
     *
     * @param args An array containing two elements: the source directory path and
     *             the destination directory path.
     * @return void
     */
    public void cp_r(String[] args) {
        if (args.length == 2) {
            File sourceDir = new File(args[0]);
            File destDir = new File(args[1]);
            if (sourceDir.isDirectory() && destDir.isDirectory()) {
                copyDirectoryFiles(sourceDir, destDir);
            } else {
                System.out.println("Both source and destination should be directories");
            }
        } else {
            System.out.println("Usage: cp -r source_directory destination_directory");
        }
    }

    /**
     * Recursively copies the files and subdirectories from a source directory to a destination directory.
     *
     * @param sourceDir The source directory to copy.
     * @param destDir The destination directory where the files and subdirectories will be copied.
     * @return void
     */
    private void copyDirectoryFiles(File sourceDir, File destDir) {
        File[] sourceFiles = sourceDir.listFiles();
        if (sourceFiles != null) {
            for (File sourceFile : sourceFiles) {
                File destFile = new File(destDir, sourceFile.getName());

                if (sourceFile.isDirectory()) {
                    destFile.mkdir();
                    copyDirectoryFiles(sourceFile, destFile);
                } else {
                    try {
                        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Concatenates the content of files and prints it.
     *
     * If no file arguments are provided, this method reads input from the user and prints it to the console.
     * If file arguments are provided, it reads and prints the content of each specified file.
     *
     * Use "exit" to exit the user input mode.
     *
     * @param fileNames An array of file names.
     * @return void
     */
    public void cat(String[] fileNames) {
        if (fileNames.length == 0) {
            /* Read input from the user and print it */
            /* if user doesn't enter filename */
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = reader.readLine()) != null) {
                    /**
                     * Check if the user wants to exit
                     */
                    if ("exit".equalsIgnoreCase(line)) {
                        break;
                    }
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            /**
            * Concatenate and print the content of files
            */
            int index = 0;
            for (String fileName : fileNames) {
                index++;
                File file = new File(fileName);
                if (file.exists() && file.isFile()) {
                    /*File exists, so print its content*/ 
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                        System.out.print(",in file " + index + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("File '" + fileName + "' does not exist.");
                }
            }
        }
    }

    /**
     * Remove files specified by their names.
     *
     * If a file exists, it checks whether it's a file or a directory.
     * If it's a directory, it cannot be removed.
     * If it's a file, the method attempts to delete it.
     *
     *
     * @param fileNames An array of file names to remove.
     * @return void
     */
    public void rm(String[] fileNames) {
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (file.exists()) {
                if (file.isDirectory()) {
                    System.out.println("rm: cannot remove " + fileName + ": is a directory");
                } else {
                    if (!file.delete()) {
                        System.err.println("Error removing file " + fileName);
                    }
                }
            } else {
                System.err.println("File "+ fileName+ "is not found");
            }
        }
    }


    /**
     * displays an enumerated list with the 
     * commands you’ve used in the past
     * 
     * @return void
     */
    public void history(){
        for (int i = 0; i < commandHistory.size(); i++) {
            System.out.println((i+1)+"   "+commandHistory.get(i));
        }
    }

    /**
    * This method will choose the suitable command method to be called
    * based on the user input.
    *
    * @return void
    */
    public void chooseCommandAction() {
        System.out.println("Enter command: ");
        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser();

        while (true) {
            String input = scanner.nextLine();
           
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            if (parser.parse(input)) {
                String commandName = parser.getCommandName();
                String[] commandArgs = parser.getArgs();

                String history=commandName+" ";
                for (int i = 0; i < commandArgs.length; i++) {
                    history += commandArgs[i] + " ";
                }
                commandHistory.add(history);

                /**
                 * Check if the command is 'cp' and it includes '-r' as an argument
                 */
                if (commandName.equals("cp") && commandArgs.length > 0 && commandArgs[0].equals("-r")) {
                    /* Remove the '-r' from the arguments*/
                    String[] newArgs = new String[commandArgs.length - 1];
                    System.arraycopy(commandArgs, 1, newArgs, 0, commandArgs.length - 1);
                    /*Call the cp_r method*/
                    cp_r(newArgs);
                }
                /**
                 * Check if the command is 'ls' and it includes '-r' as an argument
                 */
                else if (commandName.equals("ls") && commandArgs.length > 0 && commandArgs[0].equals("-r")) {
                    List<String> fileList_lsr = ls_r();
                    fileList_lsr.forEach(System.out::println);
                }
                else {
                    /**
                     * Handle commands based on commandName
                     */
                    switch (commandName) {
                        case "echo":
                            String output = echo(commandArgs);
                            System.out.println(output);
                            break;
                        case "pwd":
                            String currentDirectory = pwd();
                            System.out.println(currentDirectory);
                            break;
                        case "cd":
                            cd(commandArgs);
                            break;
                        case "ls":
                            List<String> fileList = ls();
                            fileList.forEach(System.out::println);
                            break;
                        case "cp":
                            cp(commandArgs);
                            break;
                        case "cat":
                            cat(commandArgs);
                            break;
                        case "rm":
                            rm(commandArgs);
                            break;
                        case "mkdir":
                            mkdir(commandArgs);
                            break;
                        case "rmdir":
                            rmdir(commandArgs);
                            break;
                        case "history":
                            history();
                            break;
                        case "touch":
                            touch(commandArgs[0]);
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