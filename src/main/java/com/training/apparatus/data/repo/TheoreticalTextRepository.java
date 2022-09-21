package com.training.apparatus.data.repo;

import com.training.apparatus.data.entity.TheoreticalText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TheoreticalTextRepository extends JpaRepository<TheoreticalText, Long> {

    @Query("select count(t.id) " +
            "from TheoreticalText t " +
            "where t.topic.id = :id")
    Integer getCountPageById(Long id);

    @Query("select t.text " +
            "from TheoreticalText t " +
            "where t.topic.id = :topicId and t.numberPage = :numberPage")
    String findTextByTopicIdAndNumberPage(Long topicId, Integer numberPage);

    @Query("select count(t) " +
            "from TheoreticalText t " +
            "where t.topic.id = :topicId and t.numberPage = :numberPage ")
    Integer existsByNumberPageAndTopicId(Long topicId, Integer numberPage);

    @Query("select t " +
            "from TheoreticalText t " +
            "where t.topic.id = :topicId and t.numberPage = :numberPage")
    TheoreticalText findByTopicAndNumberPage(Long topicId, Integer numberPage);

}
