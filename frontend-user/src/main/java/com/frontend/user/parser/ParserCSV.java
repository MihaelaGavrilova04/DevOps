package com.frontend.user.parser;

import com.frontend.user.model.User;
import com.frontend.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

// used for local development with h2 db

@Component
public class ParserCSV implements Parser {
    private final UserRepository userRepository;
    private static final int EXPECTED_LENGTH = 3;

    ParserCSV(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //    Изпълнява се когато Spring създаде всички Bean-ова и тоест базата е ready
    @PostConstruct
    void populate() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("users.csv").getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");

                if (arr.length != EXPECTED_LENGTH) {
                    continue;
                }

                User user = new User();

                user.setName(arr[0]);
                user.setAge(Integer.parseInt(arr[1]));
                user.setPrivatePersonalInfo(arr[2]);
                userRepository.save(user);
            }

        } catch (FileNotFoundException e1) {
            throw new UncheckedIOException(e1);
        } catch (IOException e2) {
            throw new UncheckedIOException(e2);
        }
    }
}
