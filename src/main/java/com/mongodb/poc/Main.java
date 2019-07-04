package com.mongodb.poc;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.poc.view.ViewDefinition;
import com.mongodb.poc.view.ViewSwitcher;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final String URI = "mongodb://admin:password@localhost:27017";
        final List<String> collections = Arrays.asList("coll1",
                "coll2",
                "coll3"
        );
        final String database = "myDB";

        final MongoClient mongo = MongoClients.create(new ConnectionString(URI));

        collections.forEach(coll -> {
            mongo.getDatabase(database).getCollection(coll).createIndex(new Document("a", 1));
        });

        ViewSwitcher vs = new ViewSwitcher(mongo);

        ViewDefinition definition = new ViewDefinition(database, "myView", collections);

        vs.setupView(definition);
        vs.switchViewCollection(definition);
    }
}
