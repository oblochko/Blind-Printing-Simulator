package com.training.apparatus.data.repo;

import com.training.apparatus.data.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("select avg(r.speed) from Result r where r.user.id = :id")
    Optional<Double> avgSpeed(@Param("id") long id);

    @Query("select avg(r.mistakes) from Result r where r.user.id = :id")
    Optional<Double> avgMistakes(@Param("id") long id);

    @Query("select count (r.user) from Result r where r.user.id = :id")
    int countResult(@Param("id") Long id);

    @Query(value = "select r.* " +
            "from results r " +
            "inner join tasks t " +
            "on r.task_id = t.id " +
            "inner join users u " +
            "on r.user_id = u.id " +
            "where t.type = :type and t.number = :number and u.email = :email ", nativeQuery = true)
    Optional<Result> findResultByTaskAndUser(@Param("type") String type, @Param("number") Long number, @Param("email") String email);
}
