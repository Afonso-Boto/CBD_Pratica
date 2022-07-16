CREATE KEYSPACE IF NOT EXISTS videos_db WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE videos_db;

// 1.
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    username        text,
    name            text,
    email           text,
    register_time   timestamp,

    PRIMARY KEY (username)
);

// 2.
DROP TABLE IF EXISTS video;
CREATE TABLE video (
    author          text,
    title            text,
    description     text,
    tags            frozen<set<text>>,
    upload_timestamp     timestamp,

    PRIMARY KEY (author, upload_timestamp, title)
) WITH CLUSTERING ORDER BY (upload_timestamp ASC);

// 3
DROP TABLE IF EXISTS comment;
CREATE TABLE comment (
    video        text,
    user       text,
    content     text,
    time        timestamp,

    PRIMARY KEY (video, user, time)
) WITH CLUSTERING ORDER BY (user ASC, time DESC);

// 4.
DROP TABLE IF EXISTS follower;
CREATE TABLE follower (
     video       text,
     username      text,

     PRIMARY KEY (video, username)
);

// 5.
DROP TABLE IF EXISTS event;
CREATE TABLE event (
    username        text,
    video       text,
    event       text,
    date  timestamp,
    duration  int,

    PRIMARY KEY ((video, username), date)
) WITH CLUSTERING ORDER BY (date DESC);

// 6.
DROP TABLE IF EXISTS rating;

CREATE TABLE rating (
    rating_id          timeuuid,
    video       text,
    rating        smallint,

    PRIMARY KEY (video, rating, rating_id)
)WITH CLUSTERING ORDER BY (rating DESC, rating_id ASC);

// 7.
SELECT * FROM video WHERE author='afonso';

// 8.
SELECT * FROM comment WHERE user='afonso';

// 9.
SELECT avg(rating) as average_rating, count(*) as total_rating FROM rating WHERE video='video1';

