import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import java.io.IOException;

import static org.neo4j.driver.v1.Values.parameters;

class Neo4jDriver{

    private static Driver driver = null;

    //return a singleton driver
    public static Driver getDriver() {
        if(driver == null) {
            driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "test"));
        }
        return driver;
    }

    public static void close() {
        if(driver != null) driver.close();
    }

}
