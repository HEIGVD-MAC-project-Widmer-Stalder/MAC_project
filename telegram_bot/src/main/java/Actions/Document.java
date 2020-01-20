package Actions;

import graph_db.Neo4jUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Document extends Action {

    private enum State {BEGINNING, STEP1, END}

    private State state = State.BEGINNING;

    private static UrlValidator urlValidator = new UrlValidator();

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
                    return reply.setText("enter the url of the document");
                case STEP1:

                    String url = tokens[0];

                    // we check the provided url is valid
                    if(!urlValidator.isValid(url)) {
                        return reply.setText("Invalid URL. Please enter a valid url:");
                    }

                    // check that document exists in db
                    StatementResult sr = Neo4jUtils.readingQuery("MATCH (d:Document{url: $url})\n" +
                            "RETURN d", parameters("url", url));
                    if(!sr.hasNext()) {
                        return reply.setText("This document is not in the database");
                    }

                    // used to build the reply string efficiently
                    StringBuilder sb = new StringBuilder();

                    sb.append("informations about document: ").append(url);

                    // we'll retrieve tags associated with the document
                    sb.append("\n- tags:\n");
                    sr = Neo4jUtils.readingQuery("MATCH (:User)-[tag:TAGGED]->(:Document{url: $url})\n" +
                                    "RETURN tag",
                            parameters("url", url));
                    if(sr.hasNext()) sb.append(sr.next().get("tag").get("label").asString());
                    while(sr.hasNext()) {
                        sb.append(", ").append(sr.next().get("tag").get("label").asString());
                    }

                    // we'll retrieve comments associated with the document
                    sb.append("\n - comments:\n");
                    sr = Neo4jUtils.readingQuery("MATCH (user:User)-[comment:COMMENTED]->(:Document{url: $url})\n" +
                                    "RETURN user, comment",
                            parameters("url", url));
                    if(sr.hasNext()) {
                        Record r = sr.next();
                        sb.append('@').append(r.get("user").get("username").asString()).append(" commented :\n");
                        sb.append(r.get("comment").get("comment").asString()).append('\n');
                    }
                    while(sr.hasNext()) {
                        Record r = sr.next();
                        sb.append('@').append(r.get("user").get("username").asString()).append(" commented :\n");
                        sb.append(r.get("comment").get("comment").asString()).append('\n');
                    }

                    // we'll retrieve the number of likes associated with the document
                    sb.append("\n - number of likes: ");
                    sr = Neo4jUtils.readingQuery("MATCH (:User)-[l:LIKED{coef: 1}]->(:Document{url:$url})\n" +
                            "return COUNT(l)", parameters("url", url));
                    if (sr.hasNext()) {
                        sb.append(sr.next().get(0).asInt()).append('\n');
                    }
                    // we'll retrieve the users who liked the document
                    sb.append("Users who liked the document:\n");
                    sr = Neo4jUtils.readingQuery("MATCH (u:User)-[:LIKED{coef: 1}]->(:Document{url:$url})\n" +
                            "return u", parameters("url", url));
                    if(sr.hasNext()) {
                        Record r = sr.next();
                        sb.append('@').append(r.get("u").get("username").asString());
                    }
                    while(sr.hasNext()) {
                        Record r = sr.next();
                        sb.append('@').append(r.get("u").get("username").asString()).append(", ");
                    }

                    // we'll retrieve the number of dislikes associated with the document
                    sb.append("\n - number of dislikes: ");
                    sr = Neo4jUtils.readingQuery("MATCH (:User)-[l:LIKED{coef: -1}]->(:Document{url:$url})\n" +
                            "return COUNT(l)", parameters("url", url));
                    if (sr.hasNext()) {
                        sb.append(sr.next().get(0).asInt()).append('\n');
                    }
                    // we'll retrieve the the users who disliked the document
                    sb.append("Users who disliked the document:\n");
                    sr = Neo4jUtils.readingQuery("MATCH (u:User)-[:LIKED{coef: -1}]->(:Document{url:$url})\n" +
                            "return u", parameters("url", url));
                    if(sr.hasNext()) {
                        Record r = sr.next();
                        sb.append('@').append(r.get("u").get("username").asString());
                    }
                    while(sr.hasNext()) {
                        Record r = sr.next();
                        sb.append('@').append(r.get("u").get("username").asString()).append(", ");
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
