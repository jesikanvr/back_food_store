package com.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.store.entitys.Report;

@Repository
public interface ReportsRepository extends JpaRepository<Report, Long>{    
}
