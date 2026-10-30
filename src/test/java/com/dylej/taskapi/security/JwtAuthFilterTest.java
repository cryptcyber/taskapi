package com.dylej.taskapi.security;

import com.dylej.taskapi.entity.AppUser;
import com.dylej.taskapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "jwt.secret=test-secret-key-that-is-at-least-32-bytes-long",
        "jwt.expiration=60000"
})
@AutoConfigureMockMvc
class JwtAuthFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(AppUser.builder()
                .username("test-user")
                .password("encoded-password")
                .role("ROLE_USER")
                .build());
    }

    @Test
    void protectedEndpointRejectsRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointAcceptsValidBearerToken() throws Exception {
        String token = jwtService.generateToken("test-user");

        mockMvc.perform(get("/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Spring Boot!"));
    }

    @Test
    void protectedEndpointRejectsMalformedBearerToken() throws Exception {
        mockMvc.perform(get("/hello")
                        .header("Authorization", "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }
}
