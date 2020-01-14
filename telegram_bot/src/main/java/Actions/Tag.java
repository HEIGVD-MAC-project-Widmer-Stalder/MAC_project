package Actions;

import graph_db.Neo4jUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.neo4j.driver.v1.Values.parameters;

public class Tag extends Action {

    UrlValidator urlValidator = new UrlValidator();

    // states of the state machine
    private enum Stages {BEGINNING, WAITURL, WAITTAG};

    private Stages stage = Stages.BEGINNING;

    private Integer id;
    private String url;
    private String[] tags;

    public SendMessage processMessage(Message message) {
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        String s = message.getText();

        // if the user just typed the command linked to this action
        if (s.equals("/tag")) {
            stage = Stages.WAITURL;
            id = message.getFrom().getId();
            return reply.setText("enter the URL of the document");
        } else if (stage == Stages.WAITURL) {
            url = s;
            if (!urlValidator.isValid(url)) {
                return reply.setText("URL is not valid. Please enter again.");
            }
            stage = Stages.WAITTAG;
            return  reply.setText("Enter the tags (separated by , if you enter multiple tags)");
        } else if (stage == Stages.WAITTAG) {
            tags = s.split(",");
            try {
                for(String tag: tags) {
                    Neo4jUtils.writingQuery("MATCH (user:User {telegramId: $id})\n" +
                                    "MERGE (document:Document {url: $url})\n" +
                                    "CREATE (user)-[:TAGGED {label: $label}]->(document)",
                            parameters("id", id, "url", url, "label", tag));
                }
                setActionAsCompleted(); // used to tell this action finished and do not have further steps
                return reply.setText("action has been completed"); // reply returned to the user
            } catch (Exception e) {
                e.printStackTrace();
                setActionAsCompleted();
                return reply.setText("an error occurred. " +
                        "we are sorry for the inconvenience.");
            }
        }
        // we should never reach this point
        new Exception("Invalid state value").printStackTrace();
        return reply.setText("an error occured;");
    }

}
