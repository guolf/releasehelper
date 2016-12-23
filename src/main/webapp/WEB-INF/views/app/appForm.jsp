<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>应用管理</title>
    <link rel="stylesheet" href="${ctx}/static/file-upload/css/jquery.fileupload.css">
    <script type="text/javascript" src="${ctx}/static/file-upload/js/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="${ctx}/static/file-upload/js/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="${ctx}/static/file-upload/js/jquery.fileupload.js"></script>
    <script type="text/javascript" src="${ctx}/static/file-upload/js/jquery.fileupload-process.js"></script>
</head>

<body>

<span class="btn btn-success fileinput-button">
        <i class="glyphicon glyphicon-plus"></i>
        <span>添加文件</span>
        <!-- The file input field used as target for the file upload widget -->
        <input id="fileupload" type="file" name="files[]" multiple>
    </span>
<br>
<!-- The container for the uploaded files -->
<div id="files" class="files"></div>
<br/><br/>

<div id="progress" class="progress">
    <div class="progress-bar progress-bar-success"></div>
</div>
<form id="inputForm" action="${ctx}/app/${action}" method="post" enctype="multipart/form-data" class="form-horizontal">
    <fieldset>
        <legend>
            <small>管理应用</small>
        </legend>
        <input type="hidden" name="id" id="id" value="${app.id}"/>

        <div class="control-group">
            <label for="name" class="control-label">应用名称:</label>

            <div class="controls">
                <textarea id="name" name="name" class="input-large">${app.name}</textarea>
            </div>
        </div>
        <div class="control-group">
            <label for="packageName" class="control-label">应用包名:</label>

            <div class="controls">
                <textarea id="packageName" name="packageName" class="input-large">${app.packageName}</textarea>
            </div>
        </div>
        <div class="control-group">
            <label for="version" class="control-label">版本:</label>

            <div class="controls">
                <input type="hidden" id="versionCode" name="versionCode"/>
                <input type="hidden" id="versionName" name="versionName"/>
                <textarea id="version" name="version"
                          class="input-large">${app.versionName + app.versionCode}</textarea>
            </div>
        </div>
        <div class="control-group">
            <label for="app_intro" class="control-label">应用描述:</label>

            <div class="controls">
                <textarea id="app_intro" name="intro" class="input-large">${app.intro}</textarea>
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
            url: 'uploadApp',
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
            if (data.result.code == 0) {
                $("#name").val(data.result.data.name);
                $("#packageName").val(data.result.data.packageName);
                $("#versionCode").val(data.result.data.versionCode);
                $("#versionName").val(data.result.data.versionName);
                $("#version").val(data.result.data.versionName + data.result.data.versionCode);
                $("#id").val(data.result.data.id);
            } else {
                alert(data.result.msg)
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
</script>
</body>
</html>
