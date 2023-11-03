import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Parser {

    String commandName;
    String[] args;
    /**
     * This method will divide the input into commandName and args
     * where "input" is the string command entered by the user
     */
    public boolean parse(String input) {
        String[] tokens = input.trim().split("\\s+");
        if (tokens.length > 0) {
            commandName = tokens[0];
            args = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, args, 0, args.length);
            return true;
        }
        return false;
    }

    /**
     * This method will return the commandName
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * This method will return the args
     */
    public String[] getArgs() {
        return args;
    }
}

@SuppressWarnings("ALL")
/**
 * The Terminal class is used to handle the user input and execute the commands.
 */
class Terminal {
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
     *
     * @return String which is the current directory
     */
    public String pwd() {
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory;
    }

    /**
     * Changes the current directory to the specified directory.
     *
     * @param args An array of strings representing arguments.
     * @return void
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
                // If it's an absolute path, set it directly
                System.setProperty("user.dir", newDirectory);
            } else {
                // If it's a relative path, resolve it from the current directory
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
     * Lists the contents of the current directory
     * sorted alphabetically.
     *
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
     * Lists the contents of the current directory
     * sorted alphabetically in reverse order.
     *
     * @return list of string which is the data in the directory sorted in reverse order
     */
    public List<String> ls_r() {
        List<String> fileList = ls();
        List<String> fileList_reverse = new ArrayList<>();
        for (int i = fileList.size() - 1; i >= 0; i--) {
            fileList_reverse.add(fileList.get(i));
        }
        return fileList_reverse;
    }

    /**
     * Creates new directories
     *
     * @param paths an array of paths names
     * @return void
     */
    public void mkdir(String... paths) {
        String currentDirectory = System.getProperty("user.dir");

        for (String path : paths) {
            File directory = new File(currentDirectory, path);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.out.println("Directory is not created");
                }
            } else {
                System.out.println("mkdir: cannot create directory " + path + ": File exists");
            }
        }
    }

    /**
     * Removes empty files in a directory
     *
     * @param current_directory the directory to be checked
     * @return void
     */
    public void RemoveEmptyFiles(File current_directory) {
        // exception is thrown if the directory is empty
        for (File file : Objects.requireNonNull(current_directory.listFiles())) {
            if (file.isDirectory() && Objects.requireNonNull(file.list()).length == 0) {
                boolean deleted = file.delete();
                if (!deleted)
                    System.out.println("Directory " + file + " is not deleted");
            }
        }
    }

    /**
     * Removes empty directories
     *
     * @param paths directories names
     * @return void
     */
    public void rmdir(String[] paths) {
        if (paths.length != 1) {
            System.out.println("Usage: rmdir filepath or rmdir *");
            return;
        }
        /*
         if the given argument = "*"
         remove all the empty directories in the current directory
        */
        File current_directory;
        String path = paths[0];
        if (path.equals("*")) {
            current_directory = new File(pwd());
            RemoveEmptyFiles(current_directory);
        } else {
            String currentDirectory = System.getProperty("user.dir");
            File directory = new File(currentDirectory, path);

            if (directory.exists() && directory.isDirectory()) {
                if (directory.list().length == 0) {
                    if (!directory.delete()) {
                        System.err.println("Failed to delete: " + path);
                    }
                } else {
                    System.err.println(path + "is not empty!");
                }
            } else {
                System.err.println("rmdir: failed to remove " + path + ": No such directory");
            }
        }
    }

    /**
     * Creates a new file or updates the access time of an existing file.
     *
     * If the specified file exists, this method updates its access time by appending zero bytes.
     * If the file does not exist, a new empty file is created.
     *
     * @param args An array of strings representing arguments.
     */
    public void touch(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: touch filename");
            return;
        }
        // fileName
        // The name of the file to create or update.
        String fileName = args[0];
        try {
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            Path filePath = currentDirectory.resolve(fileName);

            if (Files.exists(filePath)) {
                // If the file exists, update its access time to append
                Files.write(filePath, new byte[0], StandardOpenOption.APPEND);
            } else {
                // Create the file if it does not exist
                Files.createFile(filePath);
            }
            System.out.println("File created or updated: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
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
     *                  should be the destination file.
     * @return void
     */
    public static void cp(String[] arguments) {
        if (arguments.length != 2) {
            System.out.println("Usage: cp source_file destination_file");
            return;
        }

        String sourcePath = arguments[0];
        String destinationPath = arguments[1];

        // resolve the source and destination path according to
        // the current directory
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        Path source = currentDirectory.resolve(sourcePath);
        Path destination = currentDirectory.resolve(destinationPath);

        try {
            if (!Files.exists(source)) {
                System.err.println("cp: cannot stat '" + source + "': No such file");
                return;
            }
            if (!Files.exists(destination.getParent())) {
                Files.createDirectories(destination.getParent());
            }
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
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
        if (args.length != 2) {
            System.out.println("Usage: cp -r source_directory destination_directory");
            return;
        }
        String source = args[0];
        String destination = args[1];

        // toAbsolutePath : converts a relative path to an absolute path
        // Absolute paths start from the root of the file system
        Path currentDirectory = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        Path sourcePath = currentDirectory.resolve(source).toAbsolutePath();
        Path destinationPath = currentDirectory.resolve(destination).toAbsolutePath();

        if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
            System.err.println("Source directory does not exist");
            return;
        }

        if (!Files.exists(destinationPath) || !Files.isDirectory(destinationPath)) {
            System.err.println("Destination directory does not exist");
            return;
        }

        // create new destination with same name
        // of the old inside the previous one
        Path newDestination = destinationPath.resolve(sourcePath.getFileName());
        copyDirectoryFiles(sourcePath, newDestination);
    }

    /**
     * Recursively copies the files and subdirectories from a source directory to a destination directory.
     *
     * @param source      The path of source directory to copy.
     * @param destination The path of destination directory where the files and subdirectories will be copied.
     * @return void
     */
    private static void copyDirectoryFiles(Path source, Path destination) {
        try {
            // Convert a stream of elements to a List
            List<Path> files = Files.list(source).collect(Collectors.toList());
            for (Path file : files) {
                // Calculates the relative path from the source directory
                // to the current file or directory being processed within the source directory
                // then appends this relative path to the destination directory
                Path destFile = destination.resolve(source.relativize(file));

                if (Files.isDirectory(file)) {
                    Files.createDirectories(destFile);
                    copyDirectoryFiles(file, destFile);
                } else {
                    Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.err.println("Error copying directory: " + e.getMessage());
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
     * @param args An array of file paths.
     * @return void
     */
    public void cat(String[] args) {
        if (args.length > 2) {
            System.out.println("Usage: cat filepath or cat filepath_1 filepath_2");
            return;
        }
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));

        if (args.length == 0) {
            // Read input from the user and print it
            // if user doesn't enter filename
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Check if the user wants to exit
                    if ("exit".equalsIgnoreCase(line)) {
                        break;
                    }
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            // print just one file content or
            // concatenate and print the content of files
            for (String fileName : args) {
                Path filePath = currentDirectory.resolve(fileName);
                File file = filePath.toFile();

                if (file.exists() && file.isFile()) {
                    // File exists, so print its content
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
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
     * Removes files specified by their names.
     *
     * If a file exists, it checks whether it's a file or a directory.
     * If it's a directory, it cannot be removed.
     * If it's a file, the method attempts to delete it.
     *
     * @param args An array of file paths to remove.
     * @return void
     */
    public void rm(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: rm filepath");
            return;
        }

        try {
            Path currentDirectory = Paths.get(System.getProperty("user.dir"));
            String fileName = args[0];
            Path filePath = currentDirectory.resolve(fileName);
            File file = filePath.toFile();

            if (file.exists()) {
                if (file.isDirectory()) {
                    System.out.println("rm: cannot remove " + fileName + ": is a directory");
                } else {
                    if (!file.delete()) {
                        System.err.println("Error removing file " + fileName);
                    }
                }
            } else {
                System.out.println(fileName + ": No such file!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an enumerated list with the
     * commands you’ve used in the past
     *
     * @return void
     */
    public void history() {
        for (int i = 0; i < commandHistory.size(); i++) {
            System.out.println((i + 1) + "   " + commandHistory.get(i));
        }
    }

    /**
     * Counts lines, words, and characters in a text file and prints the results.
     *
     * @param arguments An array of strings representing arguments.
     * @return void
     */
    public void wc(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: wc file_name");
            return;
        }
        // fileName
        // The name of the text file to analyze.
        String fileName = args[0];
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        Path filePath = currentDirectory.resolve(fileName).toAbsolutePath().normalize();
        File file = filePath.toFile();

        if (file.exists() && file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineCount = 0, wordCount = 0, charCount = 0;

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    charCount += line.length();
                    String[] words = line.split("\\s+");
                    wordCount += words.length;
                }

                System.out.println(lineCount + " " + wordCount + " " + charCount + " " + fileName);
            } catch (IOException e) {
                System.err.println("Error reading the file: " + fileName);
            }
        } else {
            System.err.println("File '" + fileName + "' does not exist.");
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

                String history = commandName + " ";
                for (int i = 0; i < commandArgs.length; i++) {
                    history += commandArgs[i] + " ";
                }
                commandHistory.add(history);

                // Check if the command is 'cp' and it includes '-r' as an argument
                if (commandName.equals("cp") && commandArgs.length > 0 && commandArgs[0].equals("-r")) {
                    /* Remove the '-r' from the arguments*/
                    String[] newArgs = new String[commandArgs.length - 1];
                    System.arraycopy(commandArgs, 1, newArgs, 0, commandArgs.length - 1);
                    /*Call the cp_r method*/
                    cp_r(newArgs);
                }
                // Check if the command is 'ls' and it includes '-r' as an argument
                else if (commandName.equals("ls") && commandArgs.length > 0 && commandArgs[0].equals("-r")) {
                    List<String> fileList_lsr = ls_r();
                    fileList_lsr.forEach(System.out::println);
                } else {
                    // Handle commands based on commandName
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
                            touch(commandArgs);
                            break;
                        case "wc":
                            wc(commandArgs);
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

class Main {
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.chooseCommandAction();
    }
}