package de.morrien.nekeys.url;

import net.minecraftforge.fml.loading.ModJarURLHandler;

import java.io.File;
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
        try {
            URL resourceUrl = getClass().getResource(u.getPath());
            if (resourceUrl == null) {
                resourceUrl = new File("../build/resources/main/" + u.getPath()).toURL();
            }
            return resourceUrl.openConnection();
        } catch (Exception e) {
            return super.openConnection(u);
        }
    }
}
