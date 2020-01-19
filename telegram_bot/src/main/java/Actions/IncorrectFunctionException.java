package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class IncorrectFunctionException extends Action {

    public SendMessage processMessage(Message message) {
        // the reply we'll send to the user
        SendMessage reply =  new SendMessage().setChatId(message.getChatId());
        // send the message
        return reply.setText("Function not implemented. Look at bot commands reference for details.");

    }

}
