package kz.springboot.internetshop.services;

import kz.springboot.internetshop.entities.Role;
import kz.springboot.internetshop.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    Users getUserByEmail(String email);
    Users getUser(Long id);
    Users saveUser(Users users);
    Users createUser(Users user);
    List<Users> listOfUsers();
    List<Role> listOfRoles();
    void deleteUser(Users users);
    Role createRole(Role role);
    Role getRole(Long id);
    String encodePassword(String password);
}
