package Actions;

import graph_db.Neo4jDriver;
import org.neo4j.driver.v1.AccessMode;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Like extends Action {

    public SendMessage processMessage(Message message) {
        String s = message.getText();
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        String[] tokens = s.split("\\s+");

        if(tokens.length >= 1 && tokens[0].equals("/like")) {
            reply.setText("Enter the URL of the document");
            return reply;
        } else {
            try{
                Driver d = Neo4jDriver.getDriver();
                Session session = d.session(AccessMode.WRITE);
                session.run("MATCH (user1:user {telegramId: $id})\n" +
                        "MERGE (document1:document {url: $url})\n" +
                        "CREATE (user1)-[:LIKE{coefficient: $coef}]->(document1)",
                        parameters("id", message.getFrom().getId(), "url", tokens[0],
                                "coef", 1)); //TODO
                setActionAsCompleted();
                return reply.setText("document was liked");
            } catch (Exception e) {
                setActionAsCompleted();
                return reply.setText("an error occurred when trying to like the document. " +
                        "we are sorry for the inconvenience.");
            }
        }
    }
}

