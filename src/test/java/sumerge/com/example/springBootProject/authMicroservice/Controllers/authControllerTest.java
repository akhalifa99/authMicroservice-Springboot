package sumerge.com.example.springBootProject.authMicroservice.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sumerge.com.example.springBootProject.authMicroservice.Models.UserEntity;
import sumerge.com.example.springBootProject.authMicroservice.Security.JwtUtil;
import sumerge.com.example.springBootProject.authMicroservice.Services.UserService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class authControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser_ValidUser_ReturnsOk() throws Exception {

        UserEntity userEntity = new UserEntity("test@example.com", "password");


        mockMvc.perform(post("/auth/register")
                        .content(asJsonString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(userService).registerUser(userEntity.getEmail(), userEntity.getPassword());
    }

    @Test
    public void testLoginUser_ValidCredentials_ReturnsToken() throws Exception {

        UserEntity userEntity = new UserEntity("test@example.com", "password");

        when(userService.authenticateUser(userEntity.getEmail(), userEntity.getPassword())).thenReturn(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userEntity.getEmail(),
                userEntity.getPassword());

        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);

        String token = "generated-jwt-token";
        when(jwtUtil.generateToken(authentication)).thenReturn(token);


        mockMvc.perform(post("/auth/login")
                        .content(asJsonString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(token));


        verify(userService).authenticateUser(userEntity.getEmail(), userEntity.getPassword());
        verify(authenticationManager).authenticate(authentication);
        verify(jwtUtil).generateToken(authentication);
    }

    @Test
    public void testLoginUser_InvalidCredentials_ReturnsUnauthorized() throws Exception {

        UserEntity userEntity = new UserEntity("test@example.com", "password");

        when(userService.authenticateUser(userEntity.getEmail(), userEntity.getPassword())).thenReturn(false);


        mockMvc.perform(post("/auth/login")
                        .content(asJsonString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));


        verify(userService).authenticateUser(userEntity.getEmail(), userEntity.getPassword());
    }


    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
