package de.morrien.nekeys.url;

import net.minecraftforge.fml.loading.ModJarURLHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Timor Morrien
 */
public class ResourceHandler extends ModJarURLHandler {
    @Override
    protected URLConnection openConnection(URL u) {
        URLConnection connection = null;
        String path = u.getPath();
        if (path.charAt(0) == '/')
            path = path.substring(1);
        URL resourceUrl = ClassLoader.getSystemClassLoader().getResource(path);

        try {
            if (resourceUrl != null)
                connection = resourceUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection == null) {
            try {
                connection = super.openConnection(new URL("modjar://nekeys/" + path));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
