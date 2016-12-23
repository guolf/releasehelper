package com.guolf.releasehelper.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.guolf.releasehelper.entity.App;

/**
 * Created by guolf on 16/7/13.
 */
public interface AppDao extends PagingAndSortingRepository<App, Long>, JpaSpecificationExecutor<App> {

     App findByUuid(String uuid);
}
