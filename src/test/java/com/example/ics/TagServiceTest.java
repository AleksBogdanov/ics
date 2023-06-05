package com.example.ics;

import com.example.ics.entity.Images;
import com.example.ics.entity.Tag;
import com.example.ics.reposittory.TagRepository;
import com.example.ics.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTagsWithId() {
        Images image = new Images();
        image.setId(1L);
        Tag tag1 = new Tag("tag1", 90.0, image);
        Tag tag2 = new Tag("tag2", 95.0, image);
        when(tagRepository.findByImage_id(1L)).thenReturn(Arrays.asList(tag1, tag2));

        List<Tag> result = tagService.getAllTagsWithId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @Test
    void testGetStringOfAllTagsWithId() {
        Images image = new Images();
        image.setId(1L);
        Tag tag1 = new Tag("tag1", 90.0, image);
        Tag tag2 = new Tag("tag2", 95.0, image);
        when(tagRepository.findByImage_id(1L)).thenReturn(Arrays.asList(tag1, tag2));

        String result = tagService.getStringOfAllTagsWithId(1L);

        assertNotNull(result);
    }
    @Test
    void testAddTags() {
        Images image = new Images();
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Tag1", 0.9, image));
        tags.add(new Tag("Tag2", 0.8, image));
        tags.add(new Tag("Tag3", 0.7, image));

        tagService.addTags(tags, image);

        verify(tagRepository, times(3)).save(any(Tag.class));
    }
    
    @Test
    void testFindAllImagesWithTag() {
        when(tagRepository.findImageIdsByTagName("tag1")).thenReturn(Arrays.asList(1L, 2L));

        List<Long> result = tagService.findAllImagesWithTag("tag1");

        assertNotNull(result);
        assertEquals(2, result.size());
    }
    @Test
    void testImageAssociation() {
        Images expectedImage = mock(Images.class);
        Tag tag = new Tag();
        tag.setImage(expectedImage);
        Images actualImage = tag.getImage();
        assertEquals(expectedImage, actualImage);
    }
    @Test
    void testDeleteTagsWithId() {
        Long tagId = 123L;

        tagService.deleteTagsWithId(tagId);

        verify(tagRepository, times(1)).deleteById(tagId);
    }
    @Test
    void testConfidencePercentage() {
        double expectedConfidence = 0.75;
        Tag tag = new Tag();
        tag.setConfidencePercentage(expectedConfidence);
        double actualConfidence = tag.getConfidencePercentage();
        assertEquals(expectedConfidence, actualConfidence);
    }


//    @Test
//    void testGetAllUniqueTags() {
//        Images image1 = new Images();
//        image1.setId(1L);
//
//        Images image2 = new Images();
//        image2.setId(2L);
//
//        Tag tag1 = new Tag("tag1", 90.0, image1);
//        Tag tag2 = new Tag("tag2", 95.0, image1);
//        Tag tag3 = new Tag("tag1", 85.0, image2);
//
//        TagRepository tagRepository = mock(TagRepository.class);
//        when(tagRepository.findAll()).thenReturn(Arrays.asList(tag1, tag2, tag3));
//
//        TagService tagService = new TagService(tagRepository);
//
//        List<String> result = tagService.getAllUniqueTags();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertTrue(result.contains("tag1"));
//        assertTrue(result.contains("tag2"));
//    }
}
