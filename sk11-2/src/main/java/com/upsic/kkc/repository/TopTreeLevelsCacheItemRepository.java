package com.upsic.kkc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.upsic.kkc.models.TopTreeLevelsCacheItem;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface TopTreeLevelsCacheItemRepository extends JpaRepository<TopTreeLevelsCacheItem, Long> {

    List<TopTreeLevelsCacheItem> findAllByParentAndModelOrderById(UUID parentUid, UUID modelUid);

    @Modifying
    @Query("delete from TopTreeLevelsCacheItem")
    void removeAll();

}
