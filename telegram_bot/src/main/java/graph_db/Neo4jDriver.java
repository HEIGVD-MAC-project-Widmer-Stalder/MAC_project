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

    private final static String propertyFileName = "neo4j.properties";

    //return a singleton driver
    public static Driver getDriver() throws Exception {
        if(driver == null) {
            Properties prop = new Properties();
            InputStream inputStream = Neo4jDriver.class.getClassLoader().getResourceAsStream(propertyFileName);
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                throw new FileNotFoundException(propertyFileName + " file is missing");
            }
            String uri = prop.getProperty("uri");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            if(uri == null || user == null || password == null) {
                throw new Exception("missing properties in " + propertyFileName + " (maybe a missing uri, user or password?)");
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
