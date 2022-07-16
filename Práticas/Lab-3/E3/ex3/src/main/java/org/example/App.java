package org.example;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        Session session = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build().connect("videos_db");

        ResultSet vids;
        //inserts
        session.execute("INSERT INTO video (author, title, description, tags, upload_timestamp) VALUES ('Afonso Boto', 'Video 19',  'descricao', {'tag1', 'tag2'}, '2021-12-1T12:59')");

        ResultSet resultSet = session.execute("SELECT * FROM video WHERE author = 'Afonso Boto'");
        System.out.println(resultSet.all());

        //editions
        session.execute("UPDATE video SET description = 'descricao editada' WHERE author = 'Afonso Boto' AND title = 'Video 19' AND upload_timestamp = '2021-12-1T12:59'");

        resultSet = session.execute("SELECT * FROM video WHERE author = 'Afonso Boto'");
        System.out.println(resultSet.all());

        //deletions
        session.execute("DELETE FROM video WHERE author = 'Afonso Boto' AND title = 'Video 19' AND upload_timestamp = '2021-12-1T12:59'");

        resultSet = session.execute("SELECT * FROM video WHERE author = 'Afonso Boto'");
        System.out.println(resultSet.all());

        //Inserir 5 comentarios para video de Afonso Boto
        session.execute("INSERT INTO comment(video, user, content, time) VALUES('Video 19', 'luis cardoso123', 'comentario 1', '2021-12-1T12:19')");
        session.execute("INSERT INTO comment(video, user, content, time) VALUES('Video 19', 'luis cardoso123', 'comentario 2', '2021-12-1T12:20')");
        session.execute("INSERT INTO comment(video, user, content, time) VALUES('Video 19', 'luis cardoso123', 'comentario 3', '2021-12-1T12:21')");
        session.execute("INSERT INTO comment(video, user, content, time) VALUES('Video 19', 'luis cardoso123', 'comentario 4', '2021-12-1T12:22')");
        session.execute("INSERT INTO comment(video, user, content, time) VALUES('Video 19', 'luis cardoso123', 'comentario 5', '2021-12-1T12:23')");

        //1. Ultimos 3 comentarios de um video
        resultSet = session.execute("SELECT * FROM comment WHERE video = 'Video 19' LIMIT 3");
        System.out.println(resultSet.all());

        //2. Os 5 videos com melhor rating
        resultSet = session.execute("SELECT * FROM RATING LIMIT 5");
        System.out.println(resultSet.all());
        System.out.println("TOP VIDEOS BY RATING");
        System.out.println(resultSet.all().size());


        for(Row r : resultSet.all()){
            String vid = r.getString("video");
            System.out.println("VID ");
            vids = session.execute("SELECT * FROM video WHERE title = '" + vid + "'");
            System.out.println(vids.all());

        }
        //resultSet = session.execute("SELECT * FROM video JOIN (SELECT video, AVG(rating) AS avg_rating FROM rating GROUP BY video) AS ratings ON video.title = ratings.video ORDER BY avg_rating DESC LIMIT 5");


    }
}
