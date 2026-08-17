package com.interview.backend.service;

import com.interview.backend.dto.RoleResponseDTO;
import com.interview.backend.entity.Role;
import com.interview.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> new RoleResponseDTO(role.getId(), role.getName(), role.getDescription()))
                .collect(Collectors.toList());
    }
}