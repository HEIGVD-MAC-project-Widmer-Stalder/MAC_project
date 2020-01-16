package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.StatementResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class GetUsersByTag extends Action {

    private enum State {BEGINNING, STEP1, END}

    private State state = State.BEGINNING;

    public SendMessage processMessage(Message message) {

        // we extract tokens from the message sent by the user
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the message is blank or empty we reply nothing
        if (tokens.length < 1) return null;

        // the reply we'll send to the user
        SendMessage reply = new SendMessage().setChatId(message.getChatId());

        try {
            switch (state) {
                case BEGINNING:
                    state = State.STEP1;
                    return reply.setText("enter the tag");
                case STEP1:
                    String tag = tokens[0];
                    // we search every user that liked a document tagged with label: tag
                    StatementResult sr = Neo4jUtils.readingQuery("MATCH ()-[TAGGED{label: $tag}]->(d:Document)\n" +
                            "WITH DISTINCT d\n" +
                            "MATCH (u:User)-[:LIKED{coef: 1}]->(d)\n" +
                            "RETURN DISTINCT u.username", parameters("tag", tag));
                    // We put the result in the reply and return it
                    StringBuilder sb = new StringBuilder();
                    while (sr.hasNext()) {
                        sb.append(sr.next().get(0)).append('\n');
                    }
                    state = State.END; // used to tell the action finished and do not have further steps
                    return reply.setText(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            state = State.END;
            return reply.setText("an error occurred." +
                    "we are sorry for the inconvenience.");
        }

        // we should never reach this point
        new Error("this action should not have been used").printStackTrace();
        return null;
    }

    @Override
    public boolean onGoing() {
        return state != State.END;
    }

}
