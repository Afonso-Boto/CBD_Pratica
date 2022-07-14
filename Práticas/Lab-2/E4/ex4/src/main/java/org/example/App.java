package org.example;


import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;


public class App {
    private static BsonField sum;
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;

    public App() {
        mongoClient = MongoClients.create();

        database = mongoClient.getDatabase("cbd");
        collection = database.getCollection("restaurants");

    }

    public static void main(String[] args) {

        App app = new App();
        testSpeed(app);
        app.createIndexAscending("gastronomia");
        app.createIndexAscending("localidade");
        app.createIndexText("nome");
        testSpeed(app);

        System.out.println(app.collection.count());

        //17) de exercicio 2
        System.out.println("EXERCICIO 4 _ C");
        System.out.println("-----------------------------------------------------");
        System.out.println("1 - PRIMEIRO");
        List<Document> result = new ArrayList<>();
        app.collection.aggregate(
                List.of(Aggregates.project(new Document(Map.of("nome", 1, "localidade", 1, "gastronomia", 1))),
                Aggregates.sort(new Document(
                        Map.of("localidade", 1, "nome", 1)
                )))).into(result);

        result.forEach(x -> System.out.println("Localidade: " + x.get("localidade") + ", Nome: " + x.get("nome") + ", Gastronomia: " + x.get("gastronomia")));
        System.out.println("-----------------------------------------------------");
        //18) de exercicio 2
        System.out.println("2 - SEGUNDO");
        result.clear();
        app.collection.aggregate(
                List.of(Aggregates.project(Projections.include("nome", "localidade", "gastronomia", "grades.grade")),
                        Aggregates.match(Filters.eq("localidade", "Brooklyn")),
                        Aggregates.match(Filters.ne("gastronomia", "American")),
                        Aggregates.match(Filters.eq("grades.grade", "A")),
                        Aggregates.sort(Sorts.descending("gastronomia"))
        )).into(result);
        result.forEach(x -> System.out.println("Localidade: " + x.get("localidade") + ", Nome: " + x.get("nome") + ", Gastronomia: " + x.get("gastronomia") + ", Grade: " + x.get("grades")));
        System.out.println("-----------------------------------------------------");

        //19) de exercicio 2
        System.out.println("3 - TERCEIRO");
        result.clear();
        app.collection.aggregate(List.of(
                Aggregates.group("$localidade", Accumulators.sum("total", 1)),
                Aggregates.sort(Sorts.descending("total"))

        )).into(result);
        result.forEach(x -> System.out.println("Localidade: " + x.get("_id") + ", Total: " + x.get("total")));
        System.out.println("-----------------------------------------------------");

        //20) de exercicio 2
        System.out.println("4 - QUARTO");
        result.clear();
        app.collection.aggregate(List.of(
                Aggregates.unwind("$grades"),
                Aggregates.group("$nome", Accumulators.avg("media", "$grades.score")),
                Aggregates.match(Filters.gt("media", 30))
        )).into(result);

        result.forEach(x -> System.out.println("Nome: " + x.get("_id") + ", Media: " + x.get("media")));

        System.out.println("-----------------------------------------------------");

        //21) de exercicio 2
        System.out.println("5 - QUINTO");
        result.clear();

        result = app.search(Filters.and(
               Filters.eq("gastronomia", "Portuguese"),
               Filters.expr(new BasicDBObject("$gt", List.of(
                       new BasicDBObject("$sum", "$grades.score"),
                       50
               ))),
               Filters.lt("address.coord.0", -60)));

        result.forEach(x -> System.out.println("Nome: " + x.get("nome") + ", Gastronomia: " + x.get("gastronomia") + ", localidade: " + x.get("localidade")));
    }

    private void createIndexAscending(String field) {
        Bson index = Indexes.ascending(field);
        collection.createIndex(index);
    }

    private void createIndexText(String field) {
        Bson index = Indexes.text(field);
        collection.createIndex(index);
    }

    public void insert(Document document) {
        collection.insertOne(document);
    }

    public void insert(List<Document> docs) {
        if (docs.size() == 1)
            collection.insertOne(docs.get(0));
        else {
            collection.insertMany(docs);
        }
    }

    public void removeOne(Bson query) {
        collection.deleteOne(query);
    }

    public void removeMany(Bson query) {
        collection.deleteMany(query);
    }

    public void editOne(Bson filter, Bson update) {
        collection.updateOne(filter, update);
    }

    public void editMany(Bson filter, Bson update) {
        collection.updateMany(filter, update);
    }

    public List<Document> search(Bson filter) {
        List<Document> res = new ArrayList<>();
        collection.find(filter).forEach((Consumer<Document>) res::add);
        return res;
    }

    public static void testSpeed(App app) {
        Instant start = Instant.now();
        app.search(Filters.eq("localidade", "Manhattan"));
        app.search(Filters.eq("gastronomia", "American"));
        app.search(Filters.eq("nome", "Pao"));
        Instant end = Instant.now();

        System.out.println("Time: " + (end.toEpochMilli() - start.toEpochMilli()));
    }
}