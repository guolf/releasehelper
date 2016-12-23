<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
    <link href="${ctx}/static/styles/mobile.css" type="text/css" rel="stylesheet"/>
</head>
<body>
<!--立即下载-->
<div class="mod-main-container">
    <section class="mod-card-container">
        <div class="card-header-con">
            <div class="icon-app">
                <img src="${ctx}${version.icon}" alt="icon"/>
            </div>
            <div class="app-info">
                <h3>卫监助手</h3>

                <div class="app-info-txt">
                    <ul class="mod-header-content clear">
                        <li class="clear"><span>版本：${version.versionName}.${version.versionCode}</span></li>
                        <li class="clear"><span>包大小：${version.fileSize}</span><span>更新时间：${version.createDate}</span></li>
                        <li class="last-li">
                            <span>平台：Android</span>
                            <i id="qq-safe">
                                <div id="qq-safe-text" class="down-tips none">
                                    <p>此应用已通过<a href="javascript:void(0)">腾讯手机管家</a>安全检测，请放心下载。</p>
                                </div>
                            </i> 安全认证
                        </li>
                    </ul>
                    <!--若检测为危险安装包则隐藏下载链接-->
                    <div id="exp-detail-download-item" class="mod-btns ">
                        <a href="${ctx}${version.downUrl}" class="btn-blue icon-android">立即下载</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <div id="exp-detail-desNimgs" class="mod-container shadow ">
        <section class="mod-card-container">
            <div class="card-content">
                <!--版本描述-->
                <div id="exp-detail-description">
                    <div class="mod-items">
                        <h3 class="mod-title">版本描述</h3>
                        <div class="txt-info">
                            ${version.intro}
                        </div>
                    </div>
                </div>
                <!--应用截图-->
                <div id="exp-detail-imgs">

                </div>
            </div>
        </section>
    </div>
</div>
</body>
</html>
