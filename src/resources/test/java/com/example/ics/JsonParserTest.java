package com.example.ics;

import com.example.ics.entity.Images;
import com.example.ics.entity.Tag;
import com.example.ics.reposittory.ImagesRepository;
import com.example.ics.reposittory.TagRepository;
import com.example.ics.service.ImagesService;
import com.example.ics.service.JsonParser;
import com.example.ics.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JsonParserTest {
    private JsonParser jsonParser;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ImagesRepository imagesRepository;

    @Mock
    private ImagesService imagesService;

    @Mock
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jsonParser = new JsonParser(imagesService );
    }

    @Test
    void testParseTagsToList() {
        String jsonResponse = "{\"result\":{\"tags\":[{\"tag\":{\"en\":\"nature\"},\"confidence\":90.5},{\"tag\":{\"en\":\"landscape\"},\"confidence\":80.2},{\"tag\":{\"en\":\"mountain\"},\"confidence\":70.8}]}}";
        String url = "image1.jpg";
        Images image = new Images();
        image.setUrl(url);
        when(imagesService.findImageByUrl(url)).thenReturn(image);

        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(createTag("nature", 90.5, image));
        expectedTags.add(createTag("landscape", 80.2, image));
        expectedTags.add(createTag("mountain", 70.8, image));

        List<Tag> actualTags = jsonParser.parseTagsToList(jsonResponse, url);

        assertEquals(expectedTags.get(0).getTag(), actualTags.get(0).getTag());
        assertEquals(expectedTags.get(1).getTag(), actualTags.get(1).getTag());
        assertEquals(expectedTags.get(2).getTag(), actualTags.get(2).getTag());
    }

    private Tag createTag(String tag, double confidence, Images image) {
        Tag newTag = new Tag();
        newTag.setTag(tag);
        newTag.setConfidencePercentage(confidence);
        newTag.setImage(image);
        return newTag;
    }
}
