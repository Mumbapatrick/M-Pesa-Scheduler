package com.fintech.mpesascheduler.controller

import com.fintech.mpesascheduler.entity.UserAccount
import com.fintech.mpesascheduler.entity.UserRole
import com.fintech.mpesascheduler.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    // REGISTER NEW USER
    @PostMapping("/register")
    fun registerUser(@RequestBody user: UserAccount): ResponseEntity<Any> {
        // Check if email already exists
        val existingUser = userService.findByEmail(user.email)
        if (existingUser != null) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("User with email ${user.email} already exists.")
        }

        val savedUser = userService.registerUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
    }

    // LOGIN USER
    @PostMapping("/login")
    fun loginUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = userService.findByEmail(loginRequest.email)

        return when {
            user == null -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with email ${loginRequest.email} not found.")

            userService.checkPassword(loginRequest.password, user.passwordHash) -> {
                // Return login success with role
                val response = LoginResponse(user.email, user.role.name)
                ResponseEntity.ok(response)
            }

            else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
        }
    }

    // GET ALL USERS (ADMIN ONLY)
    @GetMapping("/")
    fun getAllUsers(@RequestParam email: String): ResponseEntity<Any> {
        val user = userService.findByEmail(email)
        return if (user != null && user.role == UserRole.ADMIN) {
            ResponseEntity.ok(userService.getAllUsers())
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied")
        }
    }
}

// DTOs
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val email: String, val role: String)
