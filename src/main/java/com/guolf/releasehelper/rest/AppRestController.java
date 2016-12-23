package com.guolf.releasehelper.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;
import com.guolf.releasehelper.entity.App;
import com.guolf.releasehelper.entity.Version;
import com.guolf.releasehelper.service.app.AppService;
import com.guolf.releasehelper.service.app.VersionService;

/**
 * Created by guolf on 16/7/14.
 */
@RestController
@RequestMapping(value = "/api/v1/app")
public class AppRestController {

    private static Logger logger = LoggerFactory.getLogger(AppRestController.class);

    @Autowired
    private AppService appService;

    @Autowired
    private VersionService versionService;


    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Version get(@PathVariable("uuid") String uuid) {
        App app = appService.findByUuid(uuid);
        if (app == null) {
            String message = "应用不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return versionService.findByAppId(app.getId());
    }

}
