package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class Template extends Action {

    public SendMessage processMessage(Message message) {

        // we extract tokens from the message sent by the user
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the message is blank or empty we reply nothing
        if(tokens.length < 1) return null;

        // the reply we'll send to the user
        SendMessage reply =  new SendMessage().setChatId(message.getChatId());

        // if the user just typed the command linked to this action
        // TODO: 1 replace /template with your command
        // TODO: 2 add your command in ActionsResolver.java (so that the bot knows this command exists and maps it)
        if(tokens[0].equals("/template")) {
            return reply.setText("enter the url of the document");
        }
        else {
            try {
                // TODO: 3 take appropriate action (example add a new document is the database)
                //String url = tokens[0];
                //Neo4jUtils.writingQuery("CREATE (document1:Document {url: $url} )", parameters("url", s));
                // TODO: 4 replace replies messages with custom ones if needed

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
