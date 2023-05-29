package com.example.sharingplatform.repository;

import com.example.sharingplatform.entity.administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface administratorRepository extends JpaRepository<administrator,Long> {
    administrator findByAdministratorNameAndAdministratorPsd(String administratorName,String administratorPsd);
}
