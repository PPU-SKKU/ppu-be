package com.ppu.ppu.user;


import com.ppu.ppu.user.dto.UserCreateDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByEmailAndLoginType(String email, LoginType loginType) {
        return userRepository.findByEmailAndLoginType(email, loginType);
    }
    public Optional<User> getUserByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

    @Transactional
    public void createUser(UserCreateDto user){
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setName(user.getName());
        newUser.setNickname(user.getNickname());
        newUser.setBirth(user.getBirth());
        newUser.setGender(user.getGender());
        newUser.setLoginType(user.getLoginType());
        System.out.println("newUser = " + newUser.toString());
        userRepository.save(newUser);
    }

    public Optional<User> findUserById(String id){
        return userRepository.findById(id);
    }
}
