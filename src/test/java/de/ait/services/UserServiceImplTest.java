package de.ait.services;

import de.ait.model.User;
import de.ait.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    public String EXIST_USER_EMAIL = "jack3@mail.com";
    public String NOT_EXIST_USER_EMAIL = "jack6@mail.com";
    UserRepository repository;
    UserService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        service = new UserServiceImpl(repository);

        Mockito.when(repository.findAll()).thenReturn(List.of(
                        new User(1L, "jack1", "jack1@mail.com"),
                        new User(2L, "jack2", "jack2@mail.com"),
                        new User(3L, "jack3", "jack3@mail.com"),
                        new User(4L, "jack4", "jack4@mail.com")
                )
        );

        Mockito.when(repository.findByEmail(EXIST_USER_EMAIL)).thenReturn(new User(3L, "jack3", "jack3@mail.com"));
        Mockito.when(repository.findByEmail(NOT_EXIST_USER_EMAIL)).thenReturn(null);


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() {
        List<User> expected = List.of(
                new User(1L, "jack1", "jack1@mail.com"),
                new User(2L, "jack2", "jack2@mail.com"),
                new User(3L, "jack3", "jack3@mail.com"),
                new User(4L, "jack4", "jack4@mail.com")
        );

        List<User> actual = service.getAllUsers();

        Assertions.assertEquals(expected, actual);

    }

    @Test
    void create_user_with_not_existing_email() {
        String name = "jack16";
        String email = NOT_EXIST_USER_EMAIL;

        service.createUser(name, email);

        Mockito.verify(repository, Mockito.times(1)).save(new User(name, email));

    }

    @Test
    void create_user_with_existing_email() {
        String name = "jack3";
        String email = EXIST_USER_EMAIL;

        Assertions.assertAll(
                () -> Assertions.assertThrows(RuntimeException.class, () -> service.createUser(name, email)), // wait Exception
                () -> Mockito.verify(repository, never()).save(any())
        );
        //Mockito.verify(repository, Mockito.times(1)).save(new User(name, email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " ", "", "email.without.at", "email@mail", "@mail.com", "a@short.org"}
    )
    void createUserWithWrongEmail(String invalidEmail) {
        String name = "John";

        assertAll(
                () -> assertThrows(RuntimeException.class, () -> service.createUser(name, invalidEmail)),
                () -> verify(repository, never()).save(any())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", " ", "name", "CamelCase", "Firstname Lastname"}
    )
    void createUserWithWrongName(String invalidName) {
        assertAll(
                () -> assertThrows(RuntimeException.class, () -> service.createUser(invalidName, NOT_EXIST_USER_EMAIL)),
                () -> verify(repository, never()).save(any())
        );
    }

    //Tests for update User

    @ParameterizedTest
    @ValueSource(strings = {
            "", " ", "name", "CamelCase", "Firstname Lastname"}
    )
    void updateUserWithWrongName(String invalidName) {
        assertAll(
                () -> assertThrows(RuntimeException.class, () -> service.updateUser(1L, invalidName, NOT_EXIST_USER_EMAIL)),
                () -> verify(repository, never()).save(any())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Jack", "Anna-Maria"}
    )
    void updateUserName(String validName) {
        assertAll(
                () -> assertThrows(RuntimeException.class, () -> service.updateUser(1L, validName, NOT_EXIST_USER_EMAIL)),
                () -> verify(repository, times(1)).update(new User(1L, validName, NOT_EXIST_USER_EMAIL))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " ", "", "email.without.at", "email@mail", "@mail.com", "a@short.org"}
    )
    void updateUserWithWrongEmail(String invalidEmail) {
        String name = "John";
        assertAll(
                () -> assertThrows(RuntimeException.class, () -> service.updateUser(1L, name, invalidEmail)),
                () -> verify(repository, never()).save(any())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "email@gmail.at", "email@mail.org.com", "email@mail.com"}
    )
    void updateUserEmail(String validEmail) {
        String name = "John";
        assertAll(
                () -> assertThrows(RuntimeException.class, () -> service.updateUser(1L, name, validEmail)),
                () -> verify(repository, times(1)).update(new User(1L, name, validEmail))
        );
    }
}