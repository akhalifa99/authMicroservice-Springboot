package sumerge.com.example.springBootProject.authMicroservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Users")
@NoArgsConstructor
public class UserEntity {
    @Id
    private String id;
    private String email;
    private String password;


    public UserEntity(String email, String password) {
        this.email=email;
        this.password=password;
    }
}
