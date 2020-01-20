package graph_db;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import java.io.*;
import java.util.Properties;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jDriver {

    private static Driver driver = null;

    //return a singleton driver
    public static Driver getDriver() throws Exception {
        if(driver == null) {
            String uri = "bolt://" + System.getenv("NEO4J_HOST") + ":7687";
            String user = System.getenv("NEO4J_USER");
            String password = System.getenv("NEO4J_PASSWORD");
            if(uri == null || user == null || password == null) {
                throw new Exception("Missing NEO4J_HOST, NEO4J_USER OR NEO4J_PASSWORD in environment variables");
            }
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        }
        return driver;
    }

    public static void close() {
        if(driver != null) {
            driver.close();
            driver = null;
        }
    }

}
