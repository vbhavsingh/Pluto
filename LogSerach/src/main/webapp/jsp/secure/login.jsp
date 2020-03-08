<%@page import="com.log.server.util.Utilities"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="d" uri="http://www.decorator.tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<!DOCTYPE html>
<html ng-app="security-app">
<head>
<meta charset="ISO-8859-1">
<title>Pluto <%=Utilities.getVerion() %> - Login</title>
<script src="res/js/angular/1.4.3/angular.min.js"></script>
<script src="res/js/angular/1.4.3/angular-animate.js"></script>
<script src="res/js/jquery/jquery-1.11.3.min.js"></script>
<style>
@-webkit-keyframes autofill {
    to {
        background: transparent;
    }
}

input:-webkit-autofill {
    -webkit-animation-name: autofill;
    -webkit-animation-fill-mode: both;
}
#loginform {
	width: 400px;
	height: 160px;
	font-size: 16px;
	position: absolute;
	top: 0px;
	bottom: 160px;
	left: 0;
	right: 0;
	margin: auto;
}

#loginform div input, input:focus {
	outline: none;
	border: none;
	padding: 2px 10px 2px 10px;
	width: 360px;
	margin: 2px 10px 2px 10px;
	height: 40px;
	box-shadow: none;
}

#loginform div span {
	color: #383B38;
	font-size: 18px;
	font-stretch: extra-expanded;
	padding-left: 10px;
	font-size: 18px;
}

#loginform div button {
	margin-left: 32%;
	background-color: #DCDEE8;
	color: #383B38;
	border: none;
	width: 100px;
	height: 30px;
	border-radius: 12px;
	border: 2px solid #DCDEE8;
}

#loginform div button:hover {
	border: 1px solid #141414;
}

._textbox_styler {
	border: 2px solid silver;
	background-color: #ffffff;
	width: 400px;
	border-radius: 15px;
}

._textbox_div_error {
	border: 2px solid #ED8C9C;
	background-color: #ED8C9C;
	width: 400px;
	border-radius: 15px;
}

._textbox_error {
	background-color: #ED8C9C;
	outline: #ED8C9C solid 0px;
	border: none;
}

._textbox_styler:hover {
	border: 2px solid #2389F7;
}
#onBoardMessgae{
	background-color: #666666;
	color:  #d9d9d9;
	position: fixed;
	top: 20px;
	left: 0;
	right: 0;
	margin: auto;
	width: 90%;
	border-radius: 5px;
	border: 1px dashed  #d9d9d9;
	height: 50px;
	text-align: center;
	vertical-align: middle;
	line-height: 50px;
	box-shadow: 5px 5px 5px #888888;
	overflow: hidden;
}
#errorMsg{
	width: 400px;
	line-height: 50px;
	color:  #660000;
	left: 0;
	right: 0;
	margin: auto;
	text-align: center;
	vertical-align: middle;
}
</style>
<script>
	var securityApp = angular.module('security-app', []);
	securityApp.controller('logincontroller', function($scope, $http) {
		var httpRequest = $http.post("open/fobcheck.htm");
		httpRequest.success(function(response) {
			$scope.reveal = response;
		});
	});
</script>

</head>
<body style="background-color: #999;" ng-controller="logincontroller">
	<div id="onBoardMessgae"  ng-show="reveal=='true'" >Welcome! to your new installation of Pluto. You can log in using the default credentials (username: admin, password: admin). Remember, to create new user with admin role and delete the default one ASAP.</div>
	<div>
		<form name="loginform" id="loginform" method="post"	action="login" onsubmit="forceHttpsOnSubmit(this)">
			<div style="display: table-row;">
				<span></span>
			</div>
			<div class="_textbox_styler"
				ng-class="{_textbox_div_error:loginform.username.$invalid && loginform.username.$dirty}">
				<input type="text" id="username" name="username" autocomplete="off"
					ng-model="login.username" placeholder="USER NAME"
					ng-class="{_textbox_error:loginform.username.$invalid && loginform.username.$dirty}"
					required>
			</div>
			<div style="padding-top: 15px;">
				<span></span>
			</div>
			<div class="_textbox_styler"
				ng-class="{_textbox_div_error:loginform.password.$invalid && loginform.password.$dirty}">
				<input type="password" id="password" name="password"
					placeholder="PASSWORD"
					ng-class="{_textbox_error:loginform.password.$invalid && loginform.password.$dirty}"
					ng-model="login.password" required>
			</div>
			<div style="padding-top: 15px;" ng-disabled="loginform.$invalid">
				<button>LOGIN</button>
			</div>
			<c:if test="${param.login_error == 1}"><div id="errorMsg">Invalid username or password</div> </c:if>
			<c:if test="${param.login_error == 2}"><div id="errorMsg">You have been successfully logged out</div> </c:if>
			<c:if test="${param.login_error == 3}"><div  id="errorMsg">Your session has timed out. Please log in again.<div></c:if>
			<c:if test="${param.login_error == 4}"><div  id="errorMsg">You have been logged out. Please log in again.<div></c:if>
			<c:if test="${param.login_error == 5}"><div  id="errorMsg">Mutiple sessions from same user are not allowed.<div></c:if>
		</form>
	</div>
	<div style="position:fixed;bottom:5px; right: 5px;color:#fff;font-size:12px;">BEST VIEWED IN MOZILLA FIREFOX BROWSER</div>
	<div style="position:fixed;bottom:5px; left: 5px;color:#fff;font-size:12px;line-height:12px;text-align:left;">
		Pluto Version: <%=Utilities.getVerion() %>
		</br>
		Charon Version: <%=Utilities.getCharonVerion()%>
		</br>
		&copy; wwww.rationalminds.net
	</div>
</body>
</html>