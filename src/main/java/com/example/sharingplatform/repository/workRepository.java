package com.example.sharingplatform.repository;

import org.springframework.stereotype.Repository;
import com.example.sharingplatform.entity.work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface workRepository extends JpaRepository<work,Long> {
    @Transactional(rollbackFor = Exception.class)   //删除发生错误会回滚数据
    void deleteByUserID(long userID);
    work findByWorkID(long workID);
}
