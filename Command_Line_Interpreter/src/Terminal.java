import java.nio.file.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("ALL")
public class Terminal {
    /*
     * @return String
     * 
     * @param args
     * Return the output of the command
     */
    public String echo(String[] args) {
        String output = "";
        for (int i = 0; i < args.length; i++) {
            output += args[i] + " ";
        }
        return output;
    }

    /*
     * @return String
     * Return the current directory
     */
    public String pwd() {
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory;
    }

    /*
     * @param args
     * Change the current directory
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
        // else if (args[0].equals("..")){
        // String currentDirectory = pwd();
        // String[] splitCurrentDirectory = currentDirectory.split("/");
        // if(splitCurrentDirectory.length > 1) {
        // StringBuilder newDirectory = new StringBuilder();
        // for (int i = 0; i < splitCurrentDirectory.length - 1; i++){
        // newDirectory.append(splitCurrentDirectory[i]).append("/");
        // }
        // System.setProperty("user.dir", newDirectory.toString());
        // }
        // }

        /*
         * cd takes 1 argument which is a path to a directory (e.g. cd
         * /home/user/Documents) and changes the current directory to the
         * specified directory.
         */
        else {
            String newDirectory = args[0];
            System.setProperty("user.dir", newDirectory);
        }
    }

    /*
     * @return list of string
     * lists the contents of the current directory
     * sorted alphabetically.
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

    /*
     * @return list of string
     * lists the contents of the current directory
     * sorted alphabetically in reverse order.
     */
    public List<String> ls_r() {
        List<String> fileList = new ArrayList<>();
        File directoryPath = new File(pwd());
        String[] contents = directoryPath.list();
        if (contents != null) {
            Arrays.sort(contents, Collections.reverseOrder());
            fileList.addAll(Arrays.asList(contents));
        }
        return fileList;
    }

    // ...
    // This method will choose the suitable command method to be called
    public void chooseCommandAction() {
    }

    public boolean FullPath(String str) {
        return str.contains("C:\\Users");
    }

    /*
     * ... for creating any number of paths
     */
    public void mkdir(String... paths) {

        for (String path : paths) {
            /* mkdir function returns true if the directory is created, false otherwise */
            boolean flag;
            if (!FullPath(path)) {
                /*
                 * if the given string is directory name not a full path
                 * a new directory is created in the current directory.
                 */
                String new_directory = pwd() + path;
                flag = new File(new_directory).mkdir();
            } else {
                // In case the given string is the full path, create the directory
                flag = new File(path).mkdir();
            }

            if (!flag) {
                System.out.println("Directory is not created");
            }
        }
    }

    /* remove empty files in a directory */
    public void RemoveEmptyFiles(File current_directory) {
        /* exception is thrown if the directory is empty */
        for (File file : Objects.requireNonNull(current_directory.listFiles())) {
            if (file.isDirectory() && Objects.requireNonNull(file.list()).length == 0) {
                boolean deleted = file.delete();
                if (!deleted)
                    System.out.println("this directory is not deleted");
            }
        }
    }

    public void rmdir(String path) {
        /*
         * if the given argument = "*"remove
         * all the empty directories in the current directory
         */
        File current_directory;
        if (Objects.equals(path, "*")) {
            current_directory = new File(pwd());
            RemoveEmptyFiles(current_directory);
        } else {
            // this part still needs to be updated
            // the code removes file if name of full path is given but not in case relative
            // (short) path is given
            if (FullPath(path)) {
                current_directory = new File(path);
                current_directory.delete();
                return;
            }
            String full_path = new File(path).getAbsolutePath();
            current_directory = new File(full_path);
            boolean deleted = current_directory.delete();
            if (!deleted)
                System.out.println("this directory is not deleted");
        }

    }

    public void touch(String path) {

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
     */
    public static void cp(String[] arguments) {
        if (arguments.length < 2) {
            System.out.println("Usage: cp <source> <destination> [destination_directory]");
            return;
        }

        String sourcePath = arguments[0];
        String destinationPath = arguments[1];

        // Check if the source exists
        Path source = Paths.get(sourcePath);
        if (!Files.exists(source)) {
            System.out.println("Source does not exist.");
            return;
        }

        // Check if the last argument is a directory
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
                    // Error: Cannot copy multiple files to a directory with only two arguments
                    System.out.println("Error: Cannot copy multiple files to a directory with only two arguments.");
                } else {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied successfully.");
                }
            } else {
                // Multiple source files provided
                if (!isLastArgDirectory) {
                    // Error: Last argument is not a directory
                    System.out.println("Error: Last argument is not a directory.");
                } else {
                    // Copy the source files into the destination directory
                    for (int i = 0; i < arguments.length - 1; i++) {
                        String file = arguments[i];
                        Path sourceFile = Paths.get(file);
                        Path targetFile = destination.resolve(sourceFile.getFileName());
                        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                    System.out.println("Files copied successfully.");
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
     */
    public void cp_r(String[] args) {
        if (args.length == 2) {
            File sourceDir = new File(args[0]);
            File destDir = new File(args[1]);
            if (sourceDir.isDirectory() && destDir.isDirectory()) {
                copyDirectoryFiles(sourceDir, destDir);
                System.out.println("Copy directories: " + args[0] + " to " + args[1]);
            } else {
                System.out.println("Both source and destination should be directories.");
            }
        } else {
            System.out.println("Usage: cp -r source_directory destination");
        }
    }

    /**
     * Recursively copies the files and subdirectories from a source directory to a destination directory.
     *
     * @param sourceDir The source directory to copy.
     * @param destDir The destination directory where the files and subdirectories will be copied.
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
     */
    public void cat(String[] fileNames) {
        if (fileNames.length == 0) {
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
            // Concatenate and print the content of files
            int index = 0;
            for (String fileName : fileNames) {
                index++;
                File file = new File(fileName);
                if (file.exists() && file.isFile()) {
                    // File exists, so print its content
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
     */
    public void rm(String[] fileNames) {
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (file.exists()) {
                if (file.isDirectory()) {
                    System.out.println("Cannot remove a directory.");
                } else {
                    if (file.delete()) {
                        System.out.println("File " + fileName + " removed successfully.");
                    } else {
                        System.err.println("Error removing file " + fileName + ".");
                    }
                }
            } else {
                System.err.println("File "+ fileName+ " not found.");
            }
        }
    }
}