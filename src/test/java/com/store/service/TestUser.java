package com.store.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Collections;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.store.dto.RoleDTO;
import com.store.dto.UserDTO;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class TestUser {

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private Random random = new Random();

    @Test
    @Rollback(true)
    @Order(1)
    public void saveUserTest() {
        RoleDTO roleDTO = roleService.findByName("ROLE_USER");

        UserDTO[] partitions = {
                new UserDTO("Airely", "Sanchez Miralles", "airely" + random.nextInt(9999),
                        "airely" + random.nextInt(9999) + "@yahoo.com",
                        encoder.encode("leandro123"), Collections.singleton(roleDTO))
        };

        /**
         * para probar campos nulos
         */
        // UserDTO userDTO = partitions[0];
        // userDTO.setEmail(null);

        UserDTO newUserDTO = userService.createUser(partitions[0]);
        assertNotNull(newUserDTO);
    }

    @Test
    @Order(2)
    public void updateUserTest() {
        UserDTO updateUser = new UserDTO();
        updateUser.setName("Leandro");
        updateUser.setLast_name("De Varona");
        updateUser.setPass(encoder.encode("leandro123"));
        updateUser.setEmail("leandrokml@gmail.com");
        updateUser.setPhone("59757731");
        updateUser.setUser("leandromnl");
        updateUser.setId(Long.valueOf(1));
        RoleDTO roleDTO = roleService.findByName("ROLE_ADMIN");
        updateUser.setRoles(Collections.singleton(roleDTO));
        UserDTO updatedUser = userService.editUser(updateUser, updateUser.getId());
        assertNotNull(updatedUser);
    }

}
