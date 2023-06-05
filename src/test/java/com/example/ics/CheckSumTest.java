package com.example.ics;

import com.example.ics.service.CheckSum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CheckSumTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CheckSum checkSum;


    @Test
    public void testGetChecksum_Base64_URL() {
        byte[] imageBytes = {1, 2, 3, 4, 5};  // Mock image bytes
        String base64Url = Base64.encodeBase64String(imageBytes);
        String expectedChecksum = DigestUtils.sha256Hex(imageBytes);

        String actualChecksum = checkSum.getChecksum(base64Url);

        assertEquals(expectedChecksum, actualChecksum);
    }



}
