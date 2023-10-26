import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    String contents[] = directoryPath.list();
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
    String contents[] = directoryPath.list();
    if (contents != null) {
        Arrays.sort(contents, Collections.reverseOrder()); 
        fileList.addAll(Arrays.asList(contents));
    }
    return fileList;
}

// ...
//This method will choose the suitable command method to be called
public void chooseCommandAction(){}





public static void main(String[] args){
    Terminal terminal = new Terminal();
    terminal.parser = new Parser();
    while(true){
        System.out.print("user@user:~$ ");
        String input = System.console().readLine();
        terminal.parser.parse(input);
        terminal.chooseCommandAction();
    }
}

// public static void main(String[] args) {
//     Terminal terminal = new Terminal();
//     String[] cdArgs = {"E:\\FCAI\\Third_Year\\Third_Year(1)\\OS\\test"};
//     terminal.cd(cdArgs);
//     List<String> fileList = terminal.ls();
//     for (String file : fileList) {
//         System.out.println(file);
//     }
// }



}