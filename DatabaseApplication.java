import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.ResultSetMetaData;

import javax.naming.spi.DirStateFactory.Result;

import com.mysql.cj.protocol.Resultset;

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
                    + "3.) Modifying" + "\n" + "4.) Deleting\n" + "5.) 'Set'Functions\n" + "6.) OLAP\n" + "7.) Advanced Aggregated Functions\n" + "8.) With Clause Queries\n" + "9.) Exit Database");
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
                    int userInput;
                    System.out.println("What 'Set' Function would you like to do:\n1.) Set Operation\n2.) Set Membership\n3.) Set Comparison");
                    userInput = scan.nextInt();

                    switch(userInput){

                        case 1:
                            setOperation(connection);
                            break;
                        case 2: 
                            setMembership(connection);
                            break;
                        case 3:
                            setComparison(connection);
                            break;
                        default:
                            System.out.println("ERROR: Input isn't valid");
                            menu(connection);
                            break;
                    }

                    break;
                case 6:
                    OLAP(connection);
                    break;
                case 7:
                    AdvanceAggFunctions(connection);
                    break;
                case 8:
                    withQueries(connection);
                    break;
                case 9:
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
        String url = "jdbc:mysql://localhost:3306/ieee_database2?user=root";
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

            //INSERT INTO publication (PublicationID, Title, Topic, Date, PublicationType) Values ()
            System.out.println("Enter your SQL query:");
            scan.nextLine();
            String sqlQuery = scan.nextLine();
            
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            boolean success = preparedStatement.execute();
            
            if (success) {
                System.out.println("Query executed successfully!");
                // If you expect a result set, you can handle it here
            } else {
                int rowsAffected = preparedStatement.getUpdateCount();
                if (rowsAffected > 0) {
                    System.out.println("Query executed successfully! " + rowsAffected + " row(s) affected.");
                } else {
                    System.out.println("No rows affected.");
                }
            }
            
            continueProgram(connection);
        }catch(SQLException E){
            System.out.println("ERROR: " + E.getMessage());
            menu(connection);
        }
    }

    public static void reading(Connection connection) {
        
        try{

            System.out.println("WHich table would you like to read data from:\n1.) publication\n2.) podcastepisode\n3.) journalarticle\n4.) conference\n5.) book\n6.) authorsconference\n7.) authors\n");
            scan.nextLine();
            String tableName = scan.nextLine();
            String SQLQuery = "Select * From " + tableName ;

            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){

                switch(tableName){

                    case "publication":

                        int publicationID = resultSet.getInt("PublicationID");
                        String title = resultSet.getString("Title");
                        String topic = resultSet.getString("Topic");
                        String date = resultSet.getString("Date");
                        String publicationType = resultSet.getString("PublicationType");
        
                        System.out.println("PublicationID: " + publicationID + " | Title: " + title + " | Topic: " + topic + " | Date: " + date + " | Publication Type: " + publicationType);

                        break;
                    case "podcastepisode":

                        int publicationID2 = resultSet.getInt("PublicationID");
                        int authorsID = resultSet.getInt("AuthorsID");
                        String episodeName = resultSet.getString("Ep_name");
                        int episodeNumber = resultSet.getInt("Ep_num");
                        int duration = resultSet.getInt("Duration");

                        System.out.println("PublicationID: " + publicationID2 + " | AuthorsID: " + authorsID + " | Episode Name: " + episodeName + " | Episode Number: " + episodeNumber + " | Duration: " + duration);

                        break;

                    case "journalarticle": 

                        int authorsID2 = resultSet.getInt("AuthorsID");
                        int publicationID3 = resultSet.getInt("PublicationID");
                        String articleTitle = resultSet.getString("ArticleTitle");
                        int volume = resultSet.getInt("Volume");
                        int issue = resultSet.getInt("Issue");

                        System.out.println("AuthorsID: " + authorsID2 + " | PublicationID: " + publicationID3 + " | Article Title: " + articleTitle + " | Volume: " + volume + " | Issue: " + issue);

                        break;
                    case "conference":

                        int conferenceID = resultSet.getInt("ConferenceID");
                        String name = resultSet.getString("Name");
                        String topic2 = resultSet.getString("Topic");
                        String location = resultSet.getString("Location");
                        String startDate = resultSet.getString("StartDate");
                        String endDate = resultSet.getString("EndDate");
                        String EventFormat = resultSet.getString("EventFormat");

                        System.out.println("ConferenceID: " + conferenceID + " | Name: " + name + " | Topic: " + topic2 + " | Location: " + location + " | Start Date: " + startDate + " | End Date: " + endDate + " | Event Format" + EventFormat);

                        break;


                    case "book":

                        int authorsID3 = resultSet.getInt("AuthorsID");
                        int publicationID4 = resultSet.getInt("PublicationID");
                        int totalPages = resultSet.getInt("TotalPages");
                        int chapterNumbers = resultSet.getInt("ChapterNumbers");
                        String publisher = resultSet.getString("Publisher");

                        System.out.println("AuthorsID: " + authorsID3 + " | PublicationID: " + publicationID4 + " | Total Pages: " + totalPages + " | Chapter Numbers: " + chapterNumbers + " | Publisher: " + publisher);

                        break;

                    case "authorsconference":

                        int AuthorsID = resultSet.getInt("AuthorsID");
                        int ConferenceID = resultSet.getInt("ConferenceID");


                        System.out.println("AuthorsID: " + AuthorsID + " | ConferenceID: " + ConferenceID);

                        break;

                    case "authors":

                        int authorsID4 = resultSet.getInt("AuthorsID");
                        String firstName = resultSet.getString("FirstName");
                        String lastName = resultSet.getString("LastName");
                        String affiliation = resultSet.getString("Affiliation");
                        String publiactionType2 = resultSet.getString("PublicationType");

                        System.out.println("AuthorsID: " + authorsID4 + " | First Name: " + firstName + " | Last Name: " + lastName + " | Affiliation: " + affiliation + " | PublicationType: " + publiactionType2);

                        break;
                    default:
                        System.out.println("ERROR: Invalid input");
                        menu(connection);
                        break;

                }
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

            //"Update podcastepisode Set Duration = ? Where PublicationID = ?";
            System.out.println("Enter an SQL Query: ");
            scan.nextLine();
            String mySQLStatement = scan.nextLine();

            PreparedStatement preparedStatement = connection.prepareStatement(mySQLStatement);

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

            System.out.println("Enter an SQL query: ");
            scan.nextLine();
            String query = scan.nextLine();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

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

            //Select PublicationID, Title From publication Where PublicationID In (Select PublicationID From book)

            System.out.println("Enter an SQL query: ");
            scan.nextLine();
            String query = scan.nextLine(); 

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

           
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getObject(i) + " | ");
                }
                System.out.println(); 
            }

            continueProgram(connection);

        }catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            menu(connection);
        }

    }

    public static void OLAP(Connection connection){

        try{

            //Group by
            //Total number of authors per publication type
            //Select PublicationType, Count(Distinct AuthorsID) As TotalAuthors From Authors Group By PublicationType

            //Rollup
             //The amount of publication types within each topic
            //Select Topic, PublicationType, Count(PublicationType) As Count FROM publication Group By Topic, PublicationType With Rollup Having Topic Is Not Null And PublicationType Is Not Null

            System.out.println("Enter an SQL query: ");
            scan.nextLine();
            String query = scan.nextLine(); 

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

        
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + resultSet.getObject(i) + " | ");
                }
                System.out.println(); 
            }

            continueProgram(connection);

        }catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            menu(connection);
        }

    }

    // window and ranking
    public static void AdvanceAggFunctions(Connection connection){

        
            int userResponse;
            System.out.println("Do you want to do:\n1.) Ranking\n2.) Windowing");
            scan.nextLine();
            userResponse = scan.nextInt();

            try{

                switch(userResponse){

                    //Ranking
                    case 1: 
                        //Rank
                        // "Select Ep_name, Ep_Num, Duration, Rank() Over (Order By Duration Desc) as duration_rank From PodcastEpisode
                        //Dense Rank
                        //Rank authorsID by the amount of conference they have done, first being the highest, last being the lowest
                        //"SELECT AuthorsID, DENSE_RANK() OVER (ORDER BY TotalConferenceID ASC) as ranks FROM (SELECT AuthorsID, SUM(ConferenceID) AS TotalConferenceID FROM authorsconference GROUP BY AuthorsID) AS AuthorTotals
                        //Percent Rank
                        //"Select *, Rank() Over(Order By TotalPages) as p_rank, Round(Percent_Rank() Over (Order By TotalPages), 2)*100 as p_percent_rank From book
                        //Cume Dist
                        //Select *, Round(Round(Cume_Dist() Over (Order By ChapterNumbers), 2)*100, 2) as Cume_Percent From book
                        //Row Numbers
                        // Select Row_Number() Over (Order By TotalPages) as row_num,AuthorsID, PublicationID, TotalPages, ChapterNumbers, Publisher, Rank() Over (Order By TotalPages) As pages_rank From book
                        //Ntile(n)
                        //"Select AuthorsID, ConferenceID, NTILE(5) over (order by AuthorsID) AS Group5 From authorsconference
    
    
                        System.out.println("Enter an SQL query: ");
                        scan.nextLine();
                        String query = scan.nextLine(); 
    
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        ResultSet resultSet = preparedStatement.executeQuery();
    
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
    
                        while(resultSet.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(metaData.getColumnName(i) + ": " + resultSet.getObject(i) + " | ");
                            }
                            System.out.println(); 
                        }
    
                        continueProgram(connection);
                        
                        break;
                    
                    //Windowing
                    case 2: 
    
                        //Moving averages
                        //"SELECT Publisher, ROUND(AVG(TotalPages), 2) AS AvgPage, ROUND(AVG(ChapterNumbers), 2) AS AvgChapter FROM book GROUP BY Publisher"
                        //Running sum/totals
                        //Select Ep_num, Count(*) As episodes_count,Sum(Count(*)) Over (Order By Ep_num) As running_total From podcastepisode Group By Ep_num Order By Ep_num";
    
                        System.out.println("Enter an SQL query: ");
                        scan.nextLine();
                        String query2 = scan.nextLine(); 
    
                        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                        ResultSet resultSet2 = preparedStatement2.executeQuery();
    
                        ResultSetMetaData metaData2 = resultSet2.getMetaData();
                        int columnCount2 = metaData2.getColumnCount();
    
                        while(resultSet2.next()) {
                            for (int i = 1; i <= columnCount2; i++) {
                                System.out.print(metaData2.getColumnName(i) + ": " + resultSet2.getObject(i) + " | ");
                            }
                            System.out.println(); 
                        }
    
                        continueProgram(connection);
    
                        break;
                    default:
                        System.out.println("ERROR: Invalid input");
                        menu(connection);
                        break;
                }

            }catch(SQLException e){
                System.out.println("ERROR: " + e.getMessage());
                menu(connection);
            }
            
            
       
    }

    public static void setComparison(Connection connection){


        try{

            //Select * From publication Union Select * From journalarticle

            System.out.println("Enter an SQL query: ");
            scan.nextLine();
            String query2 = scan.nextLine(); 

            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            ResultSetMetaData metaData2 = resultSet2.getMetaData();
            int columnCount2 = metaData2.getColumnCount();

            while(resultSet2.next()) {
                for (int i = 1; i <= columnCount2; i++) {
                    System.out.print(metaData2.getColumnName(i) + ": " + resultSet2.getObject(i) + " | ");
                }
                System.out.println(); 
            }

            continueProgram(connection);

        }catch(SQLException e){
            System.out.println("Error:" + e.getMessage());
            menu(connection); 
        }

    }

    public static void withQueries(Connection connection){

        try{
            //With EpisodeStatistics As (Select PublicationID, Count(*) As EpisodeNumber, Avg(Duration) as AvgDuration From podcastepisode Group By PublicationID) Select PublicationID, EpisodeNumber, AvgDuration From EpisodeStatistics
            System.out.println("Enter an SQL query: ");
            scan.nextLine();
            String query2 = scan.nextLine(); 

            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            ResultSetMetaData metaData2 = resultSet2.getMetaData();
            int columnCount2 = metaData2.getColumnCount();

            while(resultSet2.next()) {
                for (int i = 1; i <= columnCount2; i++) {
                    System.out.print(metaData2.getColumnName(i) + ": " + resultSet2.getObject(i) + " | ");
                }
                System.out.println(); 
            }

            continueProgram(connection);

    }
    catch(SQLException e){
        System.out.println("Error:" + e.getMessage());
        menu(connection); 
    }

    }
}


