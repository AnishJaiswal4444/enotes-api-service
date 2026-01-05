package Enotes_API_Service.integration;

import Enotes_API_Service.Dto.CategoryDto;
import Enotes_API_Service.Dto.LoginRequest;
import Enotes_API_Service.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private CategoryDto categoryDto = null;
    private Category category = null;

    @BeforeEach
    public void initialize(){
        categoryDto = CategoryDto.builder()
                .id(null)
                .name("JAVA Notes")
                .description("Java notes")
                .isActive(true).build();

        category  = Category.builder()
                .id(null)
                .name("JAVA Notes")
                .description("Java notes")
                .isActive(true)
                .isDeleted(false)
                .build();
    }

    @Test
    public void testSaveCategory() throws Exception {

        String token = generateToken("anish.jaiswal.4444@gmail.com", "1234");
        mockMvc.perform(post("/api/v1/category/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDto)).header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("saved success"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    public String  generateToken(String email, String password) throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String response = mockMvc.perform(post("/api/v1/category/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = mapper.readTree(response);
        String token = root.path("data").path("token").asText();
        return "Bearer" + token;
    }
}
