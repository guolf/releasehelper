<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>应用管理</title>
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
        <th>key</th>
        <th>icon</th>
        <th>应用</th>
        <th>包名</th>
        <th>简介</th>
        <shiro:hasRole name="admin">
            <th>管理</th>
        </shiro:hasRole>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${apps.content}" var="app">
        <tr>
            <td>${app.uuid}</td>
            <td><img width="50px" height="50px" src="${ctx}${app.icon}" alt="icon"/></td>
            <td><a href="${ctx}/app/update/${app.id}">${app.name}</a></td>
            <td>${app.packageName}</td>
            <td>${app.intro}</td>


            <td>
                <shiro:hasRole name="admin">
                    <a href="${ctx}/app/delete/${app.id}">删除</a>&nbsp;
                    <a href="${ctx}/app/update/${app.id}">编辑</a>&nbsp;
                </shiro:hasRole>
                <a href="${ctx}/download/${app.uuid}">下载</a>&nbsp;
                <a href="${ctx}/app/version/${app.id}">版本</a>&nbsp;
            </td>

        </tr>
    </c:forEach>
    </tbody>
</table>

<tags:pagination page="${apps}" paginationSize="10"/>
<shiro:hasRole name="admin">
    <div>
        <a class="btn" href="${ctx}/app/create">创建应用</a>
    </div>
</shiro:hasRole>
</body>
</html>
