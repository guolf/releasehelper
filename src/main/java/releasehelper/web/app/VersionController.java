package releasehelper.web.app;

import com.google.common.collect.Maps;
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
import releasehelper.entity.App;
import releasehelper.entity.JsonResult;
import releasehelper.entity.Version;
import releasehelper.service.app.AppService;
import releasehelper.service.app.VersionService;
import releasehelper.utils.CommonUtils;
import releasehelper.utils.MessageDigestUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by guolf on 16/7/13.
 */
@Controller
@RequestMapping(value = "/app/version")
public class VersionController {

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

    @RequiresRoles("admin")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateVersion(@Valid Version version, RedirectAttributes redirectAttributes) throws IOException {
        Version version1 = versionService.getById(version.getId());
        version1.setIntro(version.getIntro());
        version1.setFlag(true);
        versionService.save(version1);
        redirectAttributes.addFlashAttribute("message", "更新成功");
        return "redirect:/app/version/" + version1.getApp().getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getVersionList(@PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
                                 @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
                                 ServletRequest request) {

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        Page<Version> versions = versionService.getAppVersion(id, searchParams, pageNumber, pageSize, sortType);
        model.addAttribute("appId", id);
        model.addAttribute("versions", versions);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortTypes", sortTypes);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
        return "app/version/versionList";
    }

    /**
     * 更新版本时上传APK
     *
     * @param request
     * @param appId
     * @return
     * @throws IOException
     */
    @RequiresRoles("admin")
    @RequestMapping(value = "/version/uploadVersion", method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResult<Version> uploadVersion(HttpServletRequest request, @RequestParam(value = "appId", defaultValue = "-1") Long appId) throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        JsonResult<Version> jsonResult = new JsonResult<Version>();
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator iter = multiRequest.getFileNames();
            String filePath = "";
            String abPath = "";

            while (iter.hasNext()) {
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
            if (!StringUtils.isEmpty(filePath) && appId != -1) {
                try {
                    File file = new File(filePath);
                    ApkParser apkParser = new ApkParser(file);
                    ApkMeta apkMeta = apkParser.getApkMeta();
                    String icon = UUID.randomUUID() + ".png";
                    CommonUtils.writeFileFromByte(apkParser.getIconFile().getData(), request.getRealPath("/") + File.separator + "static/upload/", icon);

                    App app = appService.getOne(appId);
                    app.setName(apkMeta.getName());
                    app.setIcon(File.separator + "static/upload/" + icon);
                    appService.saveApp(app);

                    Version version = new Version();
                    version.setApp(app);
                    version.setCreateDate(new Date());
                    version.setDownUrl(abPath);
                    version.setIcon(File.separator + "static/upload/" + icon);
                    version.setVersionName(apkMeta.getVersionName());
                    version.setVersionCode(apkMeta.getVersionCode());
                    version.setMd5(MessageDigestUtils.getMd5FromFile(file));
                    version.setSha1(MessageDigestUtils.getSha1FromFile(file));
                    version.setFileSize(CommonUtils.humanReadableByteCount(file.length(), true));
                    version.setFlag(true);
                    Version version1 = versionService.save(version);
                    jsonResult.setCode(0);
                    jsonResult.setData(version1);
                    jsonResult.setMsg("上传成功");
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    jsonResult.setCode(-1);
                    jsonResult.setMsg("系统异常，上传失败");
                }
            }
        }
        return jsonResult;
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteVersion(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Long appid = versionService.getById(id).getApp().getId();
        versionService.delete(id);
        redirectAttributes.addFlashAttribute("message", "删除版本成功");
        return "redirect:/app/version/" + appid;
    }

}
