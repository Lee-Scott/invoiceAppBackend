package com.familyfirstsoftware.invoiceApplication.service;

import com.familyfirstsoftware.invoiceApplication.domain.Role;

import java.util.Collection;

public interface RoleService {
    Role getRoleById(Long id);
    Collection<Role> getRoles();
}
