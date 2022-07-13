package message;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static message.Message.SYSTEM_MESSAGE;

public class SystemMessage {

    private Jedis jedis;
    private boolean logged = false;
    private String CURRENT_USER = "";

    public SystemMessage(){
        this.jedis = new Jedis("localhost");
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opt = -1;
        SystemMessage systemMessage = new SystemMessage();
        while(opt != 7){
            systemMessage.menu(systemMessage.logged);
            opt = sc.nextInt();
            if(!systemMessage.logged){
                switch (opt){
                    case 1:
                        systemMessage.logged = systemMessage.login();
                        break;
                    case 2:
                        systemMessage.register();
                        break;
                }
            }
            else{
                switch (opt){
                    case 1:
                        systemMessage.sendMessage(true);
                        break;
                    case 2:
                        systemMessage.sendMessage(false);
                        break;
                    case 3:
                        systemMessage.follow();
                        break;
                    case 4:
                        systemMessage.unfollow();
                        break;
                    case 5:
                        systemMessage.showMessages();
                        break;
                    case 6:
                        systemMessage.logout();
                        break;
                }
            }
        }
    }

    private void logout() {
        this.logged = false;
        this.CURRENT_USER = "";
    }

    private void showMessages() {
        System.out.println("Messages: " + jedis.lrange("user:"+CURRENT_USER, 0, -1));
    }

    private void unfollow() {
    }

    private void follow() {
    }

    private void sendMessage(boolean isSystemMessage) {
        Scanner sc = new Scanner(System.in);

        if(isSystemMessage){
            System.out.println("Enter message content: ");
            String content = sc.nextLine();
            Message message = new Message(content, CURRENT_USER, SYSTEM_MESSAGE);
            jedis.hset("message:" + message.getId(), message.toMap());
        }
        else{
            System.out.println("Enter message content: ");
            String content = sc.nextLine();
            System.out.println("Enter receiver: ");
            String receiver = sc.nextLine();
            if(!jedis.exists("user:" + receiver)){
                System.out.println("User not found");
                return;
            }
            Message message = new Message(content, CURRENT_USER, receiver);
            jedis.hset("user:" + receiver, message.toMap());
            System.out.println("Message sent!");
        }
    }

    private void register() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        jedis.hset("user:" + name, "password", password);
        System.out.println("User created successfully!");
    }

    private boolean login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        if(jedis.exists("user:" + username)){
            if(jedis.hget("user:" + username, "password").equals(password)){
                CURRENT_USER = username;
                return true;
            }
        }
        System.out.println("Invalid username or password");
        return false;
    }

    public void menu(boolean loggedIn) {
        if(loggedIn){
            System.out.println("-----------------Menu-----------------\n" +
                    "1 - Send message to system\n" +
                    "2 - Send message to user\n" +
                    "3 - Follow user\n" +
                    "4 - Unfollow user\n" +
                    "5 - Show users messages\n" + "" +
                    "6 - logout\n");
        }
        else{
            System.out.println("-----------------Menu-----------------\n" +
                    "1 - Login\n" +
                    "2 - Register\n");
        }




        System.out.println("7 - Exit\n");
        System.out.print("Enter your option: ");
    }
    


}
