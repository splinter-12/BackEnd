package ma.ensetm.project.security.service;

import ma.ensetm.project.security.entities.AppRole;
import ma.ensetm.project.security.entities.AppUser;

import java.util.List;

public interface SecurityService {
    AppUser addNewUser(AppUser appUser);
    AppUser loadUserByUsername( String username);
    AppUser loadUserByEmail( String email);
    AppUser update(AppUser appUser);
    List<AppUser> getAllUsers();

    void addNewRole(AppRole appRole);
    void addRoleToUser( String username, String roleName);
    List<AppRole> getAllRoles();
    AppRole findRoleByRoleName(String roleName);

}
