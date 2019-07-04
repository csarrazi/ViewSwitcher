package com.mongodb.poc.view;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Collections;
import java.util.List;

public class ViewSwitcher {
    private MongoClient mongo;

    public ViewSwitcher(MongoClient mongo) {
        this.mongo = mongo;
    }

    public void setupView(ViewDefinition view) {
        String name = detectCurrentCollection(view);

        if (null != name) {
            return;
        }

        view.getTargetCollections().stream().findFirst()
                .ifPresent(s -> getDatabase(view).createView(view.getViewName(), s, view.getPipeline()));
    }

    /**
     * Switch the collection on which the view points
     *
     * @param view A view
     */
    public void switchViewCollection(ViewDefinition view) {
        String workingCollection = detectCurrentWorkingCollection(view);
        Document success = getDatabase(view)
                .runCommand(new Document()
                        .append("collMod", view.getViewName())
                        .append("viewOn", workingCollection)
                        .append("pipeline", Collections.emptyList())
                );

        if (success.getDouble("ok").equals(0.0)) {
            throw new RuntimeException("Error switching views");
        }

        String currentCollection = detectCurrentCollection(view);
    }

    /**
     * Get the name of the collection on which the view is not pointed
     *
     * @param view
     * @return String
     */
    public String detectCurrentWorkingCollection(ViewDefinition view) {

        String currentCollection = detectCurrentCollection(view);

        List<String> targetCollections = view.getTargetCollections();

        return targetCollections.get((targetCollections.indexOf(currentCollection) + 1) % targetCollections.size());
    }

    /**
     * Get the name of the collection on wich the view  points
     *
     * @param view
     * @return String
     */
    public String detectCurrentCollection(ViewDefinition view) {
        Document viewDefinition = getDatabase(view).listCollections().filter(Filters.eq("name", view.getViewName()))
                .first();

        if (null == viewDefinition) {
            return null;
        }

        return ((Document) viewDefinition.get("options")).getString("viewOn");
    }

    private MongoDatabase getDatabase(ViewDefinition view) {
        return mongo.getDatabase(view.getDbName());
    }
}
