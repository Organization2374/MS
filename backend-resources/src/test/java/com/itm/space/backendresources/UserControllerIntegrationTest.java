package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends BaseIntegrationTest {

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenCreateUser_thenReturns201() throws Exception {
        UserRequest request = new UserRequest("testuser", "testemail@example.com", "password123", "FirstName", "LastName");

        mvc.perform(requestWithContent(post("/api/users"), request))
                .andExpect(status().isOk());

        verify(userService, times(1)).createUser(any(UserRequest.class));
    }
    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenGetUserById_thenReturns200() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponse response = new UserResponse("FirstName", "LastName", "testemail@example.com", List.of(), List.of());

        Mockito.when(userService.getUserById(userId)).thenReturn(response);

        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenGetHello_thenReturns200() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenUnauthorized_thenReturns403() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void whenInvalidUserRequest_thenReturns400() throws Exception {
        UserRequest invalidRequest = new UserRequest("", "invalidemail", "", "", "");

        mvc.perform(requestWithContent(post("/api/users"), invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
