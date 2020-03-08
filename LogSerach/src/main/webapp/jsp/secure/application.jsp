<%@page import="com.log.server.util.Utilities"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html ng-app="search-app" style="height: 100%;width: 100%">
<head>
<title>Pluto <%=Utilities.getVerion() %> - administration</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="res/js/jquery/jquery-1.11.3.min.js"></script>
<script src="res/js/jquery/jquery-ui-1.11.4.min.js"></script>
<script src="res/js/angular/1.4.3/angular.min.js"></script>
<script src="res/js/angular/1.4.3/angular-animate.js"></script>
<script src="res/js/angular/1.2.0rc1/angular-route.min.js"></script>
<script src="res/js/angular/ng-dialog/0.4.0/ngDialog.min.js"></script>
<script src="res/js/angular/bootstrap/ui-bootstrap-tpls-0.13.3.js"></script>
<script src="res/js/ng-infinite-scroll-pluto.js"></script>
<script src="res/js/context-menu.js"></script>
<script src="res/js/sch_app.js"></script>
<script src="res/js/angularjs-datetime-picker.min.js"></script>
<script src="res/js/chart-min.js"></script>
<link rel="stylesheet" type="text/css" href="res/css/angularjs-datetime-picker.css">
<link rel="stylesheet" type="text/css" href="res/css/result.css">
<link rel="stylesheet" type="text/css" href="res/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="res/css/ng-dialog/0.4.0/ngDialog.min.css">
<link rel="stylesheet" type="text/css" href="res/css/ng-dialog/0.4.0/ngDialog-theme-default.min.css">
<link rel="stylesheet" type="text/css" href="res/css/main.css">
<link rel="stylesheet" type="text/css" href="res/css/search_header.css">
<link rel="stylesheet" type="text/css" href="res/css/nav_header.css" />
</head>
<body style="height: 100%;width: 100%;">
    <div class="header">
        <div class="menu">
            <ul id="nav">
                <li><a href="#/">Search</a></li>
                <li><a href="#download">Configure Agent</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/admin/admin.jsp">Administration</a></li>
                <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Logout</a></li>
            </ul>
        </div>
        <div style="width:300px;right: 0px;position: fixed;color: white;font-family:sans-serif;font-size: 12px;font-weight: bold;min-height: 32px; max-height: 32px;margin-top: 10px;text-align: right;">
        	<span>Welcome <sec:authentication property="principal.salutation"/></span>
        	<span style="font-size: 7px;">version: <%=Utilities.getVerion() %></span>
        </div>
    </div>
    
    <div style="position: relative; margin: 0px 0px 0px 0px;height: calc(100% - 96px);width: 100%">
		<div style="position: inherit;height: calc(100% -96px);width: 100%" ng-view=""> </div>
	</div>
    
    
   </body>
   </html>