package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("Юзер с таким email уже есть");
        }
        return userRepository.save(user);
    }

    public List<User> userList() {
        return userRepository.findAll();

    }

    public User findByName(String firstName) {
        Optional<User> optionalUser = userRepository.findByName(firstName);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User не найден");
        }
        return optionalUser.get();
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.getByLogin(login);
    }

    public User findByEmail(String user) {
        Optional<User> optionalUser = userRepository.findByEmail(user);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Email не найден");
        }
        return optionalUser.get();
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
                throw new IllegalStateException("Юзер с таким email уже существует");
            }
            user.setEmail(email);
        }

        if (name != null && !name.equals(user.getFirstName())) {
            user.setFirstName(name);
        }
    }

    public void delete(Long id) {
        Optional<User> userOption = userRepository.findById(id);

        if (userOption.isEmpty()) {
            throw new IllegalStateException("Юзера с " + "id ->" + id + " нет");
        }

        userRepository.deleteById(id);
    }

}
