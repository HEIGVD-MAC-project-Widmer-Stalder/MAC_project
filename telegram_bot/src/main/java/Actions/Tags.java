package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class Tags extends Action {

    public SendMessage processMessage(Message message) {
        String s = message.getText();
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        setActionAsCompleted();
        try {
            StatementResult results = Neo4jUtils.readingQuery("MATCH ()-[t:TAGGED]->()\n" +
                    "RETURN DISTINCT t.label, count(*)\n" +
                    "LIMIT 50");
            StringBuilder sb = new StringBuilder();
            sb.append("Stored tags:\n");
            while (results.hasNext()){
                Record r = results.next();
                Value tagLabel = r.get(0);
                Value count = r.get(1);
                String str = tagLabel.asString();
                int c = count.asInt();
                sb.append(str).append(" (count: ").append(c).append(")\n");
            }
            setActionAsCompleted(); // used to tell this action finished and do not have further steps
            return reply.setText(sb.toString()); // reply returned to the user
        } catch (Exception e) {
            e.printStackTrace();
            setActionAsCompleted();
            return reply.setText("an error occurred." +
                    "we are sorry for the inconvenience.");
        }
    }
}