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

@Component
public class ParserCSV implements Parser {
    private final UserRepository userRepository;

    ParserCSV(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //    Изпълнява се когато Spring създаде всички Bean-ова и тоест базата е ready
    @PostConstruct
    void populate() {
//        за да работи като пакетирам в JAR:
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("users.csv").getInputStream()))){
            String line;
            while((line=reader.readLine()) != null){
                String[] arr = line.split(",");

                if(arr.length != 3){
                    continue;
                }

                User user = new User();

                user.setName(arr[0]);
                user.setAge(Integer.parseInt(arr[1]));
                user.setPrivatePersonalInfo(arr[2]);
                userRepository.save(user);
            }
        }catch(FileNotFoundException  e1){
            throw new UncheckedIOException(e1);
        }catch(IOException e2){
            throw new UncheckedIOException(e2);
        }
    }
}
