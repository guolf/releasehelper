package com.guolf.releasehelper.web.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.guolf.releasehelper.entity.App;
import com.guolf.releasehelper.entity.Version;
import com.guolf.releasehelper.service.ServiceException;
import com.guolf.releasehelper.service.app.AppService;
import com.guolf.releasehelper.service.app.VersionService;

/**
 * Created by guolf on 16/7/16.
 */
@Controller
@RequestMapping(value = "/download")
public class DownloadController {
    @Autowired
    private AppService appService;

    @Autowired
    private VersionService versionService;

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public String index(@PathVariable("uuid") String uuid, Model model,Device device) {
        App app = appService.findByUuid(uuid);
        if (app == null) {
            throw new ServiceException("应用不存在");
        }
        Version version = versionService.findByAppId(app.getId());
        model.addAttribute("app", app);
        model.addAttribute("version", version);

        if(device.isMobile()){
            return "download/mobile";
        }else{
            return "download/index";
        }
    }
}
