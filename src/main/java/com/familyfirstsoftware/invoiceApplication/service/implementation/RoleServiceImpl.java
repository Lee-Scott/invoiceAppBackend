package com.familyfirstsoftware.invoiceApplication.service.implementation;

import com.familyfirstsoftware.invoiceApplication.domain.Role;
import com.familyfirstsoftware.invoiceApplication.repository.RoleRepository;
import com.familyfirstsoftware.invoiceApplication.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository<Role> roleRoleRepository;

    @Override
    public Role getRoleById(Long id) {
        return roleRoleRepository.getRoleByUserId(id);
    }
}
