<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div class="_user_list_table" ng-controller="EditUserController" style="width:80%;">
	<!-- table heaader -->
	<div class="_user_list_table_row_header">
		<div class="_user_list_table_cell" style="max-width: 30px; min-width: 30px;"></div>
		<div class="_user_list_table_cell" style="max-width: 50px; min-width: 50px;">User</div>
		<div class="_user_list_table_cell" style="max-width: 80px; min-width: 60px;">Name</div>
		<div class="_user_list_table_cell" style="max-width: 80px; min-width: 50px;">Email</div>
		<div class="_user_list_table_cell" style="max-width: 50px; min-width: 50px;">Role</div>
		<div class="_user_list_table_cell" style="max-width: 70px; min-width: 70px;">Created By</div>
		<div class="_user_list_table_cell" style="max-width: 50px; min-width: 50px;">Created Date</div>
		<div class="_user_list_table_cell" style="max-width: 50px; min-width: 50px;">Groups</div>
		<div class="_user_list_table_cell" style="max-width: 50px; min-width: 50px;">Edit</div>
		<div class="_user_list_table_cell" style="max-width: 50px; min-width: 50px;">Delete</div>
	</div>
	<div class="_user_list_table_row" ng-repeat="user in users">
			<div class="_user_list_table_cell">
				<img alt="" src="../../res/img/server_tn_black.png" style="width: 20px;"
				popover-template="'node-list.html'" 
				popover-placement="right" 
				popover-title="Nodes" 
				popover-trigger="mouseenter">
			</div>
			<div class="_user_list_table_cell" >{{user.username}}</div>
			<div class="_user_list_table_cell" >{{user.firstname}} {{user.lastname}}</div>
			<div class="_user_list_table_cell" >{{user.email}}</div>
			<div class="_user_list_table_cell" >{{user.role}}</div>
			<div class="_user_list_table_cell" >{{user.createdBy}}</div>
			<div class="_user_list_table_cell" >{{user.createdDate}}</div>
			<div class="_user_list_table_cell">
				<input type="image" alt="" src="../../res/img/user-group-icon.png" style="width: 20px;" 
				popover-template="'group-list.html'" 
				popover-placement="right" 
				popover-title="Groups" 
				popover-trigger="mouseenter">
				
				<input type="image" alt="" src="../../res/img/api_icon.png" style="width: 20px;" 
				popover="api access enabled" 
				popover-trigger="mouseenter"
				ng-show="{{user.robot}}"
				>
			</div>
			<div class="_user_list_table_cell">
				<input type="image" src="../../res/img/edit-icon-black.png"
					style="width: 18px;" ng-click="editUser($index)"
					popover="click to edit"
					popover-trigger="mouseenter">
			</div>
			<div class="_user_list_table_cell">
				<input type="image" src="../../res/img/trash_icon.png" ng-confirm-box-click="Are you sure want to delete?"
					style="width: 18px;" confirmed-click="deleteUser($index)"
					popover="click to delete"
					popover-trigger="mouseenter">
			</div>
	</div>
</div>
<script type="text/ng-template" id="group-list.html">
     <div ng-repeat="gp in user.groups">{{gp}}</div>
</script>
<script type="text/ng-template" id="node-list.html">
     <div ng-repeat="node in user.myNodes">{{node}}</div>
</script>