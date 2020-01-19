package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class Documents extends Action {

    public SendMessage processMessage(Message message) {
        String s = message.getText();
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        setActionAsCompleted();
        try {
            StatementResult results = Neo4jUtils.readingQuery("MATCH (d:Document)\n" +
                    "RETURN d\n" +
                    "LIMIT 50");
            StringBuilder sb = new StringBuilder();
            sb.append("Stored documents:\n");
            while (results.hasNext()){
                Value a = results.next().get("d");
                Value b = a.get("url");
                String str = b.asString();
                sb.append(str);
                sb.append('\n');
            }
            setActionAsCompleted(); // used to tell this action finished and do not have further steps
            return reply.setText(sb.toString()); // reply returned to the user
        } catch (Exception e) {
            e.printStackTrace();
            setActionAsCompleted();
            return reply.setText("An error occurred." +
                    "We are sorry for the inconvenience.");
        }
    }
}