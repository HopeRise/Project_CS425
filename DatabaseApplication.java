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
                    + "3.) Modifying" + "\n" + "4.) Deleting\n" + "5.) Exit Database");
            System.out.println("=========================");
            System.out.println("Enter an option:");

            userChoice = scan.nextInt();

            switch (userChoice) {

                case 1:
                    adding();
                    break;
                case 2:
                    reading();
                    break;
                case 3:
                    modifying(connection); // Pass the Connection object
                    break;
                case 4:
                    deleting();
                    break;
                case 5:
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

    public static void adding() {
        System.out.println("yippie 1");
    }

    public static void reading() {
        System.out.println("yippie 2");
    }

    public static void modifying(Connection connection) { // Accept Connection object as parameter

        try {

            String mySQLStatement = "Update podcastepisode Set Duration = ? Where PublicationID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(mySQLStatement);

            preparedStatement.setInt(1, 40);
            preparedStatement.setInt(2, 105);

            int rowsAffeccted = preparedStatement.executeUpdate();

            System.out.println("Rows affected: " + rowsAffeccted);

        }catch(SQLException e){
            System.out.println("ERROR: Couldn't update attribute");
            menu(connection);
        }
    }

    public static void deleting() {
        System.out.println("yippie 4");
    }

}
