<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>版本管理</title>
    <link rel="stylesheet" href="${ctx}/static/file-upload/css/jquery.fileupload.css">
    <script type="text/javascript" src="${ctx}/static/file-upload/js/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="${ctx}/static/file-upload/js/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="${ctx}/static/file-upload/js/jquery.fileupload.js"></script>
    <script type="text/javascript" src="${ctx}/static/file-upload/js/jquery.fileupload-process.js"></script>
</head>

<body>
<c:if test="${not empty message}">
    <div id="message" class="alert alert-success">
        <button data-dismiss="alert" class="close">×</button>
            ${message}</div>
</c:if>
<div class="row">
    <div class="span4 offset7">
        <form class="form-search" action="#">
            <label>名称：</label> <input type="text" name="search_LIKE_title" class="input-medium"
                                      value="${param.search_LIKE_name}">
            <button type="submit" class="btn" id="search_btn">搜索</button>
        </form>
    </div>
    <tags:sort/>
</div>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>icon</th>
        <th>版本</th>
        <th>Build</th>
        <th>文件大小</th>
        <th>发布时间</th>
        <th>更新说明</th>
        <th>管理</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${versions.content}" var="version">
        <tr>
            <td><img width="50px" height="50px" src="${ctx}${version.icon}" alt="icon"/></td>
            <td>${version.versionName}</td>
            <td>${version.versionCode}</td>
            <td>${version.fileSize}</td>
            <td>${version.createDate}</td>
            <td>${version.intro}</td>
            <td>
                <shiro:hasRole name="admin">
                    <a href="${ctx}/app/version/delete/${version.id}">删除</a>&nbsp;
                    <a href="javascript:edit('${version.id}','${version.intro}')">编辑</a>&nbsp;
                </shiro:hasRole>
                <a href="${ctx}${version.downUrl}">下载</a>&nbsp;
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<tags:pagination page="${versions}" paginationSize="10"/>
<input type="hidden" id="versionId" name="appId" value="${appId}"/>

<shiro:hasRole name="admin">

    <span class="btn btn-success fileinput-button">
    <i class="glyphicon glyphicon-plus"></i>
    <span>添加版本</span>
    <!-- The file input field used as target for the file upload widget -->
    <input id="fileupload" type="file" class="required" name="files[]" multiple>
    </span>
    <br/><br/>

    <div id="progress" class="progress">
        <div class="progress-bar progress-bar-success"></div>
    </div>
    <br>
    <!-- The container for the uploaded files -->
    <div id="files" class="files"></div>
    <form id="inputForm" action="${ctx}/app/version/update" method="post" class="form-horizontal">
        <fieldset>
            <input type="hidden" name="id" id="id" value="${version.id}"/>

            <div class="control-group">
                <label for="version" class="control-label">版本:</label>

                <div class="controls">
                    <textarea id="version" name="version"
                              class="input-large required">${version.versionName + version.versionCode}</textarea>
                </div>
            </div>
            <div class="control-group">
                <label for="version_intro" class="control-label">更新说明:</label>

                <div class="controls">
                    <textarea id="version_intro" name="intro" class="input-large required">${version.intro}</textarea>
                </div>
            </div>
            <div class="control-group">
                <label for="version_flag" class="control-label">强制更新:</label>

                <div class="controls radio">
                    <input type="radio" id="version_flag" name="flag" checked>是<br>
                    <input type="radio" id="version_flag" name="flag" >否<br>
                </div>
            </div>
            <div class="form-actions">
                <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
                <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
            </div>
        </fieldset>
    </form>


    <script>
        $(function () {
            $("#inputForm").validate();
            $('#fileupload').fileupload({
                url: 'uploadVersion?appId=${appId}',
                dataType: 'json',
                autoUpload: true,
                acceptFileTypes: /(\.|\/)(apk)$/i,
                maxFileSize: 999000,
                progressall: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#progress .progress-bar').css(
                            'width',
                            progress + '%'
                    );
                }
            }).on('fileuploaddone', function (e, data) {
                if(data.result.code == 0) {
                    $("#id").val(data.result.data.id);
                    $("#version").val(data.result.data.versionName + data.result.data.versionCode);
                }else{
                    alert(data.result.msg);
                }
            }).on('fileuploadfail', function (e, data) {
                $.each(data.files, function (index) {
                    var error = $('<span class="text-danger"/>').text('File upload failed.');
                    $(data.context.children()[index])
                            .append('<br>')
                            .append(error);
                });
            }).prop('disabled', !$.support.fileInput)
                    .parent().addClass($.support.fileInput ? undefined : 'disabled');
        });

        function edit(id, intro) {
            $("#id").val(id);
            $("#version_intro").val(intro);
        }
    </script>

</shiro:hasRole>

</body>
</html>
