package com.training.apparatus.data.repo;

import com.training.apparatus.data.dto.UserDto;
import com.training.apparatus.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("select u from User u " +
            "inner join Group g " +
            "on u.group.id = g.id " +
            "where g.link = :link")
    List<User> findByGroup(@Param("link") String link);


    @Query("select new com.training.apparatus.data.dto.UserDto( " +
                "u.pseudonym, " +
                "u.email, " +
                "coalesce(avg(r.mistakes), 0), " +
                "coalesce(avg(r.speed), 0) , count(r.id) ) " +
            "from  User u " +
                "left join Group g on u.group.id = g.id " +
                "left join Result r on r.user.id = u.id " +
            "where g.id = :id " +
                "group by u.id")
    List<UserDto> findUserDtoByGroup(@Param("id") Long id);
}
