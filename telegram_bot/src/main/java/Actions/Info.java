package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Info extends Action {

    private static final Date buildDate = getClassBuildTime();

    /**
     * Handles files, jar entries, and deployed jar entries in a zip file (EAR).
     * @return The date if it can be determined, or null if not.
     */
    private static Date getClassBuildTime() {
        Date d = null;
        Class<?> currentClass = new Object() {}.getClass().getEnclosingClass();
        URL resource = currentClass.getResource(currentClass.getSimpleName() + ".class");
        if (resource != null) {
            if (resource.getProtocol().equals("file")) {
                try {
                    d = new Date(new File(resource.toURI()).lastModified());
                } catch (URISyntaxException ignored) { }
            } else if (resource.getProtocol().equals("jar")) {
                String path = resource.getPath();
                d = new Date( new File(path.substring(5, path.indexOf("!"))).lastModified() );
            } else if (resource.getProtocol().equals("zip")) {
                String path = resource.getPath();
                File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));
                //long jfodLastModifiedLong = jarFileOnDisk.lastModified ();
                //Date jfodLasModifiedDate = new Date(jfodLastModifiedLong);
                try {
                    JarFile jf = new JarFile (jarFileOnDisk);
                    ZipEntry ze = jf.getEntry (path.substring(path.indexOf("!") + 2));//Skip the ! and the /
                    long zeTimeLong = ze.getTime ();
                    Date zeTimeDate = new Date(zeTimeLong);
                    d = zeTimeDate;
                } catch (Exception ignored) { }
            }
        }
        return d;
    }

    public SendMessage processMessage(Message message) {
        // the reply we'll send to the user
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        return reply.setText("Compile time: " + buildDate.toString());
    }

}
