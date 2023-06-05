package com.example.ics;

import com.example.ics.reposittory.ImagesRepository;
import com.example.ics.reposittory.TagRepository;
import com.example.ics.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ImaggaServiceTest {
    private ImaggaService imaggaService;

    @Mock
    private ThrottleService throttleService;

    @Mock
    private TagService tagService;

    @Mock
    private ImagesService imagesService;

    @Mock
    private ImagesRepository imagesRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private JsonParser jsonParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

//    @Test
//    void testClassifyImage_shouldThrottleTwice() throws Exception {
//        when(throttleService.shouldThrottle()).thenReturn(true);
//
//        String imageUrl = "{\"imageUrl\":\"https:\\/\\/encrypted-tbn0.gstatic.com\\/images?q=tbn:ANd9GcQs8UFTQfU-_h6kwr79UmW8VRmVN97Mwn8ablEqQhVmOQzlP6zyLOOVFdrMqWGyDrmdxWo&usqp=CAU\"}";
//        Images image = new Images();
//        image.setUrl(imageUrl);
//        when(imagesService.findImageByUrl(imageUrl)).thenReturn(null);
//        when(imagesService.saveImage(imageUrl)).thenReturn(image);
//
//        RestTemplate restTemplate = mock(RestTemplate.class);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBasicAuth("apiKey", "apiSecret");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("responseBody", HttpStatus.OK);
//        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(String.class)))
//                .thenReturn(responseEntity);
//
//        when(jsonParser.parseTagsToList(eq("responseBody"), eq(imageUrl))).thenReturn(new ArrayList<>());
//
//        String result = imaggaService.classifyImage(imageUrl);
//
//        assertEquals("Rate limit exceeded. Please try again later.", result);
//        verify(throttleService, times(2)).shouldThrottle();
//    }
}