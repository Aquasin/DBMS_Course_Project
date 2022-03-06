import Database_Systems.*;
import java.io.*;
import java.util.Scanner;

class FellowshipDatabase {

    public static void main(String[] args) {
        
        // Welcoming the User
        System.out.println("Welcome to Fellowship Database");
        // To Print todays Date
        utils.printDate();
        utils.createFile();

        Scanner readUserInputs = new Scanner(System.in);
        String sqlCommand = readUserInputs.nextLine();
        String[] commands = sqlCommand.split(" ");

        while(!commands[0].equals("quit"))
        {
            if(sqlCommand.charAt(sqlCommand.length()-1) != ';')
            {
                System.out.println("Semicolon Missing");
            }
            else if(commands[0].equals("CREATE"))
            {
                System.out.println("CREATE Running");
                utils.Create(commands);
            }
            else if(commands[0].equals("HELP"))
            {
                System.out.println("HELP Running");
                utils.Help(commands);
            }
            // else if(commands[0].equals("SELECT"))
            // {
            //     System.out.println("SELECT Running");
            // }
            sqlCommand = readUserInputs.nextLine();
            commands = sqlCommand.split(" ");
        }
        readUserInputs.close();
    }
}
