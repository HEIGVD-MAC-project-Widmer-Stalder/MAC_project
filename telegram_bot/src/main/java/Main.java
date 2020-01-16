import graph_db.Neo4jDriver;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args) {
        // initialize telegram stuff
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        // register our bot
        try{
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return;
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            // closes the neo4j connection if needed (we don't need to initialize it but we need to close it in case
            // it was used at least once)
            Neo4jDriver.close();
        }
    }

}
