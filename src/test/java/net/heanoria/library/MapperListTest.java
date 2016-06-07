package net.heanoria.library;

import net.heanoria.library.domains.Bibliotheque;
import net.heanoria.library.tools.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

@RunWith(BlockJUnit4ClassRunner.class)
public class MapperListTest extends BaseTest {

    private Mapper mapper = null;

    @Before
    public void onSetup() {
        mapper = new Mapper();
    }

    @Test
    public void testListMapperTestOne() throws IllegalAccessException, NoSuchFieldException, IOException {
        String json = readFile("inner-list-test-one.json");
        Bibliotheque bibliotheque = mapper.readValue(json, Bibliotheque.class);
        Assert.assertNotNull(bibliotheque);
        Assert.assertEquals(1.2d, bibliotheque.getVersion(), 0.1);
        Assert.assertEquals(4, bibliotheque.getLinks().size());
        Assert.assertNotNull(bibliotheque.getLinks().get(0));
        Assert.assertEquals("http://site.local/images/img01.png", bibliotheque.getLinks().get(0).getImageUrl());
        Assert.assertEquals("http://home.local/medias/video01.mov", bibliotheque.getLinks().get(0).getMediaUrl());
        Assert.assertEquals("http://pro.local/thumbnails/img01.png", bibliotheque.getLinks().get(0).getThumbnailUrl());

        Assert.assertNotNull(bibliotheque.getLinks().get(1));
        Assert.assertEquals("http://site.local/images/img02.png", bibliotheque.getLinks().get(1).getImageUrl());
        Assert.assertEquals("http://home.local/medias/video02.mov", bibliotheque.getLinks().get(1).getMediaUrl());
        Assert.assertEquals("http://pro.local/thumbnails/img02.png", bibliotheque.getLinks().get(1).getThumbnailUrl());

        Assert.assertNotNull(bibliotheque.getLinks().get(2));
        Assert.assertEquals("http://site.local/images/img03.png", bibliotheque.getLinks().get(2).getImageUrl());
        Assert.assertEquals("http://home.local/medias/video03.mov", bibliotheque.getLinks().get(2).getMediaUrl());
        Assert.assertEquals("http://pro.local/thumbnails/img03.png", bibliotheque.getLinks().get(2).getThumbnailUrl());

        Assert.assertNotNull(bibliotheque.getLinks().get(3));
        Assert.assertEquals("http://site.local/images/img04.png", bibliotheque.getLinks().get(3).getImageUrl());
        Assert.assertEquals("http://home.local/medias/video04.mov", bibliotheque.getLinks().get(3).getMediaUrl());
        Assert.assertEquals("http://pro.local/thumbnails/img04.png", bibliotheque.getLinks().get(3).getThumbnailUrl());


    }
}
