package com.garden.service;

import com.garden.authentication.PasswordEncryptor;
import com.garden.authentication.SessionContext;
import com.garden.enums.Role;
import com.garden.model.SeasonDay;
import com.garden.model.User;
import com.garden.repository.UserRepository;
import com.garden.seed.SpeciesSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpeciesSeeder speciesSeeder;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void changePassword(String newPassword) {
        String encodedPassword = PasswordEncryptor.encrypt(newPassword);
        SessionContext.getUser().setPassword(encodedPassword);
        userRepository.save(SessionContext.getUser());
        System.out.println("Your password has been changed!");
    }

    public void deleteUser(String username) {
        Optional<User> toDelete = userRepository.findByUsername(username);
        toDelete.ifPresent(user -> {
            userRepository.delete(user);
            System.out.println("User " + username + " has been deleted!");
        });
    }

    public void register(String fullName, String username, String password, Role role, int hardinessZone, SeasonDay lastSpringFrost,
                         SeasonDay firstFallFrost, int wateringLeadDays, int frostWarningDays, int plantingLeadDays) {
        User user = new User(fullName, username, PasswordEncryptor.encrypt(password), role, hardinessZone,  lastSpringFrost, firstFallFrost, wateringLeadDays, frostWarningDays, plantingLeadDays);
        userRepository.save(user);
        speciesSeeder.seed(user);
        SessionContext.login(user);
    }
}
