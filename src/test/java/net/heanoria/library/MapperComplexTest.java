package net.heanoria.library;

import net.heanoria.library.domains.Application;
import net.heanoria.library.domains.Author;
import net.heanoria.library.tools.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

@RunWith(BlockJUnit4ClassRunner.class)
public class MapperComplexTest extends BaseTest {

    private Mapper mapper;

    @Before
    public void onSetup() {
        mapper = new Mapper();
    }

    @Test
    public void testComplexDeserialization() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("inner-object-test-one.json");
        Author author = mapper.readValue(json, Author.class);
        Assert.assertNotNull(author);
        Assert.assertEquals("Robin Hobb", author.getName());
        Assert.assertEquals("http://www.youtube.com/watch?v=so49WpSj9bo", author.getVideo());
        Assert.assertEquals("Robin Hobb was born in 1952", author.getDescription());
    }

    @Test
    public void testComplexDeserializationDeep() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("inner-object-test-two.json");
        Application application = mapper.readValue(json, Application.class);
        Assert.assertNotNull(application);
    }
}
