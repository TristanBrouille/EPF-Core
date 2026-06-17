package com.epfcore.epfcore.reservation.repository;
import org.springframework.stereotype.Repository;
import com.epfcore.epfcore.reservation.entity.reservation;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface  reservationRepository extends JpaRepository<reservation, Long> {

}