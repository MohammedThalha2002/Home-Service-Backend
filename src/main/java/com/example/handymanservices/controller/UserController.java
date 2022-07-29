package com.example.handymanservices.controller;

import com.example.handymanservices.Repository.UserRepository;
import com.example.handymanservices.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private MongoOperations mongoOperations;

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
       return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user){
        return ResponseEntity.status(201).body(this.userRepository.save(user));
    }

    @GetMapping("/users/{profession}")
    public ResponseEntity<List<User>> getProfessionals(@PathVariable String profession){
        return ResponseEntity.ok(userRepository.findUsersByProfession(profession));
    }

    @GetMapping("/users/{profession}/outdistance")
    public List<User> findByDistance(@PathVariable("profession") String profession,
                                     @RequestParam float latitude,
                                     @RequestParam float longitude,
                                     @RequestParam int distance){
        Point basePoint = new Point(longitude, latitude); // searching user's location
        Distance radius = new Distance(distance, Metrics.KILOMETERS); // Making a radius to search - 20 km radius
        Circle area = new Circle(basePoint, radius); // Making a circle

        Query query = new Query();
        query.addCriteria(Criteria.where("geoLocation").withinSphere(area).and("profession").is(profession));
        System.out.println("latitude : " +  latitude + "longitude : " + longitude);
        return mongoOperations.find(query, User.class);
    }

    @GetMapping("/users/{profession}/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("profession") String profession,
                                                      @PathVariable("id") String id){
        return ResponseEntity.ok(userRepository.findById(id));
    }
}

//
