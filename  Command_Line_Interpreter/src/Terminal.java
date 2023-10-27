import java.io.File;
import java.util.*;

public class Terminal {
    Parser parser;

/*
 * @return String
 * @param args
 * Return the output of the command
 */
public String echo(String[] args){
    String output = "";
    for(int i = 0; i < args.length; i++){
        output += args[i] + " ";
    }
    return output;
}
/*
 * @return String
 * Return the current directory
 */
public String pwd(){
    String currentDirectory = System.getProperty("user.dir");
    return currentDirectory;
}
/*
 * @param args
 * Change the current directory
 */
public void cd(String[] args){
    /*cd takes no arguments and changes the current path to the path
    of your home directory */
    if (args.length==0){
        System.setProperty("user.dir", System.getProperty("user.home"));
    }
    /*cd takes 1 argument which is “..” (e.g. cd ..) and changes the
    current directory to the previous directory. 
    example:
    Current directory: /home/user/Documents
    Using the command: cd ..
    New directory: /home/user
    */
    else if (args[0].equals("..")) {
        String currentDirectory = pwd();
        File file = new File(currentDirectory);
        String parentDirectory = file.getParent();
        if (parentDirectory != null) {
            System.setProperty("user.dir", parentDirectory);
        }
    }
    //     else if (args[0].equals("..")){
    //     String currentDirectory = pwd();
    //     String[] splitCurrentDirectory = currentDirectory.split("/");
    //     if(splitCurrentDirectory.length > 1) {
    //         StringBuilder newDirectory = new StringBuilder();
    //         for (int i = 0; i < splitCurrentDirectory.length - 1; i++){
    //             newDirectory.append(splitCurrentDirectory[i]).append("/");
    //         }
    //         System.setProperty("user.dir", newDirectory.toString());
    //     }
    // }

    /*
    cd takes 1 argument which is a path to a directory (e.g. cd
    /home/user/Documents) and changes the current directory to the
    specified directory.
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
    for(int i=0; i<contents.length; i++) {
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
//This method will choose the suitable command method to be called
public void chooseCommandAction(){}


    public boolean FullPath(String str) {
        return str.contains("C:\\Users");
    }
    /*
    ... for creating any number of paths
   */
    public void mkdir(String... paths) {

        for (String path : paths) {
            /*mkdir function returns true if the directory is created, false otherwise*/
            boolean flag;
            if (!FullPath(path)) {
                /*
                 * if the given string is directory name not a full path
                 * a new directory is created in the current directory.
                 */
                String new_directory = pwd() + path;
                flag = new File(new_directory).mkdir();
            }
            else {
                //In case the given string is the full path, create the directory
                flag = new File(path).mkdir();
            }

            if (!flag) {
                System.out.println("Directory is not created");
            }
        }
    }


    /*remove empty files in a directory*/
    public void RemoveEmptyFiles(File current_directory){
        /*exception is thrown if the directory is empty*/
        for(File file : Objects.requireNonNull(current_directory.listFiles())){
            if (file.isDirectory() && Objects.requireNonNull(file.list()).length==0) {
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
            // the code removes file if name of full path is given but not in case relative (short) path is given
            if (FullPath(path)){
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

}