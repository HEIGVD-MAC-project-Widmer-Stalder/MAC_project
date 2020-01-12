package Actions;

import graph_db.Neo4jUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Add extends Action {

    UrlValidator urlValidator = new UrlValidator();

    public SendMessage processMessage(Message message) {
        SendMessage reply =  new SendMessage().setChatId(message.getChatId());
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the user just typed the command linked to this action
        if(tokens.length >= 1 && tokens[0].equals("/add")) {
            return reply.setText("enter the url of the document");
        }
        else {
            // if the url is valid (not necessarily reachable though)
            if(urlValidator.isValid(s)) {
                // try to add the document to the db
                try{
                    Neo4jUtils.writingQuery("CREATE (document1:document {url: $url} )", parameters("url", s));
                    setActionAsCompleted();
                    return reply.setText("document was added");
                } catch (Exception e) {
                    setActionAsCompleted();
                    return reply.setText("an error occurred when trying to add the document. " +
                            "we are sorry for the inconvenience.");
                }
            } else {
                // url is invalid so we notify the user about it and do nothing
                return reply.setText("invalid url. please try again.");
            }
        }
    }
}
