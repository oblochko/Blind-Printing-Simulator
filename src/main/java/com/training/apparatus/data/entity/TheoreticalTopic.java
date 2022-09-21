package com.training.apparatus.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TheoreticalTopics")
@Getter
@Setter
@NoArgsConstructor
public class TheoreticalTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameTopic;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TheoreticalText> texts = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheoreticalTopic that = (TheoreticalTopic) o;
        return Objects.equals(nameTopic, that.nameTopic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTopic);
    }
}
