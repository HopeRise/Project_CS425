-- Active: 1705953318753@@127.0.0.1@3306@project_cs425

--indexes;
Create index index_name on publication(topic);
show index FROM publication;

--views;

CREATE view AI_pod_ep_70 as
Select p.Topic, e.ep_num
from publication p, podcastepisode e
where p.PublicationID=e.PublicationID and p.topic = "Blockchain" and e.Ep_num >70;

SELECT * FROM AI_pod_ep_70


--triggers;


--func;


--stored porcedure;


--creating tables;

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
    AuthorsID int,
    PublicationID int,
    Ep_name varchar(300),
    Ep_num int,
    Duration int,
    primary KEY (AuthorsID, PublicationID),
    Foreign Key (AuthorsID) REFERENCES Authors(AuthorsID),
    Foreign Key (PublicationID) REFERENCES Publication(PublicationID)
);


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


create table PublicationConference(
    AuthorsID int,
    PublicationID int,
    primary KEY (AuthorsID, PublicationID),
    Foreign Key (AuthorsID) REFERENCES Authors(AuthorsID)
);


