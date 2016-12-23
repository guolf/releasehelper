package com.guolf.releasehelper.web.app;

import com.google.common.collect.Maps;
import com.guolf.releasehelper.service.app.VersionService;
import com.guolf.releasehelper.utils.MessageDigestUtils;
import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;
import com.guolf.releasehelper.entity.App;
import com.guolf.releasehelper.entity.JsonResult;
import com.guolf.releasehelper.entity.Version;
import com.guolf.releasehelper.service.app.AppService;
import com.guolf.releasehelper.utils.CommonUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by guolf on 16/7/13.
 */
@Controller
@RequestMapping(value = "/app")
public class AppController {

    private static final String PAGE_SIZE = "3";

    private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

    static {
        sortTypes.put("auto", "自动");
        sortTypes.put("title", "标题");
    }

    @Autowired
    private AppService appService;

    @Autowired
    private VersionService versionService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
                       @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
                       ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        Page<App> tasks = appService.getPageApp(searchParams, pageNumber, pageSize, sortType);

        model.addAttribute("apps", tasks);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortTypes", sortTypes);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return "app/appList";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("app", appService.getOne(id));
        model.addAttribute("action", "update");
        return "app/appForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid App app, RedirectAttributes redirectAttributes) {
        App app1 = appService.getOne(app.getId());
        app1.setIntro(app.getIntro());
        app1.setName(app.getName());
        appService.saveApp(app1);
        redirectAttributes.addFlashAttribute("message", "更新成功");
        return "redirect:/app/";
    }

    @RequiresRoles("admin")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("app", new App());
        model.addAttribute("action", "create");
        return "app/appForm";
    }

    @RequiresRoles("admin")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid App newApp, RedirectAttributes redirectAttributes) throws IOException {
        if (newApp.getId() == null) {
            appService.saveApp(newApp);
        } else {
            App app = appService.getOne(newApp.getId());
            app.setIntro(newApp.getIntro());
            appService.saveApp(app);
        }
        redirectAttributes.addFlashAttribute("message", "创建应用成功");
        return "redirect:/app/";
    }

    /**
     * 创建应用时上传APK
     *
     * @param request
     * @return
     * @throws IOException
     */

    @RequiresRoles("admin")
    @RequestMapping(value = "uploadApp", method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResult<App> uploadApp(HttpServletRequest request) throws IOException {
        JsonResult<App> jsonResult = new JsonResult<App>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();
            String filePath = "";
            String abPath = "";

            while (iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    String fileName = file.getOriginalFilename();
                    if (!fileName.substring(fileName.lastIndexOf(".")).equals(".apk")) {
                        jsonResult.setCode(-1);
                        jsonResult.setMsg("请上传apk文件");
                        return jsonResult;
                    }
                    abPath = File.separator + "static/upload/" + UUID.randomUUID() + file.getOriginalFilename();
                    String path = request.getRealPath("/") + abPath;
                    System.out.println("path:" + path);
                    file.transferTo(new File(path));
                    filePath = path;
                }
            }
            if (!StringUtils.isEmpty(filePath)) {
                try {
                    File file = new File(filePath);
                    ApkParser apkParser = new ApkParser(file);
                    String icon = UUID.randomUUID() + ".png";
                    CommonUtils.writeFileFromByte(apkParser.getIconFile().getData(), request.getRealPath("/") + File.separator + "static/upload/", icon);
                    ApkMeta apkMeta = apkParser.getApkMeta();

                    // 保存APP信息
                    App app = new App();
                    app.setPackageName(apkMeta.getPackageName());
                    app.setVersionCode(apkMeta.getVersionCode());
                    app.setVersionName(apkMeta.getVersionName());
                    app.setName(apkMeta.getLabel());
                    app.setIcon(File.separator + "static/upload/" + icon);
                    app.setCreateDate(new Date());
                    App saveApp = appService.saveApp(app);

                    // 保存Version信息
                    Version v1 = new Version();
                    v1.setCreateDate(new Date());
                    v1.setApp(saveApp);
                    v1.setVersionCode(saveApp.getVersionCode());
                    v1.setVersionName(saveApp.getVersionName());
                    v1.setIcon(File.separator + "static/upload/" + icon);
                    v1.setDownUrl(abPath);
                    v1.setMd5(MessageDigestUtils.getMd5FromFile(file));
                    v1.setSha1(MessageDigestUtils.getSha1FromFile(file));
                    v1.setFileSize(CommonUtils.humanReadableByteCount(file.length(), true));
                    versionService.save(v1);
                    jsonResult.setCode(0);
                    jsonResult.setMsg("上传成功");
                    jsonResult.setData(app);
                } catch (Exception ex) {
                    jsonResult.setCode(-1);
                    jsonResult.setMsg("系统异常，上传失败");
                }
            }
        }
        return jsonResult;
    }

    @ModelAttribute
    public void getApp(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("task", appService.getOne(id));
        }
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        appService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "删除应用成功");
        return "redirect:/app/";
    }
}
