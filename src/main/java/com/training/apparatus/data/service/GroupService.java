package com.training.apparatus.data.service;

import com.training.apparatus.data.entity.Group;
import com.training.apparatus.data.entity.Role;
import com.training.apparatus.data.entity.User;
import com.training.apparatus.data.repo.GroupRepository;
import com.training.apparatus.data.repo.UserRepository;
import com.training.apparatus.secutiy.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addUser(String email, String link) {
        Optional<Group> group = groupRepository.findByLink(link);
        if(group.isPresent()) {
            User user = userRepository.findByEmail(email);
            group.get().addUser(user);
            groupRepository.save(group.get());
        }
    }

    public void removeUser(User user) {
        Optional<Group> group = groupRepository.findById(user.getGroup().getId());
        if(group.isPresent()) {
            group.get().removeUser(user);
            groupRepository.save(group.get());
        }
    }

    @Transactional
    public void save(Group group) {
        UserDetails auth = securityService.getAuthenticatedUser();
        User user = userRepository.findByEmail(auth.getUsername());
        user.setRole(Role.ROLE_BOSS);
        group.addUser(user);
        groupRepository.save(group);
    }

    @Transactional
    public String generateCode(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if(user.isPresent()) {
            double max = 800000;
            double min = 200000;
            int result = (int)((Math.random() * ((max - min) + 1)) + min);
            Group group = user.get().getGroup();
            group.setLink(String.valueOf(result));
            groupRepository.save(group);
            return String.valueOf(result);
        }
        return "none";
    }
}
