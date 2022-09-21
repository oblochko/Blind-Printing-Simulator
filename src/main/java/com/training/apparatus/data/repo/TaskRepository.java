package com.training.apparatus.data.repo;

import com.training.apparatus.data.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "select count(*) " +
            "from tasks t " +
            "where t.type = :name " +
            "group by t.type", nativeQuery = true)
    Optional<Long> getSizeByType(@Param("name") String name);

    @Query(value = "select * " +
            "from tasks t " +
            "where t.type = :name " +
            "and t.number = :number", nativeQuery = true)
    Optional<Task> findByNumberAndType(@Param("name") String name, @Param("number") Long number);

}
