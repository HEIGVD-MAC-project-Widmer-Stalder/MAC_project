package Actions;

import graph_db.Neo4jDriver;
import org.apache.commons.validator.routines.UrlValidator;
import org.neo4j.driver.v1.AccessMode;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Add implements Action {

    private boolean onGoing = true;
    UrlValidator urlValidator = new UrlValidator();

    public boolean onGoing() {
        return onGoing;
    }

    public SendMessage processMessage(Message message) {
        String s = message.getText();
        SendMessage reply = new SendMessage().setChatId(message.getChatId());

        // if the user just typed the command linked to this action
        if(s.contains("/add")) {
            return reply.setText("enter the url of the document");
        }
        else {
            // if the url is valid (not necessarily reachable though)
            if(urlValidator.isValid(s)) {
                // try to add the document to the db
                try{
                    Driver d = Neo4jDriver.getDriver();
                    Session session = d.session(AccessMode.WRITE);
                    session.run("CREATE (document1:document {url: $url} )", parameters("url", s));
                    onGoing = false;
                    return reply.setText("document was added");
                } catch (Exception e) {
                    onGoing = false;
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
