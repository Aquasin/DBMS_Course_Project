import Database_Systems.*;
import java.util.Scanner;
// import java.io.*;

class FellowshipDatabase {

    // public static final String ANSI_RESET = "\u001B[0m";
    // public static final String ANSI_BLACK = "\u001B[30m";
    // public static final String ANSI_RED = "\u001B[31m";
    // public static final String ANSI_GREEN = "\u001B[32m";
    // public static final String ANSI_YELLOW = "\u001B[33m";
    // public static final String ANSI_BLUE = "\u001B[34m";
    // public static final String ANSI_PURPLE = "\u001B[35m";
    // public static final String ANSI_CYAN = "\u001B[36m";
    // public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        
        // Welcoming the User
        System.out.println("Welcome to Fellowship Database");
        // To Print today's Date
        utils.printDate();
        utils.createFile("schema.txt");
        utils.loadTableSchema();
        
        Scanner readUserInputs = new Scanner(System.in);
        // System.out.print("# - ");
        // System.out.print(ANSI_RED + "Fellowship> " + ANSI_RESET);
        System.out.print("Fellowship> ");
        String sqlCommand = readUserInputs.nextLine();
        String[] commands = sqlCommand.split(" ");

        while(!commands[0].equals("quit"))
        {
            if(commands[0].length() == 0)
            {
                System.out.println("Enter SQL Command");
            }
            else if(sqlCommand.charAt(sqlCommand.length()-1) != ';')
            {
                System.out.println("Semicolon Missing");
            }
            else if(commands[0].equals("CREATE"))
            {
                // System.out.println("CREATE Running");
                utils.Create(commands);
            }
            else if(commands[0].equals("DROP"))
            {
                // System.out.println("DROP Running");
                utils.Drop(commands);
            }
            else if(commands[0].equals("HELP"))
            {
                // System.out.println("HELP Running");
                utils.Help(commands);
            }
            else if(commands[0].equals("DESCRIBE") || commands[0].equals("DESC"))
            {
                // System.out.println("HELP Running");
                utils.Describe(commands);
            }
            else if(commands[0].equals("CLEAR;"))
            {
                // System.out.println("HELP Running");
                utils.Clear(commands);
            }
            else if(commands[0].equals("INSERT"))
            {
                // System.out.println("INSERT RUNNING");
                utils.Insert(commands);
            }
            else if(commands[0].equals("SELECT"))
            {
                // System.out.println("SELECT Running");
                utils.Select(commands);
            }
            else if(commands[0].equals("DELETE"))
            {
                // System.out.println("DELETE Running");
                utils.Delete(commands);
            }
            else if(commands[0].equals("UPDATE"))
            {
                // System.out.println("UPDATE Running");
                utils.Update(commands);
            }
            else {
                System.out.println("Incorrect Command");
            }
            //* Prompt
            System.out.print("Fellowship> ");
            sqlCommand = readUserInputs.nextLine();
            commands = sqlCommand.split(" ");
        }
        readUserInputs.close();
    }
}
