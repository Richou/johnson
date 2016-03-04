package net.heanoria.library;

import net.heanoria.library.domains.Main;
import net.heanoria.library.domains.SuperMap;
import net.heanoria.library.tools.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

@RunWith(BlockJUnit4ClassRunner.class)
public class MapperMapTest extends BaseTest{

    private Mapper mapper = null;

    @Before
    public void onSetup() {
        mapper = new Mapper();
    }

    @Test
    public void testMapMapperTestOne() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("inner-map-test-one.json");
        Main main = mapper.readValue(json, Main.class);
        Assert.assertNotNull(main);
        Assert.assertEquals(1.2d, main.getVersion(), 0.1);
        Assert.assertNotNull(main.getVersions());
        for(String key: main.getVersions().keySet()) {
            Assert.assertEquals("1.0", key);
            Assert.assertEquals("http://site.local/images/img.png", main.getVersions().get(key).getLinks().getImageUrl());
            Assert.assertEquals("http://pro.local/thumbnails/img.png", main.getVersions().get(key).getLinks().getThumbnailUrl());
            Assert.assertEquals("http://home.local/medias/video.mov", main.getVersions().get(key).getLinks().getMediaUrl());
        }
    }

    @Test
    public void testMapMapperTestTwo () throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("inner-map-test-two.json");
        SuperMap superMap = mapper.readValue(json, SuperMap.class);
        Assert.assertNotNull(superMap);
        Assert.assertEquals(1.2d, superMap.getVersion(), 0.1);
        Assert.assertNotNull(superMap.getSize());
        Assert.assertTrue(superMap.getSize().containsKey("XS"));
        Assert.assertEquals("http://site.local/extra-small", superMap.getSize().get("XS"));
        Assert.assertTrue(superMap.getSize().containsKey("M"));
        Assert.assertEquals("http://pro.local/medium", superMap.getSize().get("M"));
    }
}
