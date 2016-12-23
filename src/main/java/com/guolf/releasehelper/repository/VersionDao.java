package com.guolf.releasehelper.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.guolf.releasehelper.entity.Version;

import java.util.List;

/**
 * Created by guolf on 16/7/13.
 */
public interface VersionDao extends PagingAndSortingRepository<Version, Long>, JpaSpecificationExecutor<Version> {

    @Query(value = "select v from Version v where v.app.id = :id and v.versionCode = (select max(e.versionCode) from Version e where e.app.id = :id ) ")
    List<Version> findByAppId(@Param("id") Long id);
}
