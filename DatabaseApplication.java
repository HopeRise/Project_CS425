import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DatabaseApplication {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Connection connection = databaseConnection();
        menu(connection);
    }

    // main menu of the database program
    public static void menu(Connection connection) {
        int userChoice;

        try {
            System.out.println("=========================");
            System.out.println("\nWelcome to IEEE Library Database");
            System.out.println("Which option would you like to do?" + "\n" + "1.) Adding" + "\n" + "2.) Reading" + "\n"
                    + "3.) Modifying" + "\n" + "4.) Deleting\n" + "5.) Set Operations\n" + "6.) Set Membership\n" + "7.) OLAP\n" + "8.) Exit Database");
            System.out.println("=========================");
            System.out.println("Enter an option:");

            userChoice = scan.nextInt();

            switch (userChoice) {

                case 1:
                    adding(connection);
                    break;
                case 2:
                    reading(connection);
                    break;
                case 3:
                    modifying(connection); 
                    break;
                case 4:
                    deleting(connection);
                    break;
                case 5:
                    setOperation(connection);
                    break;
                case 6: 
                    setMembership(connection);
                    break;
                case 7:
                    OLAP(connection);
                    break;
                case 8:
                    System.out.println("Connection closed Succesfully");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option doesn't exist!");
                    break;
            }
        } catch (InputMismatchException error) {
            System.out.println("ERROR: Choice provided is not a value");
            scan.nextLine();
            menu(connection);
        }
    }

    public static void continueProgram(Connection connection){
        
        try{
            System.out.println("Do you want to continue: Y/N");
            char userInput = scan.next().charAt(0);

            if(userInput == 'Y'){
                menu(connection);
            }
            else{
                System.out.println("Connection closed Succesfully");
                System.exit(0);
            }
        }catch(InputMismatchException e){
            System.out.println("ERROR: Incorrect input");
            continueProgram(connection);
        }


    }

    public static Connection databaseConnection() {
        String url = "jdbc:mysql://localhost:3306/IEEE_Database2?user=root";
        String username = "root";
        String password = "Master18//";
        Connection myConnection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            myConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found!");
        } catch (SQLException e) {
            System.out.println("SQLException : " + e);
            System.out.println("Connection failed!");
        }

        return myConnection;
    }

    //adding method 
    public static void adding(Connection connection) {
        try{       
            String insertQuery = "INSERT INTO book (AuthorsID, PublicationID, TotalPages, ChapterNumbers, Publisher) VALUES (?, ?, ?, ?, ?)";
    
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
    
            preparedStatement.setInt(1, 83);
            preparedStatement.setInt(2, 190);
            preparedStatement.setInt(3, 250);
            preparedStatement.setInt(4, 15);
            preparedStatement.setString(5, "IEEE");
    
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
                continueProgram(connection);
            }
        }
        catch(SQLException E){
            System.out.println("ERROR: " + E.getMessage());
            menu(connection);
        }
    }

    public static void reading(Connection connection) {
        
        try{
            String SQLQuery = "Select * From publication";

            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){

                int publicationID = resultSet.getInt("PublicationID");
                String title = resultSet.getString("Title");
                String topic = resultSet.getString("Topic");
                String date = resultSet.getString("Date");
                String publicationType = resultSet.getString("PublicationType");

                System.out.println("PublicationID: " + publicationID + " Title: " + title + " Topic: " + topic + " Date: " + date + " Publication Type: " + publicationType + "\n");
            }

            continueProgram(connection);

        } catch(SQLException e){

            System.out.println("ERROR: " + e.getMessage());
            menu(connection);

        }
    }

    //updating database method
    public static void modifying(Connection connection) { 

        try {

            String mySQLStatement = "Update podcastepisode Set Duration = ? Where PublicationID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(mySQLStatement);

            preparedStatement.setInt(1, 50);
            preparedStatement.setInt(2, 105);

            int rowsAffeccted = preparedStatement.executeUpdate();

            System.out.println("Database updated. \nRows affected: " + rowsAffeccted);
            continueProgram(connection);

        }catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            menu(connection);
        }
    }

    //Deleting method
    public static void deleting(Connection connection) {
        
        try{

            String query = "Delete From book Where PublicationID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, 128);

            int rowsAffeccted = preparedStatement.executeUpdate();

            System.out.println("Successfully deleted. \nRows affected: " + rowsAffeccted);
            continueProgram(connection);

        }catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            menu(connection);
        }

    }


    public static void setOperation(Connection connection){

       
    }

    public static void setMembership(Connection connection){

        try{

            String query = "Select  PublicationID, Title From publication " 
            + " Where PublicationID In (Select PublicationID From book)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
    
                int publicationID = resultSet.getInt("PublicationID");
                String title = resultSet.getString("Title");

                System.out.println("PublicationID: " + publicationID + " Title: " + title + "\n");
            }

            continueProgram(connection);

        }catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            menu(connection);
        }

    }

    public static void OLAP(Connection connection){

        try{

            int userResponse;

            System.out.println("Do you want to do:\n1.) Group By\n" + "2.) Rollup\n" + "3.) Cube\n");

            userResponse = scan.nextInt();

            switch(userResponse){

                //Group by
                case 1: 
                    //Total number of authors per publication type
                    String query1 = "Select PublicationType, Count(Distinct AuthorsID) As TotalAuthors " + 
                    "From Authors " + 
                    "Group By PublicationType;";
    
                    PreparedStatement preparedStatement = connection.prepareStatement(query1);
    
                    ResultSet resultSet = preparedStatement.executeQuery();
    
                    while(resultSet.next()){
    
                        String publicationID = resultSet.getString("PublicationType");
                        int totalAuthors = resultSet.getInt("TotalAuthors");
    
                        System.out.println("PublicationID: " + publicationID + "Total Authors: " + totalAuthors + "\n");
                    }
    
                    continueProgram(connection);

                    break;

                //Rollup
                case 2:

                    //The amount of publication types within each topic
                    String query2 = "Select Topic, PublicationType, Count(PublicationType) As Count "
                    + "FROM publication "
                    + "Group By Topic, PublicationType With Rollup "
                    + "Having Topic Is Not Null And PublicationType Is Not Null";

                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);

                    ResultSet resultSet2 = preparedStatement2.executeQuery();

                    while(resultSet2.next()){
    
                        String topic = resultSet2.getString("Topic");
                        String publicationType = resultSet2.getString("PublicationType");
                        int count = resultSet2.getInt("Count");
    
                        System.out.println("Topic: " + topic + " Publication Type: " + publicationType + " Count: " + count + "\n");
                    }
    
                    continueProgram(connection);

                    break;
                    
                //Cube
                case 3:

                    break;

                default:
                    System.out.println("ERROR: Invalid input");
                    menu(connection);
            }

        }catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            menu(connection);
        }

    }

}
