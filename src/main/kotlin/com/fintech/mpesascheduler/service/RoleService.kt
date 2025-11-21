package com.fintech.mpesascheduler.service

import com.fintech.mpesascheduler.entity.Role
import com.fintech.mpesascheduler.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val repository: RoleRepository
) {
    fun getAllRoles(): List<Role> = repository.findAll()

    fun createRole(role: Role): Role = repository.save(role)
}
