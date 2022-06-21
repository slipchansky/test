package com.upsic.kkc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.upsic.kkc.models.TopTreeLevelsCache;

@Transactional
@Repository
public interface TopTreeLevelsCacheRepository extends JpaRepository<TopTreeLevelsCache, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "lock table top_tree_levels_cache in access exclusive mode")
    void lockTable();

    TopTreeLevelsCache findFirstBy();

}


