package sumerge.com.example.springBootProject.authMicroservice.Services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sumerge.com.example.springBootProject.authMicroservice.Models.UserEntity;
import sumerge.com.example.springBootProject.authMicroservice.Repository.UserRepository;

@Service
@Data
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;




    public void registerUser(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("email already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        userEntity.setPassword(encodedPassword);

        userRepository.save(userEntity);
    }
    public boolean authenticateUser(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

}
