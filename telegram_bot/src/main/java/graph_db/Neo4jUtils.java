package graph_db;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jUtils {

    public static StatementResult writingQuery(String query, Value values) throws Exception {
        Driver d = Neo4jDriver.getDriver();
        Session session = d.session(AccessMode.WRITE);
        return session.run(query, values);
    }

    public static StatementResult readingQuery(String query, Value values) throws Exception {
        Driver d = Neo4jDriver.getDriver();
        Session session = d.session(AccessMode.READ);
        return session.run(query, values);
    }

}
