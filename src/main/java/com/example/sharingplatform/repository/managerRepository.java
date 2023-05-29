package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface managerRepository extends JpaRepository<manager,Long> {
    manager findByManagerNameAndManagerPsd(String managerName,String managerPsd);
}
