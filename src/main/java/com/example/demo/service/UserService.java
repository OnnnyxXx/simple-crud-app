package com.example.demo.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> userList() {
        return userRepository.findAll();

    }

    public User findByName(String user) {
        List<User> optionalUser = userRepository.findByName(user);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not find");
        }
        return optionalUser.get(0);
    }

    public User findByEmail(String user) {
        Optional<User> optionalUser = userRepository.findByEmail(user);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Email not find");
        }
        return optionalUser.get();
    }


    public User create(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("Юзер с таким email уже есть");
        }
        user.setAge(Period.between(user.getBirth(), LocalDate.now()).getYears());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        Optional<User> userOption = userRepository.findById(id);

        if (userOption.isEmpty()) {
            throw new IllegalStateException("Юзера с " + "id->" + id + " нет");
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, String email, String name) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Юзера с id: " + id + " не существует");
        }
        User user = optionalUser.get();

        if (email != null && !email.equals(user.getEmail())) {
            Optional<User> foundByEmail = userRepository.findByEmail(email);
            if (foundByEmail.isPresent()) {
                throw new IllegalStateException("Юзер с таким имейлом уже существует");
            }
            user.setEmail(email);
        }

        if (name != null && !name.equals(user.getName())) {
            user.setName(name);
        }
    }

}
