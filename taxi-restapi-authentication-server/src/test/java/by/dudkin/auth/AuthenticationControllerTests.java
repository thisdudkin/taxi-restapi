package by.dudkin.auth;

import by.dudkin.auth.i18n.I18nUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexander Dudkin
 */
@Import(TestSecurityConfig.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private I18nUtils i18nUtils;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String REGISTER_URI = "/api/auth/register";
    private static final String REGISTER_ADMIN_URI = "/api/auth/register/admin";
    private static final String LOGIN_URI = "/api/auth/login";
    private static final String REFRESH_URI = "/api/auth/refresh";

    @Test
    void whenRegisterUser_thenStatus201() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "testUsername",
                "testEmail@email.com",
                "verySecretPassword",
                Role.ROLE_PASSENGER
        );

        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Mockito.verify(authenticationService).saveUser(Mockito.any(RegistrationRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenRegisterAdmin_thenStatus201() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "admin",
                "admin@admin.com",
                "adminPassword",
                Role.ROLE_ADMIN
        );

        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_ADMIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Mockito.verify(authenticationService).saveAdmin(Mockito.any(RegistrationRequest.class));
    }

    @Test
    void whenRegisterAdmin_thenStatus401() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "admin",
                "admin@admin.com",
                "adminPassword",
                Role.ROLE_ADMIN
        );

        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_ADMIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    void whenRegisterAdmin_thenStatus403() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "admin",
                "admin@admin.com",
                "adminPassword",
                Role.ROLE_ADMIN
        );

        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_ADMIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenLogin_thenStatus200() throws Exception {
        LoginRequest request = new LoginRequest(
                "username",
                "password"
        );

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(authenticationService).login(Mockito.any(LoginRequest.class));
    }

    @Test
    void whenRefreshToken_thenStatus200() throws Exception {
        Mockito.when(authenticationService.refreshToken(Mockito.any(String.class))).thenReturn(new AccessTokenResponse());

        mockMvc.perform(MockMvcRequestBuilders.post(REFRESH_URI)
                        .param("token", RandomStringUtils.randomAlphabetic(256)))
                .andExpect(status().isOk());

        Mockito.verify(authenticationService).refreshToken(Mockito.any(String.class));
    }

}
