package com.training.apparatus.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TheoreticalText")
@Getter
@Setter
@NoArgsConstructor
public class TheoreticalText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3000)
    private String text;

    public Integer numberPage;

    @ManyToOne(fetch = FetchType.LAZY)
    private TheoreticalTopic topic;

    public TheoreticalText(String text, Integer numberPage) {
        this.text = text;
        this.numberPage = numberPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheoreticalText that = (TheoreticalText) o;
        return Objects.equals(text, that.text) && Objects.equals(numberPage, that.numberPage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, numberPage);
    }
}
