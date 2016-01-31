package net.heanoria.library.tools;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Assert;

import java.io.IOException;
import java.net.URL;

public abstract class BaseTest {

    protected String readFile(String filename) {
        try {
            URL resource = Resources.getResource(filename);
            return Resources.toString(resource, Charsets.UTF_8);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }
}
