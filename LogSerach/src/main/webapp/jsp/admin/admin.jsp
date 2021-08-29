<%@page import="com.log.analyzer.commons.Constants"%>
<%@page import="com.log.server.util.Utilities"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html ng-app="admin-app">
<head>
<title>Pluto <%=Constants.VERSION %> - administration</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../../res/js/angular/1.4.3/angular.min.js"></script>
<script src="../../res/js/angular/1.4.3/angular-animate.js"></script>
<script src="../../res/js/jquery/jquery-1.11.3.min.js"></script>
<script src="../../res/js/angular/1.2.0rc1/angular-route.min.js"></script>
<script src="../../res/js/angular/ng-dialog/0.4.0/ngDialog.min.js"></script>
<script src="../../res/js/angular/bootstrap/ui-bootstrap-tpls-0.13.3.js"></script>
<script src="../../res/js/registration-main.js"></script>
<link rel="stylesheet" type="text/css" href="../../res/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../../res/css/ng-dialog/0.4.0/ngDialog.min.css">
<link rel="stylesheet" type="text/css" href="../../res/css/ng-dialog/0.4.0/ngDialog-theme-default.min.css">
<link rel="stylesheet" type="text/css" href="../../res/css/admin-main.css">
</head>
<body style="background-color: #999;">
	<div id="menu" ng-controller="AdminController">
		<label for="main-nav-check" class="toggle" onclick="" title="Close">&times;</label>
		<ul>
			<li><a href="">Welcome <sec:authentication property="principal.salutation" /></a></li>
			<li><a href="${pageContext.request.contextPath}">Search</a></li>
			<sec:authorize access="hasAuthority('ADMIN') or hasAuthority('GROUP_ADMIN')">
			<li><a href="#">User Administration</a> <label for="fast-apps"
				class="toggle-sub" onclick="">&#9658;</label> <input type="checkbox"
				id="fast-apps" class="sub-nav-check" />
				<ul id="fast-apps-sub" class="sub-nav">
					<li class="sub-heading">User Administration <label
						for="fast-apps" class="toggle" onclick="" title="Back">&#9658;</label></li>
					<li><a href="#createUser">Create User</a></li>
					<li><a href="#editUser">Edit User</a></li>
				</ul></li>
			</sec:authorize>
			<sec:authorize access="hasAuthority('ADMIN') or hasAuthority('GROUP_ADMIN')">
			<li><a href="#">Group Administration</a> <label for="fast-apps"
				class="toggle-sub" onclick="">&#9658;</label> <input type="checkbox"
				id="fast-apps" class="sub-nav-check" />
				<ul id="fast-apps-sub" class="sub-nav">
					<li class="sub-heading">Group Administration <label
						for="fast-apps" class="toggle" onclick="" title="Back">&#9658;</label></li>
					<li><a href="#createGroup">Create Group</a></li>
					<li><a href="#editGroup">Edit Group</a></li>
				</ul></li>
				</sec:authorize>
			<li><a href="#nodeAdmin">Node Administration</a></li>
			<li><a href="#/">Help</a></li>
			<li><a href="../../j_spring_security_logout">Logout</a></li>
		</ul>
		<span>version: <%=Utilities.getVerion()%></span>
	</div>

	<div style="position: relative; margin: 0px 0px 0px 220px;">
		<div style="position: inherit;" ng-view=""></div>
	</div>

</body>
</html>
