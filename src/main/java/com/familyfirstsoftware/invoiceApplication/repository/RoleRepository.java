package com.familyfirstsoftware.invoiceApplication.repository;



import com.familyfirstsoftware.invoiceApplication.domain.Role;

import java.util.Collection;

public interface RoleRepository<T extends Role> {
    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(); // could do list(int limit) if we want to limit the number of results
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
    void addRoleToUser(Long userId, String roleName);
    Role getRoleByUserId(Long userId);
    Role getRoleByUserEmail(String email);
    void updateUserRole(Long userId, String roleName);
}
