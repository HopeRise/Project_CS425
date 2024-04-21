import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

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
        String url = "jdbc:mysql://127.0.0.1:3306/project_cs425?user=root";
        String username = "root";
        String password = "Gilbert:0529";
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

                System.out.println("PublicationID: " + publicationID + " Title: " + title);
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
    
                        System.out.println("PublicationID: " + publicationID + " | Total Authors: " + totalAuthors);
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
    
                        System.out.println("Topic: " + topic + " | Publication Type: " + publicationType + " | Count: " + count);
                    }
    
                    continueProgram(connection);

                    break;
                    
                //Cube: would not work for our database
                case 3:

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

    // window and ranking
    public static void AdvanceAggFunctions(Connection connection){

        
            int userResponse;

            System.out.println("Do you want to do:\n1.) Ranking\n2.) Windowing");

            userResponse = scan.nextInt();
        
            switch(userResponse){

                //Ranking
                case 1: 

                    int choice;
                    
                    System.out.println("Which option do you want to do:\n1.) Rank\n2.) Dense Rank\n3.) Percent Rank\n4.) Cume Dist\n5.) Row Number\n6.) Ntile(n)");
                    choice = scan.nextInt();

                    switch (choice){

                        case 1: 
                            try{
                                // Rank the podcast episodes based on the duration of the episode, displaying only the episode name, number, duration and rank
                                String query1 = "Select Ep_name, Ep_Num, Duration, " +
                                "Rank() Over (Order By Duration Desc) as duration_rank " +
                                "From PodcastEpisode";

                                PreparedStatement preparedStatement = connection.prepareStatement(query1);
                                ResultSet resultSet = preparedStatement.executeQuery();


                                while(resultSet.next()) {
                                    
                                    String episodeName = resultSet.getString("Ep_name");
                                    int episodeNumber = resultSet.getInt("Ep_Num");
                                    int duration = resultSet.getInt("Duration");
                                    int durationRank = resultSet.getInt("duration_rank");

                                    System.out.println("Episode Name: " + episodeName + " | Episode Number: " + episodeNumber + " | Duration: " + duration + " | Duration Rank: " + durationRank);

                                }

                                continueProgram(connection);
                            }catch(SQLException e){
                                System.out.println("ERROR: " + e.getMessage());
                                menu(connection);
                            }

                            break;
                        case 2:

                            try{
                                 //Rank authorsID by the amount of conference they have done, first being the highest, last being the lowest
                                String query2 = "SELECT AuthorsID, DENSE_RANK() OVER (ORDER BY TotalConferenceID ASC) as ranks " + 
                                "FROM ( " + 
                                "SELECT AuthorsID, SUM(ConferenceID) AS TotalConferenceID " +
                                "FROM authorsconference " +
                                "GROUP BY AuthorsID "+
                                ") AS AuthorTotals";
    
                                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
    
                                ResultSet resultSet2 = preparedStatement2.executeQuery();
    
                                while(resultSet2.next()) {
                                    String Auth = resultSet2.getString("AuthorsID");
                                    double ranks = resultSet2.getDouble("ranks");
    
                                    System.out.println("AuthorsID: " + Auth + " | Ranks: " + ranks);
                                }
    
                                continueProgram(connection);
                            }catch(SQLException e){
                                System.out.println("ERROR: " + e.getMessage());
                                menu(connection);
                            }

                            break;
                        case 3:

                            try{
                                String query3 = "Select *, Rank() Over(Order By TotalPages) as p_rank, " +
                                "Round(Percent_Rank() Over (Order By TotalPages), 2)*100 as p_percent_rank From book";

                                PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                                ResultSet resultSet3 = preparedStatement3.executeQuery();

                                while(resultSet3.next()){
                                    int AID = resultSet3.getInt("AuthorsID");
                                    int PID = resultSet3.getInt("PublicationID");
                                    int totalPages = resultSet3.getInt("TotalPages");
                                    int chapterNumbers = resultSet3.getInt("ChapterNumbers");
                                    String publisher = resultSet3.getString("Publisher");
                                    double p_rank = resultSet3.getDouble("p_rank");
                                    int p_PercentRank = resultSet3.getInt("p_percent_rank");

                                    System.out.println("AuthorsID: " + AID + " | PublicationID: " + PID + " | Total Pages: " + totalPages + " | Chapter Numbers: " + chapterNumbers + " | Publisher: " + publisher + " | Rank: " + p_rank + " | Percent Rank: " + p_PercentRank);
                                    
                                }

                                continueProgram(connection);
                                
                            }catch(SQLException e){
                                System.out.println("ERROR: " + e.getMessage());
                                menu(connection);
                            }

                            break;
                        case 4:

                        try{
                            String query4 = "Select *, Round(Round(Cume_Dist() Over (Order By ChapterNumbers), 2)*100, 2) as Cume_Percent From book";

                            PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
                            ResultSet resultSet4 = preparedStatement4.executeQuery();

                            while(resultSet4.next()){
                                int AuthorsID = resultSet4.getInt("AuthorsID");
                                int PublicationID = resultSet4.getInt("PublicationID");
                                int pages = resultSet4.getInt("TotalPages");
                                int chapters = resultSet4.getInt("ChapterNumbers");
                                String publisher = resultSet4.getString("Publisher");
                                int cumeDistPercent = resultSet4.getInt("Cume_Percent");

                                System.out.println("AuthorsID: " + AuthorsID + " | PublicationID: " + PublicationID + " | Total Pages: " + pages + " | Chapter Numbers: " + chapters + " | Publisher: " + publisher + " | Cume Dist Percent: " + cumeDistPercent);
                                
                            }

                            continueProgram(connection);
                            
                        }catch(SQLException e){
                            System.out.println("ERROR: " + e.getMessage());
                            menu(connection);
                        }

                            break;
                        case 5:

                            try{

                                String query5 = "Select Row_Number() Over (Order By TotalPages) as row_num, "
                                + "AuthorsID, PublicationID, TotalPages, ChapterNumbers, Publisher, Rank() Over (Order By TotalPages) As pages_rank "+
                                "From book";

                                PreparedStatement preparedStatement5 = connection.prepareStatement(query5);
                                ResultSet resultSet5 = preparedStatement5.executeQuery();

                                while(resultSet5.next()){

                                    int rowNumber = resultSet5.getInt("row_num");
                                    int authorsID = resultSet5.getInt("AuthorsID");
                                    int publicationID = resultSet5.getInt("PublicationID");
                                    int totPages = resultSet5.getInt("TotalPages");
                                    int chapterAmount = resultSet5.getInt("ChapterNumbers");
                                    String Publisher = resultSet5.getString("Publisher");
                                    int rank = resultSet5.getInt("pages_rank");

                                    System.out.println("Row Number: " + rowNumber + " | AuthorsID: " + authorsID + " | PublicationID: " + publicationID + " | Total Pages: " + totPages + " | Chapter Numbers: " + chapterAmount + " | Publisher: " + Publisher + " | Rank: " + rank);

                                }

                                continueProgram(connection);


                            }catch(SQLException e){
                                System.out.println("ERROR: " + e.getMessage());
                                menu(connection);
                            }


                            break;
                        case 6:
                            
                            try{

                                String query6 = "Select AuthorsID, ConferenceID," +
                                "NTILE(5) over (order by AuthorsID) AS Group5 " +
                                "From authorsconference";

                                PreparedStatement preparedStatement6 = connection.prepareStatement(query6);
                                ResultSet resultSet6 = preparedStatement6.executeQuery();


                                while(resultSet6.next()) {
                                    
                                    int authorsID = resultSet6.getInt("AuthorsID");
                                    int conferenceID = resultSet6.getInt("ConferenceID");
                                    int ntile = resultSet6.getInt("Group5");

                                    System.out.println("AuthorsID: " + authorsID + " | ConferenceID: " + conferenceID + " | Ntile(5): " + ntile);
                                    
                                }
    
                                continueProgram(connection);

                            }catch(SQLException e){
                                System.out.println("ERROR: " + e.getMessage());
                                menu(connection);
                            }    

                            break;
                        default:
                            System.out.println("ERROR: Invalid input");
                            menu(connection);
                    }
                    
                    break;
                
                //Windowing
                case 2: 

                    int choice2;    
                    System.out.println("Which option do you want to do:\n1.) Moving Averages\n2.) Running Sum/Totals");
                    choice2 = scan.nextInt();

                    //Moving Averages
                    if(choice2 == 1){
                        
                        try{
                            //Average amount of page and chapter numbers within each publisher
                            String query2 = "SELECT Publisher, " + 
                            "ROUND(AVG(TotalPages), 2) AS AvgPage, ROUND(AVG(ChapterNumbers), 2) AS AvgChapter " + 
                            "FROM book " +
                            "GROUP BY Publisher";
        
                            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
        
                            ResultSet resultSet2 = preparedStatement2.executeQuery();
        
                            while(resultSet2.next()) {
                                String publisher = resultSet2.getString("Publisher");
                                double avgPage = resultSet2.getDouble("AvgPage");
                                double avgChapter = resultSet2.getDouble("AvgChapter");
        
                                System.out.println("Publisher: " + publisher + " | AvgPage: " + avgPage + " | AvgChapter: " + avgChapter);
                            }
        
                            continueProgram(connection);

                        }catch(SQLException e){
                            System.out.println("ERROR: " + e.getMessage());
                            menu(connection);
                        }
                  
                    //Running Sum/Totals
                    }else if (choice2 == 2){
                        
                        try{

                            String query2 = "Select Ep_num, Count(*) As episodes_count, "
                            + "Sum(Count(*)) Over (Order By Ep_num) As running_total From podcastepisode "
                            + "Group By Ep_num "
                            + "Order By Ep_num";

                            PreparedStatement preparedStatement3 = connection.prepareStatement(query2);
                            ResultSet resultSet3 = preparedStatement3.executeQuery();

                            while(resultSet3.next()) {
                                
                                int episodeNumber = resultSet3.getInt("Ep_num");
                                int episodeCount = resultSet3.getInt("episodes_count");
                                int runningTotal = resultSet3.getInt("running_total");

                                System.out.println("Episode Number: " + episodeNumber + " | Episodes Count: " + episodeCount + " | Running Total: " + runningTotal);

                            }
        
                            continueProgram(connection);

                        }catch(SQLException e){
                            System.out.println("ERROR: " + e.getMessage());
                            menu(connection);
                        }
                    }
                    else{
                        System.out.println("ERROR: Invalid input");
                        menu(connection);
                    }

                    break;
                default:
                    System.out.println("ERROR: Invalid input");
                    menu(connection);
                    break;
            }
       
    }

    public static void setComparison(Connection connection){
        try{
            String query = ("SELECT * FROM publication " + "UNION " + "SELECT * FROM journalarticle");


            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int publicationID = resultSet.getInt("PublicationID");
                String title = resultSet.getString("Title");
                String topic = resultSet.getString("Topic");
                String date = resultSet.getString("Date");
                String publicationType = resultSet.getString("PublicationType");

                System.out.println("PublicationID: " + publicationID + " | Title: " + title + " | Topic: " + topic + " | Date: " + date + " | Publication Type: " + publicationType);
            }
            continueProgram(connection); // continue 

        }catch(SQLException e){
            System.out.println("Error:" + e.getMessage());
            menu(connection); // return to menu
        }

    }

    public static void withQueries(Connection connection){
        try{
            String withQuery = "WITH EpisodeStatistics AS (" +
            "SELECT " +
                "PublicationID, " +
                "COUNT(*) AS EpisodeNumber, " +
                "AVG(duration) AS AvgDuration " +
            "FROM " +
                "podcastepisode " +
            "GROUP BY " +
                "PublicationID " +
            ")" +
        "SELECT " +
            "PublicationID, " +
            "EpisodeNumber, " +
            "AvgDuration " +
        "FROM " +
            "EpisodeStatistics";
    
        PreparedStatement preparedStatement = connection.prepareStatement(withQuery);
        ResultSet resultSet= preparedStatement.executeQuery();

        while(resultSet.next()){
            int publcationID = resultSet.getInt("PublicationID");
            int episodeNumber = resultSet.getInt("EpisodeNumber");
            double avgDuration = resultSet.getDouble("AvgDuration");

            System.out.println("PublcationID:" + publcationID + ",EpisodeNumber:" + episodeNumber + ", AvgDuration" + avgDuration); // output result    
        }
        continueProgram(connection); // continue 

    }
    catch(SQLException e){
        System.out.println("Error:" + e.getMessage());
        menu(connection); // return to menu
    }

    }
}


