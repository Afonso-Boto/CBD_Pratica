package alinea_a;

import redis.clients.jedis.Jedis;

public class Forum {
    private Jedis jedis;
    public Forum(){
        this.jedis = new Jedis("localhost"); System.out.println(jedis.info());
    }
    public static void main(String[] args) {
        Forum forum = new Forum();
        forum.jedis.hset("user:1", "name", "John");
        System.out.println(forum.jedis.hget("user:1", "name"));
        forum.jedis.set("Student:1", "Afonso");
        System.out.println(forum.jedis.get("Student:1"));
        forum.jedis.set("Student:1:nmec", "89285");
        System.out.println(forum.jedis.get("Student:1:nmec"));
        forum.jedis.mset("Student:2:name", "Ricardo", "Student:2:nmec", "90285");
        System.out.println(forum.jedis.mget("Student:2:name", "Student:2:nmec"));
        forum.jedis.lpush("Student:2:courses", "Java", "Python", "C++");
        System.out.println(forum.jedis.lrange("Student:2:courses", 0, -1));
        forum.jedis.lpop("Student:2:courses");
        System.out.println(forum.jedis.lrange("Student:2:courses", 0, -1));


    }
}
