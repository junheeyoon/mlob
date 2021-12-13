package com.asianaidt.ict.analyca.webserver;

import com.asianaidt.ict.analyca.domain.hostdomain.model.HostCpuStatus;
import com.asianaidt.ict.analyca.domain.hostdomain.model.HostDiskStatus;
import com.asianaidt.ict.analyca.system.dockercore.dto.registry.Catalog;
import com.asianaidt.ict.analyca.system.dockercore.dto.registry.ImageTag;
import com.asianaidt.ict.analyca.webserver.controller.GhostController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WithMockUser(username = "leekn", password = "leekn")
@WebMvcTest(GhostController.class)
public class DockerRegistryTest {
    private final String RegistryHost = "http://10.33.194.14:4506/v2";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GhostController ghostController;

    @Test
    @DisplayName("레지스트리 카탈로그 테스트")
    public void catalogTest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Catalog> entity = restTemplate.getForEntity(RegistryHost + "/_catalog", Catalog.class);
        System.out.println("entity.getBody() = " + entity.getBody());
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("도커 이미지 태그 테스트")
    public void imageTagTest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ImageTag> entity = restTemplate.getForEntity(RegistryHost + "/resource-collector/tags/list", ImageTag.class);
        System.out.println("entity.getBody() = " + entity.getBody());
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        assertEquals(entity.getBody().getName(), "resource-collector");
    }

    @Test
    @DisplayName("도커 이미지 리스트 API 테스트")
    public void imagesTest() throws Exception {
        List<ImageTag> images = new ArrayList<>();
        given(ghostController.images()).willReturn(images);
        images.forEach(System.out::println);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "leekn");
        params.add("password", "leekn");
        MvcResult result = mockMvc.perform(post("/ghost/images")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andReturn();

        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("Ghost Get Service Node Resource Status Test")
    public void getNodeResource() throws Exception {
        List<HostCpuStatus> cpuList = new ArrayList<>();
//        given(ghostController.getNodeStatus()).willReturn(cpuList);
        cpuList.forEach(System.out::println);

        ResultActions result = mockMvc.perform(get("/ghost/node/status")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        System.out.println("result = " + result);


    }
}
