package alinea_a;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Autocomplete {
    protected Jedis jedis;

    public Autocomplete(){
        this.jedis = new Jedis("localhost");
        System.out.println(jedis.info());
    }

    public void populate(List<String> keys, String strategy) {
        if(strategy.equals("normal")){
            for(String key : keys){
                jedis.set(key, "");
            }
        }
        else if(strategy.equals("csv")){
            String[] split;
            for(String key : keys){
                split = key.split("[,|;]");
                jedis.set(split[0].toLowerCase(), split[1]);
            }
        }
        else {
            System.err.println("Invalid strategy");
        }

    }

    public List<String> search(String prefix){
        List<String> res = new ArrayList<>();
        ScanResult<String> scanResult;
        String cursor = "0";
        do {
            scanResult = jedis.scan(cursor, new ScanParams().match("*" + prefix + "*"));
            res.addAll(scanResult.getResult());
            cursor = scanResult.getCursor();
        } while (!scanResult.isCompleteIteration());
        return res;
    }

    public void close(){
        jedis.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Autocomplete autocomplete = new Autocomplete();
        List<String> keys = new ArrayList<>();
        File file = new File("src/main/resources/names.txt");
        Scanner rd = new Scanner(file);
        while(rd.hasNext()){
            keys.add(rd.nextLine());
        }
        autocomplete.populate(keys, "normal");
        rd.close();
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("Search for ('Enter' for quit): ");
            String userInput = sc.nextLine();
            if(userInput.isBlank()){
                break;
            }
            autocomplete.search(userInput).forEach(System.out::println);
            System.out.println();
        }

        autocomplete.close();
    }



}
