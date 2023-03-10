package edu.tdd.junit.service;

import edu.tdd.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    private UserService userService;
    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");

    @BeforeAll
    void init(){
        System.out.println("Before all: " + this);
    }

    @BeforeEach
    void prepare(){
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Tag("development")
    @Tag("production")
    @Test
    void usersEmptyIfNoUserAdded(){
        System.out.println("Test 1: " + this);
        List<User> users = userService.getAll();
        assertThat(users).isEmpty();
    }
    @Tag("development")
    @Test
    void usersSizeIfUserAdded(){
        System.out.println("Test 2: " + this);
        userService.add(IVAN, PETR);

        List<User> users = userService.getAll();
        assertThat(users).hasSize(2);
    }

    @Tag("production")
    @Test
    void throwExceptionIfUsernameOrPasswordIsNull(){
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> userService.login(null, "1234")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> userService.login("Ivan", null))
        );
    }
    @Tag("development")
    @Test
    void loginSuccessIfUserExists(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());
        assertThat(maybeUser).isPresent();
        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
    }

    /*@Disabled("Disabled for education purpose!")
    @Tag("production")
    @Test
    void usersConvertedToMapById(){
        userService.add(IVAN, PETR);
        Map<Integer, User> users = userService.getAllConvertedById();
        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(IVAN, PETR)
        );
    }*/

    @Test
    void whenId_shouldReturnIt(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());
        assertThat(maybeUser.get().getId()).isEqualTo(IVAN.getId());
    }

    @Test
    void loginFailIfPasswordIsNotCorrect(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), "jdfjkfk");
        assertThat(maybeUser).isEmpty();
    }
    @Test
    void loginFailIfUserDoesNotExist(){
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login("Anastasiia", IVAN.getPassword());
        assertThat(maybeUser).isEmpty();
    }

    @AfterEach
    void deleteDataFromDatabase(){
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool(){
        System.out.println("After all: " + this);
    }
}
