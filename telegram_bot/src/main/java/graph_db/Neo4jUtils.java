package graph_db;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jUtils {

    private static Session readingSession;
    private static Session writingSession;
    static {
        try {
            readingSession = Neo4jDriver.getDriver().session(AccessMode.READ);
            writingSession = Neo4jDriver.getDriver().session(AccessMode.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StatementResult writingQuery(String query, Value values) throws Exception {
        if(writingSession == null) {
            throw new Exception("cannot communicate with db since we failed to create a session");
        }
        return writingSession.run(query, values);
    }

    public static StatementResult writingQuery(String query) throws Exception {
        if(writingSession == null) {
            throw new Exception("cannot communicate with db since we failed to create a session");
        }
        return writingSession.run(query);
    }

    public static StatementResult readingQuery(String query, Value values) throws Exception {
        if(readingSession == null) {
            throw new Exception("cannot communicate with db since we failed to create a session");
        }
        return readingSession.run(query, values);
    }

    public static StatementResult readingQuery(String query) throws Exception {
        if(readingSession == null) {
            throw new Exception("cannot communicate with db since we failed to create a session");
        }
        return readingSession.run(query);
    }

}
