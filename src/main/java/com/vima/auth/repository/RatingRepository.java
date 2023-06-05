package com.vima.auth.repository;

import com.vima.auth.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = "SELECT count(r.id) FROM rating r WHERE r.host_id = ?1", nativeQuery = true)
    int findNumberOfHostRatings(Long hostId);

    @Query(value = "SELECT sum(a.value)/count(a.id) FROM rating a WHERE a.host_id = ?1",nativeQuery = true)
    double findAvg(Long hostId);
}
