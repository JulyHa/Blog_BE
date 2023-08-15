package com.example.back_end.service;

import com.example.back_end.model.Role;

public interface IRoleService extends ICOREService<Role, Long> {
    Role findRoleByName (String name);
}
