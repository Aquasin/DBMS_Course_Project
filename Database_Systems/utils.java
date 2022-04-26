package Database_Systems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// keyWords 
//     CREATE,
//     SELECT,
//     FROM,
//     INSERT,
//     UPDATE,
//     DELETE
// 

public class utils {
    
    //* 
    static int MAX_TABLE_LENGTH;

    //* Storing the Schema of the table 
    static Map<String,List<String>> tableSchema;
    //* Storing the Detail of each table 
    static Map<String,List<String>> tableDetails;
    //* Storing the Maximum Attribute Length of each table 
    static Map<String,Integer> tableLengthCount;

    //* Loading the table schema initially in a Hashmap 
    public static void loadTableSchema()
    {
        tableSchema = new HashMap<String,List<String>>();
        tableDetails = new HashMap<String,List<String>>();
        tableLengthCount = new HashMap<String,Integer>();
        MAX_TABLE_LENGTH=0;

        try {
            File schemaFile = new File("schema.txt");
            Scanner fileContents = new Scanner(schemaFile);
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                String[] dataArray = lineContents.split("#");
                List<String> temp = new ArrayList<String>();
                String key = dataArray[0];
                int maxAttributeLength=0;

                if(MAX_TABLE_LENGTH < key.length())
                {
                    MAX_TABLE_LENGTH = key.length();
                }
                for(int i=1;i<dataArray.length;i++)
                {
                    if(i%2 != 0)
                    {
                        if(maxAttributeLength < dataArray[i].length())
                        {
                            maxAttributeLength = dataArray[i].length();
                        }
                    }
                    temp.add(dataArray[i]);
                }
                tableSchema.put(key, temp);
                tableLengthCount.put(key, maxAttributeLength);
                // System.out.println(lineContents);
            }
            System.out.println(tableLengthCount);
            // System.out.println(tableSchema);

            fileContents.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Error occurred Displaying Tables");
            e.printStackTrace();
        }
    }

    //* Loading the table details in a Hashmap tableDetails
    public static void loadTables(String tableName)
    {
        // System.out.println("Loading from File");
        try {
            File schemaFile = new File(tableName + ".txt");
            Scanner fileContents = new Scanner(schemaFile);
            List<String> temp = new ArrayList<String>();
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                temp.add(lineContents);
            }
            tableDetails.put(tableName, temp);

            fileContents.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Error occurred Displaying Tables");
            e.printStackTrace();
        }
    }

    //* To check if a table with the given name exists 
    private static Boolean tableExists(String tableName)
    {
        if(tableSchema.get(tableName) != null)
        {
            return true;
        }
        return false;
    }

    //* To create a file
    public static void createFile(String fileName)
    {
        try {
            File schemaFile = new File(fileName);
            if (schemaFile.createNewFile()) {
              System.out.println("File created: " + schemaFile.getName());
            } 
            // else {
            //   System.out.println("File already exists.");
            // }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    //* To print Time and Date initially
    public static void printDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date currentDate = new Date();
        System.out.println(formatter.format(currentDate));
    }

    //* To Clear the Console of Output
    public static void Clear(String[] commands)
    {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } 
        //* | (Pipe) used for catching multiple expections in a single catch block
        catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // private static void printTable(ArrayList<String> colHeadings,ArrayList<String> data)
    // {
    //     String tableContents="",temp="",horizontalRow="";
    //     String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-'))

    // }

    //* regex for checking if given string is Numeric
    private static boolean isNumeric(String str)
    {
        return str != null && !str.matches("[0-9.]+.[0-9.]+");
    }
    
    //* regex for checking if given string is Decimal
    private static boolean isDecimal(String str)
    {
        return str != null && str.matches("[0-9.]+.[0-9.]+");
    }
    
    //* regex for checking if given string is String
    private static boolean isString(String str)
    {
        return str != null && str.matches("'[a-zA-Z]+'");
    }

    public static void Create(String[] commands)
    {
        //TODO: Error Checking remaining
        //* 7 because CREATE TABLE TABLE_NAME ( Attribute_Name Datatype );
        //* (commands.length-7)%3 because CREATE TABLE TABLE_NAME ( Attribute_Name Datatype , Attribute_Name Datatype );
        if(commands.length < 7 || (commands.length-7)%3 != 0 || !commands[1].equals("TABLE"))
        {
            System.out.println("Incorrect CREATE Command");
            return;
        }
        String fileContents="";
        String tableName = commands[2];
        if(tableExists(tableName)) 
        {
            System.out.println(commands[2] + " already exists");
            return;
        }
        fileContents+=commands[2]+"#";
        //* i=4 because from index 4 attributes name and datatype start.
        for(int i=4;i<commands.length;i+=3)
        {
            // if(!commands[i].equals(",") && !commands[i].equals(");"))
            // {
            //     fileContents+=commands[i]+"#";
            // }
            //* "^CHAR\\([1-9][0-9]?\\)$" is used to detect CHAR(1) to CHAR(99)
            if(commands[i+1].equals("INT") || commands[i+1].equals("DECIMAL") || commands[i+1].matches("^CHAR\\([1-9][0-9]?\\)$"))
            {
                fileContents+=commands[i]+"#"+commands[i+1]+"#";
            } 
            //* "^CHAR\\(-[1-9][0-9]?\\)$" is used to detect Negative numbers e.g. CHAR(-20)
            else if (commands[i+1].matches("^CHAR\\(-[1-9][0-9]?\\)$"))
            {
                System.out.println("Negative Length Columns are not allowed");
                return;
            } 
            //* "^CHAR\\(0\\)$" is used to detect 0 number e.g. CHAR(0)
            else if (commands[i+1].matches("^CHAR\\(0\\)$"))
            {
                System.out.println("Zero Length Columns are not allowed");
                return;
            } 
            //* If anyone of the above conditions do not match that means the Datatype is Unknown 
            else {
                System.out.println("Unknown Datatype - " + commands[i+1]);
                return;
            }            
        }
        fileContents = fileContents.substring(0,fileContents.length()-1);
        String[] dataArray = fileContents.split("#");
        //* Updating MAX_TABLE_LENGTH if tableName is greater
        if(MAX_TABLE_LENGTH < dataArray[0].length())
        {
            MAX_TABLE_LENGTH = dataArray[0].length();
        }
        //* To Create the table file.
        createFile(dataArray[0]+".txt");
        List<String> temp = new ArrayList<String>();
        String key = dataArray[0];
        for(int i=1;i<dataArray.length;i++)
        {
            temp.add(dataArray[i]);
        }
        tableSchema.put(key, temp);
        fileContents+="\n";
        
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt",true));
            fileWriter.write(fileContents);
            System.out.println("Table Added Successfully");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred while adding Table");
            e.printStackTrace();
        }
    }

    public static void Drop(String[] commands)
    {
        //TODO: Error Checking remaining
        if(commands.length != 2)
        {
            System.out.println("Incorrect DROP Command");
            return;
        } 
        String tableName = commands[1].substring(0,commands[1].length() - 1);
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }
        tableSchema.remove(tableName);
        String fileContents="";
        for(String key : tableSchema.keySet())
        {
            fileContents+=key+"#";
            for(String temp:tableSchema.get(key))
            {
                fileContents+=temp+"#";
            }
            fileContents = fileContents.substring(0,fileContents.length()-1);
            fileContents+="\n";
        }

        //* To delete the existing .txt file of that table 
        File fileName = new File(tableName+".txt");
        if(fileName.delete())
        {
            System.out.println("Deleted the file: " + fileName.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        //* To update the schema.txt file
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt"));
            fileWriter.write(fileContents);
            System.out.println(tableName + " Table Dropped Successfully");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred while Dropping Table "+tableName);
            e.printStackTrace();
        }
    }
    
    public static void Help(String[] commands)
    {
        //TODO: Yet to add CMD for help in list of cmds
        if(commands.length != 2) 
        {
            System.out.println("Incorrect HELP Command");
            return;
        }
        if(!commands[1].equals("TABLES;"))
        {
            System.out.println("Incorrect HELP Command");
            return;
        }
        // System.out.println(MAX_TABLE_LENGTH);
        String tableContents="",temp="",horizontalRow="";
        for(int i=0;i<MAX_TABLE_LENGTH+4;i++)
        {
            horizontalRow+="-";
        }
        tableContents+=horizontalRow+"\n"; 
        tableContents+=String.format("| %-"+(MAX_TABLE_LENGTH)+"s |\n","TABLES");
        tableContents+=horizontalRow+"\n"; 
        for(String key : tableSchema.keySet())
        {
            temp+=String.format("| %-"+(MAX_TABLE_LENGTH)+"s |\n",key);
        }
        tableContents+=temp;
        tableContents+=horizontalRow+"\n"; 
        System.out.print(tableContents);
    }

    public static void Describe(String[] commands)
    {
        if(commands.length != 2) 
        {
            System.out.println("Incorrect DESCRIBE Command");
            return;
        }
        String tableName = commands[1].substring(0,commands[1].length() - 1);
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exist");
            return;
        }
        System.out.println(tableName);
        int maxAttributeLength = tableLengthCount.get(tableName);
        if(maxAttributeLength < "Attribute Name".length())
        {
            maxAttributeLength = "Attribute Name".length();
        }
        List<String> tempArray = tableSchema.get(tableName);
        System.out.println(String.format(String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-')));
        System.out.println(String.format("| %-"+maxAttributeLength+"s |","Attribute Name") + String.format(" %-8s |","Datatype"));
        System.out.println(String.format(String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-')));
        // System.out.println("-----------------------------");
        // System.out.println("| Attribute Name | Datatype |");
        // System.out.println("-----------------------------");
        for(int i=0;i<tempArray.size();i+=2)
        {
            String formattedRow = String.format("| %-"+(maxAttributeLength)+"s |",tempArray.get(i)) + String.format(" %-8s |",tempArray.get(i+1));
            System.out.println(formattedRow);
        }
        System.out.println(String.format(String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-')));
        // System.out.println("-----------------------------");
    }

    public static void Insert(String[] commands)
    {
        // TODO: Error Checking remaining
        //* 7 because INSERT INTO TABLE_NAME VALUES ( ATTRIBUTE_NAME ); 
        if(commands.length < 7 || !(commands[1].equals("INTO") && commands[3].equals("VALUES")))
        {
            System.out.println("Incorrect INSERT Command");
            return;
        }
        if(!commands[4].equals("(") || !commands[commands.length-1].equals(");"))
        {
        	System.out.println("Incorrect INSERT Command");
            return;
	    }    
        // To check if the Command has additional ( and ) in it.
        String remainingString = "";
        for(int i=5;i<commands.length-1;i++)
        {
        	remainingString+=commands[i];
	    }  
        if(remainingString.contains(")") || remainingString.contains("("))
        {
        	System.out.println("Incorrect INSERT Command");
            return;
	    }
        // System.out.println(remainingString);
        String[] attributeValues = remainingString.split(",");
        
        String tableName = commands[2];
        if(!tableExists(tableName)) 
        {
            System.out.println(commands[2] + " doesn't exists");
            return;
        }
        String rowDetails="";
        List<String> headRow = tableSchema.get(tableName);
        // System.out.println(headRow);
        if(attributeValues.length != headRow.size()/2)
        {
            System.out.println("Insufficient Attributes");
            return;
        }

        for(int i=0;i<headRow.size()/2;i++)
        {
            if(headRow.get(2*i+1).contains("CHAR"))
            {
                if(isString(attributeValues[i]))
                {
                    int maxCharSize = Integer.parseInt(headRow.get(2*i+1).substring(5,headRow.get(2*i+1).length()-1));
                    if(attributeValues[i].length()-2 > maxCharSize)
                    {
                        System.out.println(attributeValues[i] + " is too long");
                        return;
                    }

                    rowDetails+=attributeValues[i].substring(1,attributeValues[i].length()-1)+"#";
                } 
                else 
                {
                    System.out.println("Expected CHAR Datatype");
                    return;
                }
                
            }
            else if(headRow.get(2*i+1).equals("INT"))
            {
                if(isNumeric(attributeValues[i]))
                {
                    rowDetails+=attributeValues[i]+"#";
                }
                else 
                {
                    System.out.println("Expected INT Datatype");
                    return;
                }
            }
            else if(headRow.get(2*i+1).contains("DECIMAL"))
            {
                if(isDecimal(attributeValues[i]))
                {
                    rowDetails+=attributeValues[i]+"#";
                }
                else 
                {
                    System.out.println("Expected DECIMAL Datatype");
                    return;
                }
            }
        }
        rowDetails = rowDetails.substring(0,rowDetails.length()-1);
        //* Adding into tableDetails
        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
        tableDetails.get(tableName).add(rowDetails);
        rowDetails+="\n";
        String fileName = tableName + ".txt";
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName,true));
            fileWriter.write(rowDetails);
            System.out.println("Row Added Successfully in " + tableName);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred while adding Row in " + tableName);
            e.printStackTrace();
        }
        
    }

    public static void Select(String[] commands)
    {
        //* Error checking in Select Command
        //* 4 becuase SELECT * FROM STUDENT;
        if(commands.length < 4)
        {
            System.out.println("Incorrect SELECT Command");
            return;
        }
        //* To remove the ending ;
        commands[commands.length - 1] = commands[commands.length - 1].substring(0,commands[commands.length - 1].length()-1);
        // System.out.println(commands[commands.length - 1]);
        
        int indexFrom = commands.length;
        int indexWhere = commands.length;
        for(int i=0;i<commands.length;++i)
        {
            if(commands[i].equals("FROM"))
            {
                indexFrom = i;
            }
            if(commands[i].equals("WHERE"))
            {
                indexWhere = i;
            }
        }

        if(indexFrom == commands.length ) 
        {
            System.out.println("Expected FROM Keyword in SELECT Command");
            return;
        }

        if(indexFrom < 2)
        {
            System.out.println("Columns not given");
            return;
        }

        //* Did not consider 1 column name given
        // if(indexFrom == 2 && !commands[1].equals("*"))
        // {
        //     System.out.println("Incorrect SELECT Command");
        //     return;
        // }

        String tableName = commands[indexFrom+1];
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }
        
        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
        List<String> tableHeader = tableSchema.get(tableName);
        boolean conditionAND=false;
        boolean conditionOR=false;
        List<Integer> columnIndexes = new ArrayList<Integer>();
        List<Integer> conditionColumnIndexes = new ArrayList<Integer>();
        List<String> conditions = new ArrayList<String>();
        //* This means FROM keyword is at index 2 and all columns have to be considered
        if(indexFrom == 2 && commands[1].equals("*"))
        {
            
            // SELECT * FROM STUDENT WHERE NAME = Hello; - 5 to 7
            // SELECT * FROM STUDENT WHERE NAME = Hello AND ID = 2; - 5 to 11
            // SELECT * FROM STUDENT WHERE NAME = Hello AND ID = 2 AND SAL = 30; - 5 to 15
            for(int i=5;i<commands.length;i+=4)
            {
                //* AND OR postions 8, 12, 16
                if(i+3 < commands.length)
                {
                    if(commands[i+3].equals("AND"))
                    {
                        conditionAND = true;
                    }
                    else if(commands[i+3].equals("OR"))
                    {
                        conditionOR = true;
                    }
                    else
                    {
                        System.out.println("Expected AND or OR keyword");
                        return;
                    }
                }
                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        conditionColumnIndexes.add(indexColumnNo/2);
                        conditions.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }
            
            if(conditionColumnIndexes.size() != 0)
            {
                for(int i=0;i<conditionColumnIndexes.size();++i)
                {
                    System.out.println(conditionColumnIndexes.get(i) + " " + tableHeader.get(conditionColumnIndexes.get(i)*2));
                }
            }
            
            //* To display the Table Header Columns
            for(int i=0;i<tableHeader.size();i+=2)
            {
                System.out.print(String.format("%-10s ",tableHeader.get(i)));
            }
            System.out.println();
            // 1#Gaurav#gaurav@gmail.com
            for(String tableRow : tableDetails.get(tableName)) 
            {
                String[] tableArray = tableRow.split("#");
                String rowDisplay = "";
                boolean displayRow = false;
                if(conditionAND == true)
                {
                    displayRow = true;
                }
                // 1, Gaurav, gaurav@gmail.com
                //* Condition exists 
                if(conditionColumnIndexes.size() != 0)
                {
                    for(int j=0;j<conditionColumnIndexes.size();++j)
                    {
                        // 8, 12, 16
                        // System.out.println(tableArray[conditionColumnIndexes.get(j)]);
                        // System.out.println(conditions.get(j));
                        //* AND condition
                        if(conditionAND == true)
                        {
                            if(!conditions.get(j).equals(tableArray[conditionColumnIndexes.get(j)]))
                            {
                                // rowDisplay += String.format("%-10s ",tableArray[i]);
                                displayRow = false;
                            }
                        }
                        //* OR condition
                        else if(conditionOR == true)
                        {
                            if(conditions.get(j).equals(tableArray[conditionColumnIndexes.get(j)]))
                            {
                                // rowDisplay += String.format("%-10s ",tableArray[i]);
                                displayRow = true;
                            }
                        }
                        //* Neither AND nor OR. Only 1 condition
                        else 
                        {
                            if(conditions.get(j).equals(tableArray[conditionColumnIndexes.get(j)]))
                            {
                                // rowDisplay += String.format("%-10s ",tableArray[i]);
                                displayRow = true;
                            }

                        }
                    }
                    // System.out.print(String.format("%-10s ",tableArray[j]));
                }
                //* Condition doesn't exists so display all the rows
                else 
                {
                    displayRow = true;
                }
                if(displayRow)
                {
                    for(int i=0;i<tableArray.length;++i)
                    {
                        rowDisplay += String.format("%-10s ",tableArray[i]);
                    }
                }
                if(displayRow)
                {
                    System.out.println(rowDisplay);
                }
            }
        } 
        //* This means FROM keyword is at index 2 and then check the name of a single column given in query
        //* OR
        //* This means FROM keyword is not at index 2 and then check the names of all column given in query
        else {

            if((indexFrom-1)%2 == 0)
            {
                System.out.println("SELECT Command has extra ',' or ',' not given between names of columns");
                return;
            }
            
            for(int i=1;i<indexFrom;++i)
            {
                // System.out.println(i);
                // System.out.println(commands[i]);
                if(i%2 == 0)
                {
                    if(!commands[i].equals(","))
                    {
                        System.out.println("Expected ,");
                        return;
                    }
                } else {
                    boolean columnExist = false;
                    // System.out.println(tableHeader);
                    for(int j=0;j<tableHeader.size();j+=2) 
                    {
                        if(commands[i].equals(tableHeader.get(j)))
                        {
                            // System.out.println(i);
                            // System.out.println(j);
                            columnIndexes.add(j/2);
                            columnExist = true;
                            break;
                        }
                    }
                    if(!columnExist)
                    {
                        System.out.println(commands[i] + " doesn't exist in " + tableName + " table");
                        return;
                    }
                }
            }

            for(int i=indexFrom + 3;i<commands.length;i+=4)
            {
                if(i+3 < commands.length)
                {
                    if(commands[i+3].equals("AND"))
                    {
                        conditionAND = true;
                    }
                    else if(commands[i+3].equals("OR"))
                    {
                        conditionOR = true;
                    }
                    else
                    {
                        System.out.println("Expected AND or OR keyword");
                        return;
                    }
                }
                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        conditionColumnIndexes.add(indexColumnNo/2);
                        conditions.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }

            if(conditionColumnIndexes.size() != 0)
            {
                for(int i=0;i<conditionColumnIndexes.size();++i)
                {
                    System.out.println(conditionColumnIndexes.get(i) + " " + tableHeader.get(conditionColumnIndexes.get(i)*2));
                }
            }

            for(Integer columnIndex : columnIndexes)
            {
                System.out.print(String.format("%-10s",tableHeader.get(columnIndex*2)));
            }
            System.out.println();
            
            for(String tableRow : tableDetails.get(tableName)) 
            {
                String[] tableArray = tableRow.split("#");
                String rowDisplay = "";
                boolean displayRow = false;
                if(conditionAND == true)
                {
                    displayRow = true;
                }
                // 1, Gaurav, gaurav@gmail.com
                //* Condition exists 
                if(conditionColumnIndexes.size() != 0)
                {
                    for(int j=0;j<conditionColumnIndexes.size();++j)
                    {
                        //* AND condition
                        if(conditionAND == true)
                        {
                            if(!conditions.get(j).equals(tableArray[conditionColumnIndexes.get(j)]))
                            {
                                // rowDisplay += String.format("%-10s ",tableArray[i]);
                                displayRow = false;
                            }
                        }
                        //* OR condition
                        else if(conditionOR == true)
                        {
                            if(conditions.get(j).equals(tableArray[conditionColumnIndexes.get(j)]))
                            {
                                // rowDisplay += String.format("%-10s ",tableArray[i]);
                                displayRow = true;
                            }
                        }
                        //* Neither AND nor OR. Only 1 condition
                        else 
                        {
                            if(conditions.get(j).equals(tableArray[conditionColumnIndexes.get(j)]))
                            {
                                // rowDisplay += String.format("%-10s ",tableArray[i]);
                                displayRow = true;
                            }

                        }
                    }
                    // System.out.print(String.format("%-10s ",tableArray[j]));
                }
                //* Condition doesn't exists so display all the rows
                else 
                {
                    displayRow = true;
                }
                if(displayRow)
                {
                    for(Integer columnIndex : columnIndexes) 
                    {
                        rowDisplay += String.format("%-10s",tableArray[columnIndex]);
                    }
                }
                if(displayRow)
                {
                    System.out.println(rowDisplay);
                }
            }
        }
    }
    
    public static void Delete(String[] commands)
    {
        int count = 0;
        String tableName;
        //* 3 becuase DELETE FROM TABLE_NAME; which deletes all the records of the table
        if(commands.length == 3 && commands[1].equals("FROM"))
        {
            tableName = commands[2].substring(0,commands[2].length()-1);
            if(!tableExists(tableName)) 
            {
                System.out.println(tableName + " doesn't exists");
                return;
            }
            if(tableDetails.get(tableName) == null)
            {
                loadTables(tableName);
            }   
            
            List<String> tableRows = tableDetails.get(tableName);
            
            for(int i=0;i<tableRows.size();++i)
            {
                tableDetails.get(tableName).remove(i);
                // --i because the size of tableRow gets reduced by 1 when a record is removed from ArrayList
                // and hence it skips the next record and goes to the +2 index record.
                --i;
                ++count;
            }
        }
        
        //* 7 becuase DELETE FROM TABLE_NAME WHERE ID = 2;
        else if(commands.length >= 7 || (commands[1].equals("FROM") && commands[3].equals("WHERE")))
        {
            tableName = commands[2];
            if(!tableExists(tableName)) 
            {
                System.out.println(tableName + " doesn't exists");
                return;
            }
            if(tableDetails.get(tableName) == null)
            {
                loadTables(tableName);
            }
            List<String> headRow = tableSchema.get(tableName);
            // int indexColumnNo = 0;
            List<Integer> columnIndexes = new ArrayList<Integer>();
            //* DELETE FROM STUDENT WHERE NAME = Vivek; - 4 to 6
            //* DELETE FROM STUDENT WHERE NAME = Vivek AND ID = 1 AND SAL = 30; - 4 to 14
            // System.out.println(headRow);
            for(int i=4;i<commands.length;i+=4)
            {
                //* AND OR postions 7, 11, 15
                if(i+3 < commands.length)
                {
                    if(!(commands[i+3].equals("AND") || commands[i+3].equals("OR")))
                    {
                        System.out.println("Expected AND or OR keyword");
                        return;
                    }
                }
                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<headRow.size();indexColumnNo+=2)
                {
                    if(headRow.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        columnIndexes.add(indexColumnNo/2);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }
            // List<String> tableRows = tableDetails.get(tableName);
            
            // System.out.println(tableRows);
            // String condition = commands[6].substring(0,commands[6].length()-1);
            // for(int i=0;i<tableRows.size();++i)
            // {
            //     System.out.println(i);
            //     String[] temp = tableRows.get(i).split("#");
            //     // for(int j=0;j<temp.length;++j)
            //     // {
            //     //     System.out.println(temp[j]);
            //     // }
            //     // System.out.println(temp.length);
            //     if(condition.equals(temp[indexColumnNo]))
            //     {
            //         System.out.println("Match Found");
            //         tableDetails.get(tableName).remove(i);
            //         // --i because the size of tableRow gets reduced by 1 when a record is removed from ArrayList
            //         // and hence it skips the next record and goes to the +2 index record.
            //         --i;
            //         ++count;
            //     }
            // }
        }
        else 
        {
            System.out.println("Incorrect DELETE Command");
            return;
        }

        // System.out.println("Count is "+count);
        // if(count != 0)
        // {
        //     List<String> tableRow = tableDetails.get(tableName);
        //     String rowDetails="";
        //     for(int i=0;i<tableRow.size();++i)
        //     {
        //         rowDetails+=tableRow.get(i)+"\n";
        //     }
        //     String fileName=tableName+".txt";
        //     try {
        //         BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName));
        //         fileWriter.write(rowDetails);
        //         System.out.println(count + " rows deleted Successfully in " + tableName);
        //         fileWriter.close();
        //     } catch (IOException e) {
        //         System.out.println("Error occurred while deleting rows in " + tableName);
        //         e.printStackTrace();
        //     }
        // } 
        // else {
        //     System.out.println("No Row deleted");
        // }
        
    }

    public static void Update(String[] commands)
    {
        //* 10 becuase UPDATE TABLE_NAME SET ATTRIBUTE_NAME1 = ATTRIBUTE_VALUE1 WHERE ATTRIBUTE_NAME2 = ATTRIBUTE_VALUE2;
        if(commands.length < 10 || !(commands[2].equals("SET") && commands[6].equals("WHERE")))
        {
            System.out.println("Incorrect UPDATE Command");
        }
        String tableName = commands[1];
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }
        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
        List<String> headRow = tableSchema.get(tableName);
        Boolean attributeNameChange=false,attributeNameIdentifier=false;
        for(String colName : headRow)
        {
            if(colName.equals(commands[3]))
            {
                attributeNameChange = true;
            }
            if(colName.equals(commands[7]))
            {
                attributeNameIdentifier = true;
            }
        }
        if(!(attributeNameChange && attributeNameIdentifier))
        {
            System.out.println("Unknown Attribute name");
            return;
        }
    }
}