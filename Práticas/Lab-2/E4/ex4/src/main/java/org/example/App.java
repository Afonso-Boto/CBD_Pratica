package org.example;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;


public class App
{
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;
    public App(){
        mongoClient = MongoClients.create();

        database = mongoClient.getDatabase("cbd");
        collection = database.getCollection("cbd");

    }
    public static void main( String[] args )
    {
        App app = new App();
        
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
}