package com.example.Blog_App.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();

        // equivalent to ResponseEntity.status(HttpStatus.OK).body(users);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/find")
    public ResponseEntity<UserDto> getUserByEmail(@RequestBody String email) {
        UserDto user = userService.getUserByEmail(email);

        return ResponseEntity.ok(user);
    }
}