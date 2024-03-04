-- Active: 1705953318753@@127.0.0.1@3306@project_cs425

--indexes;
Create index index_Topic on publication(topic);
show index FROM publication;

create index index_Last_Name on Authors(LastName);
show index FRom authors

Create index 

--views;

CREATE view BC_pod_ep_70 as
Select p.Topic, e.EpisodeNumber
from publication p, podcastepisode e
where p.PublicationID=e.PublicationID and p.topic = "Blockchain" and e.EpisodeNumber >70;

SELECT * FROM BC_pod_ep_70

CREATE view pub_book AS
select p.Topic, b.ChapterNumbers
from publication p, book b
where b.PublicationID=p.PublicationID and p.Topic='Cloud Computing' and b.ChapterNumbers>20;

SELECT * from pub_book

--triggers;


--func;


--stored porcedure;
Create Procedure Authors_Total_Books(In Authors_ID Int)
Begin 

    Declare Book_Total Int;

    Select Count(AuthorsID) Into Book_Total From Book
    Where AuthorsID = Authors_ID;

    Select Concat('Total books for author ', Authors_ID, ': ', Book_Total);

End

Call Authors_Total_Books(10);

Create Procedure Podcast_Duration_Time(In Duration_Number Int)
Begin

    Declare Podcast_Number Int;

    Select Count(*) into Podcast_Number From PodcastEpisode
    Where Duration > Duration_Number;

    Select Concat('Number of podcast episodes greater than ', Duration_Number, ' minutes is/are: ', Podcast_Number);
End

Call Podcast_Duration_Time(30);

Create Function Authors_Affiliation_Count(Affiliation_Name Varchar(400)) Returns Int
DETERMINISTIC
Begin

    Declare author_count Int;

    Select Count(AuthorsID) Into author_count From Authors
    Where Affiliation = Affiliation_Name;

    Return author_count;
End

Select Authors_Affiliation_Count('MIT') as author_count;

Create Function Topic_Count(Topic_Name Varchar(255)) Returns Int  
DETERMINISTIC
Begin 

    Declare Publication_Topic_Count Int;

    Select Count(PublicationID) Into Publication_Topic_Count From Publication 
    Where Topic = Topic_Name;

    Return Publication_Topic_Count;

End  

Select Topic_Count('Blockchain') as Publication_Topic_Count;





Create table Publication(
    PublicationID INT,
    Title VARCHAR(400),
    Topic VARCHAR(100),
    Date VARCHAR(20),
    PublicationType VARCHAR(25),
    PRIMARY KEY (PublicationID)
);

create table Conference(
    ConferenceID int,
    Name VARCHAR(400),
    Topic VARCHAR(200),
    Location VARCHAR(200),
    StartDate VARCHAR(200),
    EndDate VARCHAR(200),
    EventFormat VARCHAR(200),
    PRIMARY KEY (ConferenceID)
);

create table Authors(
    AuthorsID int,
    FirstName varchar(150),
    LastName varchar(150),
    Affiliation VARCHAR(300),
    PublicationType VARCHAR(300),
    PRIMARY key (AuthorsID)
);

create table JournalArticle(
    AuthorsID int,
    PublicationID int,
    ArticleTitle varchar(300),
    Volume int,
    Issue int,
    primary KEY (AuthorsID, PublicationID),
    Foreign Key (AuthorsID) REFERENCES Authors(AuthorsID),
    Foreign Key (PublicationID) REFERENCES Publication(PublicationID)
);

create table PodcastEpisode(
    PublicationID int,    
    AuthorsID int,
    EpisodeName varchar(1000),
    EpisodeNumber int,
    Duration int,
    primary KEY (AuthorsID, PublicationID),
    Foreign Key (AuthorsID) REFERENCES Authors(AuthorsID),
    Foreign Key (PublicationID) REFERENCES Publication(PublicationID)
);

--some of the data was not imported T_T;
Insert Podcastepisode(PublicationID,AuthorsID,EpisodeName,EpisodeNumber,Duration)
Values ('156','46', 'Machine Learning: From Algorithms to Practical Applications', '94', '23'),
('105','58', 'Machine Learning: From Algorithms to Practical ApplicationsBlockchain Beyond Bitcoin: Exploring Diverse Applications', '77', '31'),
('112','19', 'Demystifying Cyber Security: The Essential Guide for Modern Businesses', '16', '31'),
('105','58','Blockchain: Beyond Cryptocurrencies, Revolutionizing Industries', '58','35'),
('170',54, 'Blockchain and Identity: The Future of Digital IDs','16','66'),
('151', '47', 'Building the Connected Home: IoT and Home Automation','59','25');

()




create table Book(
    AuthorsID int,
    PublicationID int,
    TotalPages int,
    ChapterNumbers int,
    Publisher varchar(400),
    primary KEY (AuthorsID, PublicationID),
    Foreign Key (AuthorsID) REFERENCES Authors(AuthorsID),
    Foreign Key (PublicationID) REFERENCES Publication(PublicationID)
);


create table AuthorConference(
    AuthorsID int,
    PublicationID int,
    primary KEY (AuthorsID, PublicationID),
    Foreign Key (AuthorsID) REFERENCES Authors(AuthorsID)
);

Select Count(AuthorsID) as total From Book;



DELIMITER //
CREATE TRIGGER additional_publications
BEFORE INSERT ON authors
FOR EACH ROW
BEGIN
    UPDATE authors
    SET 
    FirstName= NEW.FirstName,LastName= NEW.LastName, Affiliation = NEW.affiliation
    ,PublicationType= NEW.PublicationType
    WHERE authorsID = NEW.authorsID;
END//
DELIMITER ;
insert into authors(AuthorsID, FirstName, LastName, Affiliation, PublicationType)
values('100','Lina', 'Moore', 'USC','Book');
select * from authors;