package com.example.back_end.service.impl;

import com.example.back_end.model.Role;
import com.example.back_end.repository.IRoleRepository;
import com.example.back_end.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository iRoleRepository;

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return iRoleRepository.findAll(pageable);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return iRoleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return iRoleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        iRoleRepository.deleteById(id);
    }

    @Override
    public Role findRoleByName(String name) {
        return iRoleRepository.findRoleByName(name);
    }
}
