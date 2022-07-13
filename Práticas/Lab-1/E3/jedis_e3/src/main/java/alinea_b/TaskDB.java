package alinea_b;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

public class TaskDB {
    private Jedis jedis;

    public TaskDB(){
        this.jedis = new Jedis("localhost");
        System.out.println(jedis.info());
    }

    public static String OWNERS_KEY = "owners";
    public static String REAL_STATES_KEY = "real_states";

    public static void main(String[] args) {
        TaskDB db = new TaskDB();
        String[] owners = {"Joao", "Afonso", "Ricardo"};
        Map<String,String> realStates = Map.of("Alugado", "Rua Fernando", "Disponivel", "Avenida Lourenço Peixinho", "Em Manutencao", "Travessa do Catarino");

        for(String owner : owners){
            db.addOwner(owner);
        }

        for(Map.Entry<String,String> entry : realStates.entrySet()){
            db.setRealState(entry.getKey(), entry.getValue());
        }

        db.getOwners().forEach(System.out::println);
        db.getAddresses().forEach((k,v) -> System.out.println(k + ": " + v));
    }

    public void addOwner(String owner){
        jedis.rpush(OWNERS_KEY, owner);
    }

    public void setRealState(String state, String address){
        jedis.hset(REAL_STATES_KEY, state, address);
    }

    public List<String> getOwners(){
        return jedis.lrange(OWNERS_KEY, 0, -1);
    }

    public Map<String, String> getAddresses(){
        return jedis.hgetAll(REAL_STATES_KEY);
    }

}
