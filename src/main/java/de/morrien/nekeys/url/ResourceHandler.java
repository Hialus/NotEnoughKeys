package de.morrien.nekeys.url;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Created by Timor Morrien
 */
public class ResourceHandler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        final URL resourceUrl = getClass().getResource(u.getPath());
        return resourceUrl.openConnection();
    }
}
