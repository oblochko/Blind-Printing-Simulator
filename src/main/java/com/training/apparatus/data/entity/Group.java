package com.training.apparatus.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Groups")
@Setter
@Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String link;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    public Group() {

    }

    public void addUser(User user) {
        users.add(user);
        user.setGroup(this);
    }
    public void removeUser(User user) {
        users.remove(user);
        user.setGroup(null);
    }
}
