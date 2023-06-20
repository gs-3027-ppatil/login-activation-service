package com.oneabc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oneabc.model.PinMgt;

@Repository
public interface PinMgtRepository extends JpaRepository<PinMgt, Long> {

	Optional<PinMgt> findByCurrentMpin(String currentMpin);

}
