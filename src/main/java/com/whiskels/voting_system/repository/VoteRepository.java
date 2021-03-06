package com.whiskels.voting_system.repository;

import com.whiskels.voting_system.model.User;
import com.whiskels.voting_system.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.localDate DESC")
    List<Vote> getAll(@Param("userId") int userId);

    Vote getByUserAndLocalDate(User user, LocalDate localDate);

    Vote getByUserIdAndLocalDate(int userId, LocalDate localDate);
}
