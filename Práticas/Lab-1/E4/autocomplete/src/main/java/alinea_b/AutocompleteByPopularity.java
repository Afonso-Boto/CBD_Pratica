package alinea_b;

import alinea_a.Autocomplete;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AutocompleteByPopularity extends Autocomplete {

    public AutocompleteByPopularity() {
        super();
    }

    public List<String> getValues(List<String> keys) {
        List<String> res = new ArrayList<>();
        for (String key : keys) {
            res.add(jedis.get(key));
        }
        return res;
    }

    public static void main(String[] args) throws FileNotFoundException {
        AutocompleteByPopularity autocomplete = new AutocompleteByPopularity();
        List<String> keys = new ArrayList<>();
        File file = new File("src/main/resources/nomes-pt-2021.csv");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            keys.add(line);
        }
        autocomplete.populate(keys, "csv");
        Scanner sc = new Scanner(System.in);
        String userInput = "";
        do {
            System.out.print("Search for ('Enter' for quit): ");
            userInput = sc.nextLine();
            List<String> res = autocomplete.search(userInput);
            List<Integer> popularity = autocomplete.getValues(res).stream().map(Integer::parseInt).collect(Collectors.toList());
            Map<String, Integer> popularityMap = res.stream().collect(Collectors.toMap(s -> s, s -> popularity.get(res.indexOf(s))));
            res.sort((s1, s2) -> popularityMap.get(s2) - popularityMap.get(s1));
            res.forEach(System.out::println);
            System.out.println();

        }while (!userInput.isBlank());
    }

}
