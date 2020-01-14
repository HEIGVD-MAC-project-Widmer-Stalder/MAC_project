package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class Comment extends Action {

    public SendMessage processMessage(Message message) {
        SendMessage reply =  new SendMessage().setChatId(message.getChatId());
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the message is blank or empty we reply nothing
        if(tokens.length < 1) return null;

        // if the user just typed the command linked to this action
        // TODO: 2 add your command in ActionsResolver.java so that the bot knows this command exists
        if(tokens[0].equals("/comment")) {
            return reply.setText("enter the url of the document");
        }
        else {
            try {
                // TODO: 3 take appropriate action (example add a new document is the database)
                //Neo4jUtils.writingQuery("CREATE (document1:document {url: $url} )", parameters("url", s));

                setActionAsCompleted(); // used to tell this action finished and do not have further steps
                return reply.setText("action has been completed"); // reply returned to the user
            } catch (Exception e) {
                e.printStackTrace();
                setActionAsCompleted();
                return reply.setText("an error occurred when trying to add the document. " +
                        "we are sorry for the inconvenience.");
            }
        }
    }

}
