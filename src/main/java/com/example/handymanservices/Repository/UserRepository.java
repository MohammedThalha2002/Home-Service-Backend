package com.example.handymanservices.Repository;

import com.example.handymanservices.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findUsersByProfession(String profession);
}
