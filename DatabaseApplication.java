import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseApplication {

    public static void main(String[] args) {
        // Database URL
        String url = "jdbc:mysql://127.0.0.1:3306/?user=root";

        String username = "root";
        String password = "Gilbert:0529";

        Connection myConnection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            myConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException : " + e);
            System.out.println("Connection failed!");
            e.printStackTrace();
        } finally {
            try {
                if (myConnection != null && !myConnection.isClosed()) {
                    myConnection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e);
                e.printStackTrace();
            }
        }
    }
}
