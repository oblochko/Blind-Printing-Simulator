package com.training.apparatus.data.repo;

import com.training.apparatus.data.entity.TheoreticalTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheoreticalTopicRepository extends JpaRepository<TheoreticalTopic, Long> {

    TheoreticalTopic findByNameTopic(String nameTopic);
}
