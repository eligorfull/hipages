package com.hipages.repository;

import com.hipages.domain.Suburb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuburbRepository extends JpaRepository<Suburb, Long> {
}
