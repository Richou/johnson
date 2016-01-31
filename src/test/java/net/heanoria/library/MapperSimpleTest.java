package net.heanoria.library;

import net.heanoria.library.domains.Book;
import net.heanoria.library.tools.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

@RunWith(BlockJUnit4ClassRunner.class)
public class MapperSimpleTest extends BaseTest {

    private Mapper mapper = null;

    @Before
    public void onSetup() {
        mapper = new Mapper();
    }

    @Test
    public void testBasicDeserialize() throws IOException, IllegalAccessException, NoSuchFieldException {
        String json = readFile("simple-test.json");
        Book book = mapper.readValue(json, Book.class);
        Assert.assertNotNull(book);
        Assert.assertEquals("Test", book.getTitle());
        Assert.assertEquals("Test - Sous test", book.getSubtitle());
    }

    @Test
    public void testBasicDeserializeTwo() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("simple-test-two.json");
        Book book = mapper.readValue(json, Book.class);
        Assert.assertNotNull(book);
        Assert.assertEquals("Test", book.getTitle());
        Assert.assertEquals("Test - Arthur Rimbaud Sous test", book.getSubtitle());
    }

    @Test
    public void testInvertDeserialize() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("invert-test.json");
        Book book = mapper.readValue(json, Book.class);
        Assert.assertNotNull(book);
        Assert.assertEquals("Test", book.getTitle());
        Assert.assertEquals("Test - Sous test", book.getSubtitle());
    }

}
