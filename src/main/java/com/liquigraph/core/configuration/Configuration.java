package com.liquigraph.core.configuration;

import com.google.common.base.Optional;
import com.liquigraph.core.writer.*;
import org.neo4j.graphdb.GraphDatabaseService;

import static com.liquigraph.core.configuration.RunMode.RUN_MODE;

/**
 * Immutable Liquigraph configuration settings.
 * Please see {@link ConfigurationBuilder} to
 * create a configuration instance tailored to your environment.
 *
 * @see ConfigurationBuilder
 */
public final class Configuration {

    private final String masterChangelog;
    private final String uri;
    private final Optional<String> username;
    private final Optional<String> password;
    private final ExecutionContexts executionContexts;
    private final ExecutionMode executionMode;
    private final GraphDatabaseService graphDatabaseService;

    Configuration(String masterChangelog,
                  String uri,
                  Optional<String> username,
                  Optional<String> password,
                  ExecutionContexts executionContexts,
                  ExecutionMode executionMode,
                  GraphDatabaseService graphDatabaseService) {

        this.masterChangelog = masterChangelog;
        this.uri = uri;
        this.username = username;
        this.password = password;
        this.executionContexts = executionContexts;
        this.executionMode = executionMode;
        this.graphDatabaseService = graphDatabaseService;
    }




    public String masterChangelog() {
        return masterChangelog;
    }

    public String uri() {
        return uri;
    }

    public Optional<String> username() {
        return username;
    }

    public Optional<String> password() {
        return password;
    }

    public ExecutionContexts executionContexts() {
        return executionContexts;
    }

    public ExecutionMode executionMode() {
        return executionMode;
    }

    public GraphDatabaseService graphDatabaseService() {
        return graphDatabaseService;
    }

    public ChangelogWriter resolveWriter(GraphDatabaseService graphDatabase,
                                         PreconditionExecutor preconditionExecutor,
                                         PreconditionPrinter preconditionPrinter) {
        ExecutionMode executionMode = executionMode();
        if (executionMode == RUN_MODE) {
            return new ChangelogGraphWriter(graphDatabase, preconditionExecutor);
        }
        else if (executionMode instanceof DryRunMode) {
            DryRunMode dryRunMode = (DryRunMode) executionMode;
            return new ChangelogFileWriter(preconditionPrinter, dryRunMode.getOutputFile());
        }
        throw new IllegalStateException("Unsupported <executionMode>: " + executionMode);
    }
}
