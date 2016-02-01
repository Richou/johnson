package net.heanoria.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.heanoria.library.domains.Book;
import net.heanoria.library.tools.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

@RunWith(BlockJUnit4ClassRunner.class)
public class InjecterSimpleTest extends BaseTest {

    private Mapper mapper = null;
    private ObjectMapper objectMapper = null;

    @Before
    public void onSetup() {
        mapper = new Mapper();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBasicDeserialize() throws IOException, IllegalAccessException, NoSuchFieldException {
        String json = readFile("simple-test.json");
        Book book = objectMapper.readValue(json, Book.class);
        Assert.assertNotNull(book);
        Assert.assertEquals("Test", book.getTitle());
        Assert.assertEquals("${title} - Sous test", book.getSubtitle());
        Book injectedBook = mapper.injectValue(book);
        Assert.assertNotNull(injectedBook);
        Assert.assertEquals("Test", injectedBook.getTitle());
        Assert.assertEquals("Test - Sous test", injectedBook.getSubtitle());
    }

    @Test
    public void testBasicDeserializeTwo() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("simple-test-two.json");
        Book book = objectMapper.readValue(json, Book.class);
        Assert.assertNotNull(book);
        Assert.assertEquals("Test", book.getTitle());
        Assert.assertEquals("${title} - ${author} Sous test", book.getSubtitle());
        Book injectedBook = mapper.injectValue(book);
        Assert.assertNotNull(injectedBook);
        Assert.assertEquals("Test", injectedBook.getTitle());
        Assert.assertEquals("Test - Arthur Rimbaud Sous test", injectedBook.getSubtitle());
    }

    @Test
    public void testInvertDeserialize() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("invert-test.json");
        Book book = objectMapper.readValue(json, Book.class);
        Assert.assertNotNull(book);
        Assert.assertEquals("Test", book.getTitle());
        Assert.assertEquals("${title} - Sous test", book.getSubtitle());
        Book injectedBook = mapper.injectValue(book);
        Assert.assertNotNull(injectedBook);
        Assert.assertEquals("Test", injectedBook.getTitle());
        Assert.assertEquals("Test - Sous test", injectedBook.getSubtitle());
    }
}
