create table Users (
	username varchar(30) primary key,
	password varchar(80) not null
);

create table Sightings(
	sighting_id int primary key,
	location varchar(30) not null,
	timeStamp datetime,
	title varchar(100),
	description varchar(280),
	imgFile varchar(100),
	flagCount int DEFAULT 0,
	username varchar(30) not null,
	constraint FK_UserSighting foreign key (username) references Users(username)
);

create table Tags(
	tag_id int primary key,
	content varchar(15) not null
);

create table SightingTags(
	sighting_id int,
	tag_id int,
	primary key (sighting_id, tag_id),
	constraint FK_Sighting foreign key (sighting_id)
	references Sightings(sighting_id),
	constraint FK_Tag foreign key (tag_id)
	references Tags(tag_id)
);

create table Comments(
	comment_id int primary key,
	username varchar(30),
	sighting_id int,
	time datetime,
	content varchar (280),
	constraint FK_CommentSighting foreign key (sighting_id)
	references Sightings(sighting_id)
);
