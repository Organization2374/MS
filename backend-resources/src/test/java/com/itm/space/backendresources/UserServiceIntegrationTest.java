package com.itm.space.backendresources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private Keycloak keycloak;

    private String userId;

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUser() throws Exception {
        String username = "Test3";
        String userJson = "{\n" +
                "  \"username\": \"" + username + "\",\n" +
                "  \"email\": \"test3@test.com\",\n" +
                "  \"password\": \"password\",\n" +
                "  \"firstName\": \"FirstName\",\n" +
                "  \"lastName\": \"LastName\"\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isOk());

        userId = keycloak.realm("ITM").users().search(username).get(0).getId();

    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testGetUserById() throws Exception {
        String userId = "837025c5-2f7f-4f6b-adc9-2361ddce8802";

        mvc.perform(MockMvcRequestBuilders.get("/api/users/" + userId))
                .andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        if (userId != null) {
            keycloak.realm("ITM").users().get(userId).remove();
        }
    }
}
