package de.morrien.nekeys.url;

import de.morrien.nekeys.NotEnoughKeys;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Created by Timor Morrien
 */
public class NekeysURLStreamHandler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL u) {
        URLConnection connection = null;
        String path = u.getPath();
        if (path.charAt(0) == '/')
            path = path.substring(1);
        URL resourceUrl = ClassLoader.getSystemClassLoader().getResource(path);
        if (resourceUrl == null) {
            resourceUrl = NotEnoughKeys.class.getClassLoader().getResource(path);
        }
        // Workaround for files that Sphinx wants to load but don't actually exist
        if (resourceUrl == null) {
            resourceUrl = ClassLoader.getSystemClassLoader().getResource("assets/nekeys/voice/empty");
        }
        if (resourceUrl == null) {
            resourceUrl = NotEnoughKeys.class.getClassLoader().getResource("assets/nekeys/voice/empty");
        }

        try {
            if (resourceUrl != null)
                connection = resourceUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
