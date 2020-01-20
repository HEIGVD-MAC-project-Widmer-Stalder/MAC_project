package Actions;

import graph_db.Neo4jUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Like extends Action {

    private UrlValidator urlValidator = new UrlValidator();

    private enum Stages {BEGINNING, WAITURL, WAITLIKECOEF};

    private Stages stage = Stages.BEGINNING;
    private String url;

    public SendMessage processMessage(Message message) {
        // we extract tokens from the message sent by the user
        String s = message.getText();
        String[] tokens = s.split("\\s+");

        // if the message is blank or empty we reply nothing
        if (tokens.length < 1) return null;

        // the reply we'll send to the user
        SendMessage reply = new SendMessage().setChatId(message.getChatId());

        // if the message is the command itself
        if (tokens[0].equals("/like")) {
            reply.setText("Enter the URL of the document to like (or dislike, unlike)");
            stage = Stages.WAITURL;
            return reply;
        } else if (stage == Stages.WAITURL) { // if we are waiting for the url of document to be sent to us
            url = tokens[0];
            if (!urlValidator.isValid(url)) {
                return reply.setText("Invalid URL. Please try again.");
            } else {
                reply.setText("Enter whether you would like to: like, dislike or unlike the document" +
                        "(unlike = reset any of your vote concerning the document)");
                stage = Stages.WAITLIKECOEF;
                return reply;
            }
        } else if (stage == Stages.WAITLIKECOEF) { // if we are waiting for the like coeff to be sent to us
            // we use user message to determine:
            // coef value (-1 for dislike, 1 for like, and 0 for resetting any of these)
            int coef;
            String str;
            switch (tokens[0].charAt(0)) {
                case 'l':
                    coef = 1;
                    str = "liked";
                    break;
                case 'd':
                    coef = -1;
                    str = "disliked";
                    break;
                case 'u':
                    coef = 0;
                    str = "unliked";
                    break;
                default:
                    return reply.setText("unrecognized keyword. please try again with like, dislike or unlike");
            }

            // we execute neo4j query corresponding to liking a document
            try {
                Neo4jUtils.writingQuery("MATCH (user1:User {telegramId: $id})\n" +
                                "MERGE (document1:Document {url: $url})\n" +
                                "MERGE (user1)-[:LIKED{coef: $coef}]->(document1)",
                        parameters("id", message.getFrom().getId(), "url", url,
                                "coef", coef));
                setActionAsCompleted();
                return reply.setText("document was " + str + " successfully");
            } catch (Exception e) {
                e.printStackTrace();
                setActionAsCompleted();
                return reply.setText("an error occurred when trying to like the document. " +
                        "we are sorry for the inconvenience. (maybe try /start and try again)");
            }
        } else {
            // this point should never be reached (if the state machine works correctly)
            System.out.println("error: unexpected value of variable stage in Like.java");
            return null;
        }

    }
}

