package com.fintech.mpesascheduler.service

import com.fintech.mpesascheduler.entity.UserAccount
import com.fintech.mpesascheduler.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    /**
     * Registers a new user by hashing the password before saving.
     */
    fun registerUser(user: UserAccount): UserAccount {
        val hashedUser = user.copy(passwordHash = passwordEncoder.encode(user.passwordHash))
        return repository.save(hashedUser)
    }

    /**
     * Finds a user by email.
     */
    fun findByEmail(email: String): UserAccount? =
        repository.findByEmail(email)

    /**
     * Checks if the provided raw password matches the hashed password.
     */
    fun checkPassword(rawPassword: String, hashedPassword: String): Boolean =
        passwordEncoder.matches(rawPassword, hashedPassword)

    /**
     * Retrieves all users.
     */
    fun getAllUsers(): List<UserAccount> = repository.findAll()
}
