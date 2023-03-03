package edu.tdd.junit.service;

import edu.tdd.junit.dto.User;

import java.util.*;

public class UserService {

    private final List<User> users = new ArrayList<>();
    public List<User> getAll() {
        return users;
    }

    public void add(User... users) {
        /* Correct implementation of the method
//        this.users.addAll(Arrays.asList(users));
        * */

/* Incorrect implementation of the method to show mistakes */
        int usersCount = users.length;
        if (usersCount == 0) {
            return;
        } else if (usersCount == 1) {
            this.users.addAll(Arrays.asList(users));
        } else {
            if (this.users.isEmpty()) {
                User firstUser = Arrays.stream(users).findFirst().get();
                this.users.add(firstUser);

                for (int i = 1; i < usersCount; i++) {
                    this.users.add(User.of(i+1, firstUser.getUsername(), firstUser.getPassword()));
                }
            /*} else {
                User firstUser = this.users.stream().findFirst().get();
                for (int i = 1; i < usersCount; i++) {
                    this.users.add(firstUser);
                }*/
            }
        }
    }

    public Optional<User> login(String username, String password) {
        if (username == null || password == null){
            throw new IllegalArgumentException("username or password is null");
        }
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }

   /* public Map<Integer, User> getAllConvertedById() {
        return users.stream()
                .collect(toMap(User::getId, identity()));
    }*/
}
