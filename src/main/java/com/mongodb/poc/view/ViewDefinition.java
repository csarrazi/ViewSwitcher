package com.mongodb.poc.view;

import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;

public class ViewDefinition {
    private String dbName;
    private String viewName;
    private List<String> targetCollections;
    private List<Bson> pipeline;

    public ViewDefinition(String dbName, String viewName, List<String> targetCollections) {
        this(dbName, viewName, targetCollections, Collections.emptyList());
    }

    public ViewDefinition(String dbName, String viewName, List<String> targetCollections, List<Bson> pipeline) {
        if (targetCollections.size() == 0) {
            throw new IllegalArgumentException("Target collections size should be greater or equal to 1");
        }

        this.dbName = dbName;
        this.viewName = viewName;
        this.targetCollections = targetCollections;
        this.pipeline = pipeline;
    }

    public String getDbName() {
        return dbName;
    }

    public String getViewName() {
        return viewName;
    }

    public List<String> getTargetCollections() {
        return targetCollections;
    }

    public List<Bson> getPipeline() {
        return pipeline;
    }
}
