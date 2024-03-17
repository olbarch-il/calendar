package com.targil.calendar;

import com.targil.calendar.users.model.entity.UserEntity;
import com.targil.calendar.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RateLimitingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${rate.limit.max-requests-per-minute}")
    private int MAX_REQUESTS_PER_MINUTE;

    private String email = "rateLimitTest@example.com";
    private String password = "password";
    private String urlTemplate = "http://localhost:8080/api/events";

    @BeforeEach
    public void setup() {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Test
    public void testRateLimiting() throws Exception {
        for (int i = 0; i < MAX_REQUESTS_PER_MINUTE; i++) {
            mockMvc.perform(get(urlTemplate)
                    .with(httpBasic(email, password))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(i < 5 ? status().isOk() : status().isTooManyRequests());
        }
    }
}
