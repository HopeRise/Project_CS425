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
