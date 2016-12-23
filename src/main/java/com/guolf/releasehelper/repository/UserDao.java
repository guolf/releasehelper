/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.guolf.releasehelper.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.guolf.releasehelper.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
    User findByLoginName(String loginName);
}
