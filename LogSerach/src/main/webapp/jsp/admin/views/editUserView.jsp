<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div>
	<div>
		<div>Username</div>
		<div>{{admin.username}}</div>
	</div>
	<div>
		<div>New Password*</div>
		<div>
			<input type="password" id="password"
				title="Mandatory field, must be 6 to 30 charcters long"
				placeholder="minimum 6 characters" type="password" name="password"
				ng-model="user.password" ng-minlength=6 maxlength="30"
				ng-class="{_error_textbox:adduser.password.$invalid && adduser.password.$dirty}"
				required>
		</div>
	</div>
	<div>
		<div>First name</div>
		<div>{{admin.firstname}}</div>
	</div>
	<div>
		<div>Last Name</div>
		<div>{{admin.lastname}}</div>
	</div>
	<div>
		<div>Email</div>
		<div>{{admin.lastname}}</div>
	</div>
	<div>
		<div>Role*</div>
		<div>{{admin.lastname}}</div>
	</div>
	<div>
		<div>Groups*</div>
		<div>{{admin.lastname}}</div>
	</div>
</div>