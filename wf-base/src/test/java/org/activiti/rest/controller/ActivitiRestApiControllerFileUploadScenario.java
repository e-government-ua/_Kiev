package org.activiti.rest.controller;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.redis.core.*;
import org.springframework.mock.web.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by alex on 4/17/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestApiControllerFileUploadScenario {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate redisTemplate;

    private BoundValueOperations operations;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initMock() {
        operations = mock(BoundValueOperations.class);
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        when(redisTemplate.boundValueOps(anyString())).thenReturn(operations);
    }

    @Test
    @Ignore // TODO need to fix test
    public void shouldSuccessfulFileUploadToRedis() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "", "application/json", "some file content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/rest/file/uploadToRedis")
                .file(file))
                .andExpect(status().isOk());
    }
}
