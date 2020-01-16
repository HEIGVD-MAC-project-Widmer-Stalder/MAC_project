//package Actions;
//
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//
//public class GetDocsByTag extends Action {
//
//    private enum State {BEGINNING, STEP1, END}
//
//    private State state = State.BEGINNING;
//
//    public SendMessage processMessage(Message message) {
//
//        // we extract tokens from the message sent by the user
//        String s = message.getText();
//        String[] tokens = s.split("\\s+");
//
//        // if the message is blank or empty we reply nothing
//        if (tokens.length < 1) return null;
//
//        // the reply we'll send to the user
//        SendMessage reply = new SendMessage().setChatId(message.getChatId());
//
//        try {
//            switch (state) {
//                case BEGINNING:
//                    state = State.STEP1;
//                    // TODO 1 replace with your custom text
//                    return reply.setText("enter the tag");
//                case STEP1:
//                    // TODO: 2 take appropriate action (example add a new document is the database)
//                    //String url = tokens[0];
//                    //Neo4jUtils.writingQuery("CREATE (document1:Document {url: $url} )", parameters("url", url));
//                    state = State.END; // used to tell the action finished and do not have further steps
//                    return reply.setText("action applied successfully");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            state = State.END;
//            return reply.setText("an error occurred." +
//                    "we are sorry for the inconvenience.");
//        }
//
//        // TODO: 3 add your command in ActionsResolver.java (so that the bot knows this command exists and maps it)
//
//        // we should never reach this point
//        new Error("this action should not have been used").printStackTrace();
//        return null;
//    }
//
//    @Override
//    public boolean onGoing() {
//        return state != State.END;
//    }
//
//}
