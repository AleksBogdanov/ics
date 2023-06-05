package com.example.ics;

import com.example.ics.entity.Images;
import com.example.ics.reposittory.ImagesRepository;
import com.example.ics.service.ImagesService;
import com.example.ics.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImagesServiceTest {
    private ImagesService imagesService;

    @Mock
    private ImagesRepository imagesRepository;

    @Mock
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imagesService = new ImagesService(imagesRepository, tagService);
    }

    @Test
    void testGetAllImages() {
        List<Images> expectedImages = Arrays.asList(
                createImage(1L, "image1.jpg"),
                createImage(2L, "image2.jpg")
        );
        when(imagesRepository.findAll()).thenReturn(expectedImages);

        List<Images> actualImages = imagesService.getAllImages();

        assertEquals(expectedImages, actualImages);
        verify(imagesRepository, times(1)).findAll();
    }

    @Test
    void testGetImageFromId() {
        Images expectedImage = createImage(1L, "image1.jpg");
        when(imagesRepository.findById(1L)).thenReturn(Optional.of(expectedImage));

        Images actualImage = imagesService.getImageFromId(1L);

        assertEquals(expectedImage, actualImage);
        verify(imagesRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllImagesWithIdList() {
        List<Long> idList = Arrays.asList(1L, 2L);
        Images image1 = createImage(1L, "image1.jpg");
        Images image2 = createImage(2L, "image2.jpg");
        when(imagesRepository.findById(1L)).thenReturn(Optional.of(image1));
        when(imagesRepository.findById(2L)).thenReturn(Optional.of(image2));

        List<Images> expectedImages = Arrays.asList(image1, image2);
        List<Images> actualImages = imagesService.getAllImagesWithIdList(idList);

        assertEquals(expectedImages, actualImages);
        verify(imagesRepository, times(2)).findById(anyLong());
    }

    @Test
    void testGetAllImagesWithTags() {
        String tag = "nature";
        Images image1 = createImage(1L, "www.image.com");
        Images image2 = createImage(2L, "www.image.com");
        when(tagService.findAllImagesWithTag(tag)).thenReturn(Arrays.asList(1L, 2L));
        when(imagesRepository.findById(1L)).thenReturn(Optional.of(image1));
        when(imagesRepository.findById(2L)).thenReturn(Optional.of(image2));
        when(tagService.getStringOfAllTagsWithId(1L)).thenReturn("nature, landscape");
        when(tagService.getStringOfAllTagsWithId(2L)).thenReturn("nature, animals");

        String expectedOutput = "www.image.com Tags:nature, landscapewww.image.com Tags:nature, animals";
        String actualOutput = imagesService.getAllImagesWithTags(tag);

        assertEquals(expectedOutput, actualOutput);
        verify(tagService, times(1)).findAllImagesWithTag(tag);
        verify(imagesRepository, times(2)).findById(anyLong());
        verify(tagService, times(1)).getStringOfAllTagsWithId(1L);
        verify(tagService, times(1)).getStringOfAllTagsWithId(2L);
    }

    @Test
    void testFindImageByUrl() {
        Images expectedImage = createImage(1L, "www.image.com");
        when(imagesRepository.findByUrl("www.image.com")).thenReturn(expectedImage);

        Images actualImage = imagesService.findImageByUrl("www.image.com");

        assertEquals(expectedImage, actualImage);
        verify(imagesRepository, times(1)).findByUrl("www.image.com");
    }


    @Test
    void testDeleteImageWithId() {
        Long imageId = 1L;
        doNothing().when(imagesRepository).deleteById(imageId);

        String result = imagesService.deleteImageWithId(imageId);

        assertEquals("Deleted", result);
        verify(imagesRepository, times(1)).deleteById(imageId);
    }

    private Images createImage(Long id, String url) {
        Images image = new Images();
        image.setId(id);
        image.setUrl(url);
        return image;
    }
}
