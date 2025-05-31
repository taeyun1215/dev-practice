package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void registerUser(User user) {
        userRepository.save(user);
        redisTemplate.opsForValue().set("USER_" + user.getUsername(), user);
    }

    public boolean validatePassword(String username, String password) {
        Optional<User> userOptional = findUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    public Optional<User> findUserByUsername(String username) {
        User cachedUser = (User) redisTemplate.opsForValue().get("USER_" + username);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        } else {
            Optional<User> userOptional = userRepository.findByUsername(username);
            userOptional.ifPresent(user -> redisTemplate.opsForValue().set("USER_" + username, user));
            return userOptional;
        }
    }
}