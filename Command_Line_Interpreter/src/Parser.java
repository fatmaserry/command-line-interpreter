/**
* This class is responsible for parsing the input entered by the user
*/
public class Parser {
    
    String commandName;
    String[] args;
    /*
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

    /*
     * This method will return the commandName
     */
    public String getCommandName() {
        return commandName;
    }

    /*
     * This method will return the args
     */
    public String[] getArgs() {
        return args;
    }
}
