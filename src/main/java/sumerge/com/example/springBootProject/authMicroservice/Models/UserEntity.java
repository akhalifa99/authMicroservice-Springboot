package sumerge.com.example.springBootProject.authMicroservice.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Users")
public class UserEntity {
    @Id
    private String id;
    private String email;
    private String password;

}
