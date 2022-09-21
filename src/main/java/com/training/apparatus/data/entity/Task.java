package com.training.apparatus.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    Type type;

    Long number;

    @Size(max =  1000)
    String text;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Result> results = new HashSet<>();

    public Task() {

    }

    public Task(Type type, Long number, String text) {
        this.type = type;
        this.number = number;
        this.text = text;
    }

    public void addResult(Result result){
        this.results.add(result);
        result.setTask(this);
    }
    public void removeTask(Result result){
        this.results.remove(result);
        result.setTask(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && type == task.type && Objects.equals(number, task.number) && Objects.equals(text, task.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, number, text);
    }
}
