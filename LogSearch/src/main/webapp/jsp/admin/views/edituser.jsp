<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="_editUserForm" ng-controller="SaveUserChagesController">
<form name="edituserform" ng-submit="saveChanges()" novalidate>
<div>
	<div>
		<div><label> Username </label>
               <input id="username" 
               		  autocomplete="off"
                      name="username" 
                      type="text" 
                      disabled="disabled"
                      ng-model="user.username" 
                      required>
           </div>
	</div>
	<div>
		<div ng-class="{_div_error:edituserform.password.$invalid && edituserform.password.$dirty}"><label>New Password</label>
			<input type="password" id="password"
				title="Should be 6 to 30 charcters long"
				placeholder="minimum 6 characters" type="password" name="password"
				ng-model="user.password" ng-minlength=6 maxlength="30"
				ng-class="{_error_textbox:edituserform.password.$invalid && edituserform.password.$dirty}">
		</div>
	</div>
	<div>
		<div><label>First Name</label>
			<input type="text" id="firstname" name="firstname" autocomplete="off"
				ng-model="user.firstname">
		</div>
	</div>
	<div>
		<div><label>Last Name</label>
		<input type="text" id="lastname" name="lastname" autocomplete="off"
				ng-model="user.lastname">
		</div>
	</div>
	<div>
		<div ng-class="{_div_error:edituserform.email.$invalid && edituserform.email.$dirty}"><label> Email* </label>
               <input id="email" 
                      name="email" 
                      type="email" 
                      ng-model="user.email"
                      autocomplete="off"
                      ng-class="{_error_textbox:edituserform.email.$invalid && edituserform.email.$dirty}"
                      required>
           </div>
	</div>
	<div>
		<div><label>Role*</label>
		<select id="role" ng-multiple="false" name="role" 
			ng-model="user.role"
			required>
			 <c:forEach var="role" items="${model.assignableRoles}">
                <option title="${role.description}" value="${role.rolename}">${role.rolename}</option>
             </c:forEach>
		</select> 
		</div>
	</div>
	<div>
	<div>
		<label>Groups*</label>
           <select id="groups" 
                   name="groups"
                   multiple="multiple"
                   ng-model="user.groups"
                   required>
			 <c:forEach var="gp" items="${model.assignableGroups}">
                <option title="${gp.description}" value="${gp.name}" > ${gp.name}</option>
             </c:forEach>
           </select>
           <div id="_selection_list">
                <ul><span>Selected Groups</span>
                     <li ng-repeat="gp in user.groups">{{gp}}</li>
                </ul>
           </div>
         </div>
     </div>
     <div>
          <div><label> Rest Access </label>
               <input id="robot" 
                       type="checkbox" 
                       name="robot" 
                       ng-model="user.robot">
               </select>
           </div>
     </div>
     
</div>
 	<div>
 		<input id="saveUserChanges" 
		ng-disabled="edituserform.$invalid" 
        type="submit"
        ng-style="edituserform.$invalid && {'background-color':'#999', 'outline': '#222 solid 1px'}"
        value="SAVE">
    </div>       
</form>
<div id="message">{{message}}</div>
</div>