package Actions;

import graph_db.Neo4jUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.neo4j.driver.v1.StatementResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Comment extends Action {

    UrlValidator urlValidator = new UrlValidator();

    // states of the state machine
    private enum Stages {BEGINNING, WAITURL, WAITCOMMENT};

    private Stages stage = Stages.BEGINNING;

    private Integer id;
    private String url;
    private String comment;

    public SendMessage processMessage(Message message) {
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        String s = message.getText();

        // if the user just typed the command linked to this action
        if (s.equals("/comment")) {
            stage = Stages.WAITURL;
            id = message.getFrom().getId();
            return reply.setText("enter the URL of the document");
        } else if (stage == Stages.WAITURL) {
            url = s;

            try {
                // Ensure the document has  been already inserted into the DB
                StatementResult results = Neo4jUtils.readingQuery("MATCH (d:Document {url: '" + s + "'}) RETURN d LIMIT 1");
                if (!results.hasNext()) {
                    return reply.setText("Document does not exist. Enter the URL of an existing document URL " +
                            "or type /cancel to cancel the operation. Add a new document by typing /add");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!urlValidator.isValid(url)) {
                return reply.setText("URL is not valid. Please enter again.");
            }
            stage = Stages.WAITCOMMENT;
            return reply.setText("Enter the comment (on PCs, use shift+Enter for newlines)");
        } else if (stage == Stages.WAITCOMMENT) {
            comment = s;
            try {
                Neo4jUtils.writingQuery("MATCH (user:User {telegramId: $id})\n" +
                                "MERGE (document:Document {url: $url})\n" +
                                "CREATE (user)-[:COMMENTED {comment: $comment}]->(document)",
                        parameters("id", (int) id, "url", url, "comment", comment));
                setActionAsCompleted(); // used to tell this action finished and do not have further steps
                return reply.setText("action has been completed"); // reply returned to the user
            } catch (Exception e) {
                e.printStackTrace();
                setActionAsCompleted();
                return reply.setText("an error occurred. " +
                        "we are sorry for the inconvenience. (maybe try /start and try again)");
            }
        }
        // we should never reach this point
        new Exception("Invalid state value").printStackTrace();
        return reply.setText("an error occured;");
    }

}
