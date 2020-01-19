package Actions;

import graph_db.Neo4jUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.neo4j.driver.v1.StatementResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Add extends Action {

    UrlValidator urlValidator = new UrlValidator();

    public SendMessage processMessage(Message message) {
        SendMessage reply =  new SendMessage().setChatId(message.getChatId());
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the message is blank or empty we reply nothing
        if(tokens.length < 1) return null;

        // if the user just typed the command linked to this action
        if(tokens[0].equals("/add")) {
            return reply.setText("enter the url of the document");
        }
        else {
            // if the url is valid (not necessarily reachable though)
            if(urlValidator.isValid(s)) {
                try {
                    // Ensure the document has not been already inserted into the DB
                    StatementResult results = Neo4jUtils.readingQuery("MATCH (d:Document {url: '" + s + "'}) RETURN d LIMIT 1");
                    if (results.hasNext()) {
                        return reply.setText("Document already exists. Enter another URL or type /cancel to cancel the operation");
                    // Add the document in the DB
                    } else {
                        Neo4jUtils.writingQuery("CREATE (document1:Document {url: $url} )", parameters("url", s));
                        setActionAsCompleted();
                        return reply.setText("document was added");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
