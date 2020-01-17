package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class User extends Action {

    private enum State {BEGINNING, STEP1, END}

    private State state = State.BEGINNING;

    public SendMessage processMessage(Message message) {

        // we extract tokens from the message sent by the user
        String s = message.getText();

        // the reply we'll send to the user
        SendMessage reply = new SendMessage().setChatId(message.getChatId());

        try {
            switch (state) {
                case BEGINNING:
                    state = State.STEP1;
                    return reply.setText("enter the username of the user");
                case STEP1:
                    String username = s;
                    StatementResult sr = Neo4jUtils.readingQuery("MATCH (u:User{username:$username})\n" +
                                    "RETURN u",
                            parameters("username", username));
                    if(!sr.hasNext()) {
                        return reply.setText("This user does not exist");
                    }
                    sr = Neo4jUtils.readingQuery("MATCH (u:User{username: $username})-[:LIKED{coef: 1}]->(d:Document)\n" +
                            "MATCH (:User)-[t:TAGGED]->(d)\n" +
                            "RETURN DISTINCT t.label", parameters("username", username));
                    // we retrieve each result and put it in the reply
                    StringBuilder sb = new StringBuilder();
                    sb.append(username).append('\n').append("Tags associated with liked documents:\n");
                    while (sr.hasNext()){
                        Value tag = sr.next().get(0);
                        String str = tag.asString();
                        sb.append(str).append('\n');
                    }
                    state = State.END; // used to tell the action finished and does not have further steps
                    return reply.setText(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            state = State.END;
            return reply.setText("an error occurred. " +
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
