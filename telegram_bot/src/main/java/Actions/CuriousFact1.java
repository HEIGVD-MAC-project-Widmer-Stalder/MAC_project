package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class CuriousFact1 extends Action {

    private enum State {BEGINNING, STEP1, END}

    private State state = State.BEGINNING;

    public SendMessage processMessage(Message message) {

        // we extract tokens from the message sent by the user
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the message is blank or empty we reply nothing
        if (tokens.length < 1) return null;

        // the reply we'll send to the user soon
        SendMessage reply = new SendMessage().setChatId(message.getChatId());

        try {
            // we perform a part of the action depending on the state we are in
            // (an action can have multiple steps until completion)
            switch (state) {
                case BEGINNING:
                    state = State.STEP1;
                    return reply.setText("enter the username of the user");
                case STEP1:
                    String username = tokens[0];
                    StatementResult sr = Neo4jUtils.writingQuery("MATCH (user:User{username: $username})-[lu1:LIKED]->(d:Document)\n" +
                            "MATCH (other_user:User)-[lou1:LIKED]->(d)\n" +
                            "WHERE user <> other_user AND lu1.coef = lou1.coef\n" +
                            "MATCH (other_user)-[lou2:LIKED]->(d2:Document)\n" +
                            "MATCH (user)-[lu2:LIKED]->(d2)\n" +
                            "WHERE d <> d2 AND lou2.coef <> lu2.coef\n" +
                            "RETURN other_user, COUNT(d2) as c\n" +
                            "ORDER BY c", parameters("username", username));
                    // we put result in the reply and return it
                    StringBuilder sb = new StringBuilder();
                    while(sr.hasNext()){
                        Record r = sr.next();
                        Value _username = r.get(0).get("username");
                        Value disagreements = r.get(1);
                        sb.append(_username.asString()).append(" (# of disagreements: ").append(disagreements.asInt())
                                .append(")\n");
                    }
                    state = State.END; // used to tell the action finished and do not have further steps
                    return reply.setText(sb.toString());
            }
        } catch (Exception e) {
            // something when wrong: we tell the user and print some logging
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