package com.training.apparatus.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String pseudonym;

    private String email;

    private double avgMistakes;

    private double avgSpeed;

    private long count;
}
