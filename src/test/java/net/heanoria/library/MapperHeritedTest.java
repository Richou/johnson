package net.heanoria.library;

import net.heanoria.library.domains.SuperMain;
import net.heanoria.library.tools.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MapperHeritedTest extends BaseTest {

    private Mapper mapper = null;

    @Before
    public void onInit() {
        mapper = new Mapper();
    }

    @Test
    public void testSimpleHerited() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("simple-herited-test.json");
        SuperMain superMain = mapper.readValue(json, SuperMain.class);

        Assert.assertNotNull(superMain);
        Assert.assertEquals(1.2, superMain.getVersion(), 0.1);
        Assert.assertNotNull(superMain.getVersions());
        for(String key: superMain.getVersions().keySet()) {
            Assert.assertEquals("1.0", key);
            Assert.assertEquals("http://site.local/images/img.png", superMain.getVersions().get(key).getLinks().getImageUrl());
            Assert.assertEquals("http://pro.local/thumbnails/img.png", superMain.getVersions().get(key).getLinks().getThumbnailUrl());
            Assert.assertEquals("http://home.local/medias/video.mov", superMain.getVersions().get(key).getLinks().getMediaUrl());
        }
    }

}
