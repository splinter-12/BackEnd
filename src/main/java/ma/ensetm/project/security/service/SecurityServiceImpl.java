package ma.ensetm.project.security.service;

import lombok.AllArgsConstructor;
import ma.ensetm.project.security.entities.AppRole;
import ma.ensetm.project.security.entities.AppUser;
import ma.ensetm.project.security.repositories.AppRoleRepository;
import ma.ensetm.project.security.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private AppUserRepository userRepository;
    private AppRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    @Override
    public AppUser addNewUser(AppUser appUser) {
        appUser.setId(UUID.randomUUID().toString());
        appUser.setPassword( passwordEncoder.encode( appUser.getPassword() ) );
        return userRepository.save( appUser);
    }

    @Override
    public void addNewRole(AppRole appRole) {
        roleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username,String roleName){
        AppUser appUser = userRepository.findByUsername(username);
        AppRole appRole = roleRepository.findByRoleName(roleName);
        appUser.getRoles().add( appRole );
    }


    @Override
    public AppUser loadUserByUsername(String username){
        return userRepository.findByUsername(username);
    }



    @Override
    public AppUser loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AppUser update(AppUser appUser) {
        AppUser user = userRepository.findByUsername(appUser.getUsername());
        user.setPassword( passwordEncoder.encode( appUser.getPassword() ) );
        user.setEmail( appUser.getEmail() );
        return userRepository.save( user );
    }

    @Override
    public List<AppUser> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public List<AppRole> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public AppRole findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
