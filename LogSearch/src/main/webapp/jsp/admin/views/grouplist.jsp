<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div class="_user_list_table" ng-controller="EditGroupController">
	<!-- table heaader -->
	<div class="_user_list_table_row_header">
		<div class="_user_list_table_cell"></div>
		<div class="_user_list_table_cell">Group Name</div>
		<div class="_user_list_table_cell">Created By</div>
		<div class="_user_list_table_cell">Created Date</div>
		<div class="_user_list_table_cell">Group Members</div>
		<div class="_user_list_table_cell">Edit</div>
		<div class="_user_list_table_cell">Delete</div>
	</div>
	<div class="_user_list_table_row" 	ng-repeat="group in groups">
			<div class="_user_list_table_cell">
				<img alt="" src="../../res/img/user-group-icon.png" style="width: 20px;">
			</div>
			<div class="_user_list_table_cell"> <span popover="{{group.description}}" popover-placement="right" popover-title="Description" popover-trigger="mouseenter">{{group.name}}</span></div>
			<div class="_user_list_table_cell">{{group.createdBy}}</div>
			<div class="_user_list_table_cell">{{group.createdDate}}</div>
			<div class="_user_list_table_cell">
				<input type="image" src="../../res/img/user-icon.png" style="width: 20px;margin: 0 30px 0;" 
				popover-template="dynamicPopover.templateUrl" 
				popover-placement="right" 
				popover-title="Users" 
				popover-trigger="mouseenter">
			</div>
			<div class="_user_list_table_cell">
				<input type="image" src="../../res/img/edit-icon-black.png"
					style="width: 20px;" ng-click="editGroup($index)">
			</div>
			<div class="_user_list_table_cell">
				<input ng-show="usersexist($index)==false"  type="image" src="../../res/img/trash_icon.png" 
					ng-confirm-box-click="Are you sure want to delete?"
					style="width: 20px;" confirmed-click="deleteGroup($index)">
				<input ng-show="usersexist($index)==true"  type="image" src="../../res/img/trash_icon_red.png" 
					style="width: 20px;"
					popover="Users exist in the group, cannot be deleted" popover-placement="right"
					popover-trigger="mouseenter">
			</div>
	</div>
</div>
<script type="text/ng-template" id="user-list.html">
     <div ng-repeat="user in group.users">{{user}}</div>
</script>
