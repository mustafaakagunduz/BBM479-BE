package com.sms.hrsam.service;

import com.sms.hrsam.entity.Role;
import com.sms.hrsam.entity.UserRole;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }
    // RoleService sınıfında
    public Role getRoleByName(UserRole name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }


}
