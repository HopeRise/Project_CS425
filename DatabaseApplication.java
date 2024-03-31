import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseApplication{

    public static void main (String [] args){

        // Database URL
        String url = "jdbc:mysql://localhost3306/hi";

        String username = "username";
        String password = "password";
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myConnection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e){

            System.out.println("JDBC driver not found!");
            e.printStackTrace();

        } catch (SQLException e){

            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}