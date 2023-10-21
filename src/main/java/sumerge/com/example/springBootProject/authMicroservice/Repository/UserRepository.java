package sumerge.com.example.springBootProject.authMicroservice.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sumerge.com.example.springBootProject.authMicroservice.Models.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity,String> {
    UserEntity findByEmail(String email);
}
