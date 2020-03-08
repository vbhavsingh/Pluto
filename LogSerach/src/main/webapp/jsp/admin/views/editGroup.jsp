<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div id="_edit_gp_save" style="display: table;"	ng-controller="SaveChangesGroupController">
	<form name="addgroup" ng-submit="savechanges()" novalidate>
		<div>
			<div ng-class="{_div_error:addgroup.name.$invalid && addgroup.name.$dirty}">
				<label>Group Name*</label> 
					<input 
					autocomplete="off"
					placeholder="Maximum 30 characters allowed" id="name" name="name"
					type="text" ng-model="group.name" maxlength="30"
					ng-class="{_error_textbox:addgroup.name.$invalid && addgroup.name.$dirty}"
					required> 
					<span ng-show="addgroup.name.$dirty && addgroup.name.$invalid"> 
					<span ng-show="addgroup.name.$error.required">This field is mandatory</span>
				</span>
			</div>
			<div ng-class="{_div_error:addgroup.description.$invalid && addgroup.description.$dirty}">
				<label>Description*</label>
				<textarea
					autocomplete="off"
					placeholder="Maximum 200 chracters allowed" id="description"
					name="description" ng-model="group.description" maxlength="200"
					ng-class="{_error_textbox:addgroup.description.$invalid && addgroup.description.$dirty}"
					required>
				</textarea>
				<span
					ng-show="addgroup.description.$dirty && addgroup.description.$invalid">
					<span ng-show="addgroup.description.$error.required">This
						field is mandatory</span>
				</span>
			</div>
			<div>
				<label></label>
				<input id="saveGroup" autocomplete="off" type="submit" ng-disabled="addgroup.$invalid"
					ng-style="addgroup.$invalid && {'background-color':'#999', 'outline': '#222 solid 1px'}"
					value="SAVE CHANGES">
			</div>
		</div>
	</form>
	<div style="padding-left: 100px;">{{message}}</div>
</div>

