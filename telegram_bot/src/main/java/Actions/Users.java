package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Records;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Users extends Action {

    public SendMessage processMessage(Message message) {

        // we extract tokens from the message sent by the user
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the message is blank or empty we reply nothing
        if(tokens.length < 1) return null;

        // the reply we'll send to the user
        SendMessage reply =  new SendMessage().setChatId(message.getChatId());

        // if the user just typed the command linked to this action
        if(tokens[0].equals("/users")) {
            try {
                StatementResult results = Neo4jUtils.readingQuery("MATCH (user:User) return user");
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (results.hasNext() && i++ < 50){
                    Value a = results.next().get("user");
                    Value b = a.get("username");
                    String str = b.asString();
                    sb.append(str);
                    sb.append('\n');
                }
                setActionAsCompleted(); // used to tell this action finished and do not have further steps
                return reply.setText(sb.toString()); // reply returned to the user
            } catch (Exception e) {
                e.printStackTrace();
                setActionAsCompleted();
                return reply.setText("an error occurred when trying to add the document. " +
                        "we are sorry for the inconvenience.");
            }

        } else {
            // one should never reach this point
            new Exception("abnormal state/behavior").printStackTrace();
            return null;
        }
    }

}
