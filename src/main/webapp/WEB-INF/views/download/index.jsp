<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>${app.name}</title>
    <link href="${ctx}/static/styles/download.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/jquery-qrcode/jquery-qrcode-0.14.0.min.js"></script>
</head>
<body class="bgc beta-body">
<%--<div class="mod-beta-header download-detail shadow mb19">--%>
    <%--<a href="${ctx}" class="beta-logo"></a>--%>
<%--</div>--%>
<div class="mod-container shadow mb19">
    <div class="pro-download">
        <div class="app-download">
            <div class="app-icon">
                <img src="${ctx}${version.icon}" alt="icon"/>
            </div>
            <h3>${app.name}</h3>

            <p>版本号：${version.versionName}.${version.versionCode}</p>

            <p>
                包大小：${version.fileSize}<span class="pl19">更新时间：${version.createDate}</span>
            </p>

            <div id="qq-safe" class="last-p">
                平台：Android
                <div class="qq-safe">
                    <div id="qq-safe-text" class="safe-tips shadow2 none">
                        <p>
                            此应用已通过<a href="javascript:void(0)">腾讯手机管家</a>安全检测，请放心下载。
                        </p>
                    </div>
                </div>
                安全认证
            </div>
        </div>
        <!--密码输入-->
        <!--若检测为危险安装包则隐藏下载链接-->
        <div id="exp-detail-download" class="mod-download-btn ">
            <a id="exp-detail-download-btn" href="${ctx}${version.downUrl}" class="btn-icon btn-android-down">立即下载</a>
            或扫描二维码下载
            <div id="exp-detail-qrcode" class="detail-code">
                <p class="code-img">
                </p>

                <div id="qrcode" class="app-qr-code">
                </div>
                <p></p>

                <p class="code-text">不支持微信扫描，请使用第三方浏览器进行扫描
                </p>
            </div>
        </div>
    </div>
</div>
<div id="exp-detail-desNimgs" class="mod-container shadow ">
    <div class="mod-container shadow mb19 ">
        <div class="mod-update-content">
            <div id="exp-detail-description">
                <div class="mod-up-items">
                    <p>版本描述：</p>

                    <div>${version.intro}</div>
                </div>
            </div>
            <div id="exp-detail-imgs">
            </div>
        </div>
    </div>
</div>
<div class="footer">
    <p>Copyright©2016 guolingfa.cn All Rights Reserved</p>

    <p>guolingfa.cn 版权所有</p>
</div>
<script>
    $(function () {
        updateQrCode();
        function updateQrCode() {
            var options = {
                render: 'image',
                text: window.location.href,
                size: 103
            };
            $('#qrcode').empty().qrcode(options);
        }

    });
</script>
</body>
</html>
