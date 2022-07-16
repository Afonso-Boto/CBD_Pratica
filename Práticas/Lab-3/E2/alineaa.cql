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
DROP TABLE IF EXISTS video_by_author;
CREATE TABLE video_by_author (
    author          text,
    name            text,
    description     text,
    tags            frozen<set<text>>,
    upload_time     timestamp,

    PRIMARY KEY (author, upload_time, name)
) WITH CLUSTERING ORDER BY (upload_time ASC);

// 3.
DROP TABLE IF EXISTS comment_by_video;
CREATE TABLE comment_by_video (
    user        text,
    video       text,
    content     text,
    time        timestamp,

    PRIMARY KEY (video, time, user)
) WITH CLUSTERING ORDER BY (time DESC);

DROP TABLE IF EXISTS comment_by_user;
CREATE TABLE comment_by_user (
    user        text,
    video       text,
    content     text,
    time        timestamp,

    PRIMARY KEY (user, time, video)
) WITH CLUSTERING ORDER BY (time DESC);

// 4.
DROP TABLE IF EXISTS follower;
CREATE TABLE follower (
     user       text,
     video      text,

     PRIMARY KEY (video, user)
);

// 5.
DROP TABLE IF EXISTS event;
CREATE TABLE event (
    user        text,
    video       text,
    event       text,
    event_time  timestamp,
    video_time  int,

    PRIMARY KEY ((video, user), event_time)
) WITH CLUSTERING ORDER BY (event_time DESC);

// 6.
DROP TABLE IF EXISTS rating;
CREATE TABLE rating (
    id          timeuuid,
    video       text,
    rat         smallint,

    PRIMARY KEY (video, id)
);

// 7.
SELECT * FROM video_by_author WHERE author='man';

// 8.
SELECT * FROM comment_by_user WHERE user='man';

// 9.
SELECT * FROM comment_by_video WHERE video='Assimilated multi-tasking moratorium';

// 10.
SELECT avg(rat) as average_rating, count(*) as total_rating FROM rating WHERE video='Me at the zoo';

