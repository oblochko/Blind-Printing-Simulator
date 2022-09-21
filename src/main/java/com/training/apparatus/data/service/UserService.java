package com.training.apparatus.data.service;

import com.training.apparatus.data.dto.UserDto;
import com.training.apparatus.data.entity.Group;
import com.training.apparatus.data.entity.User;
import com.training.apparatus.data.repo.UserRepository;
import com.training.apparatus.secutiy.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityService securityService;

    @Autowired
    EntityManager em;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        if(!userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails auth = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name()).build();
        return auth;
    }

    @Transactional
    public void resetResult(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if(user.isPresent()) {
            user.get().getResults().clear();
            userRepository.save(user.get());
        }

    }

    @Transactional
    public List<String> getMapResultInTask(User auth) {
        User user = userRepository.findByEmail(auth.getEmail());
        return user.getResults().stream()
                .map(r -> r.getTask().getType() + " " + r.getTask().getNumber()).collect(Collectors.toList());
    }

    public Group getGroup(String email) {
        return userRepository.findByEmail(email).getGroup();
    }


    public List<User> findUsersByGroup() {
        User user = securityService.getAuthUser();
        return updateUsers(user.getGroup().getLink());
    }

    public List<UserDto> findUsersDtoByGroup() {
        User user = securityService.getAuthUser();
        return userRepository.findUserDtoByGroup(user.getGroup().getId());
    }

    public List<User> updateUsers(String code) {
        return userRepository.findByGroup(code);
    }

}
