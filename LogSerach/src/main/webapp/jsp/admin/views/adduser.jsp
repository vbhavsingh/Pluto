<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <div id="_userForm" ng-controller="AddUserController">
                <form style="width: 600px;" name="adduser" ng-submit="add()" novalidate>
                <div>
                    <div ng-class="{_div_error:adduser.username.$invalid && adduser.username.$dirty}"><label> Username*  </label>
                        <input title="Mandatory field, must be 5 to 10 charcters long" 
                        	   autocomplete="false"
                               placeholder="5 to 8 characters only" 
                               id="username" 
                               type="text" 
                               name="username"
                               ng-model="user.username" 
                               ng-minlength=5
                               maxlength="8" 
                               placeholder ="prefer company id"
                               ng-class="{_error_textbox:adduser.username.$invalid && adduser.username.$dirty}"
                               required>
                        <span ng-show="adduser.username.$dirty && adduser.username.$invalid">
                            <span ng-show="adduser.username.$error.required">This field is mandatory</span>
                            <span ng-show="adduser.username.$error.minlength">Username should be least 5 characters</span>
                        </span>
                    </div>
                    <div ng-class="{_div_error:adduser.password.$invalid && adduser.password.$dirty}"><label> Password*  </label>
                        <input id="password" 
                        	   autocomplete="false"
                               title="Mandatory field, must be 6 to 30 charcters long" 
                               placeholder="minimum 6 characters" 
                               type="password" 
                               name="password"
                               ng-model="user.password" 
                               ng-minlength=6
                               maxlength="30"
                               ng-class="{_error_textbox:adduser.password.$invalid && adduser.password.$dirty}"
                               required>
                         <span ng-show="adduser.password.$dirty && adduser.password.$invalid">
                            <span ng-show="adduser.password.$error.required">This field is mandatory</span>
                            <span ng-show="adduser.password.$error.minlength">Password should be least 6 characters</span>
                        </span>
                    </div>
                    <div><label> First Name </label>
                        <input id="firstName" 
                        	   autocomplete="off" 
                               type="text" 
                               name="firstname"
                               ng-model="user.firstname">
                    </div>
                    <div><label> Last Name </label>
                        <input id="lastName" 
                        	   autocomplete="off"
                               type="text" 
                               name="lastname"
                               ng-model="user.lastname">
                    </div>
                    <div ng-class="{_div_error:adduser.email.$invalid && adduser.email.$dirty}"><label> Email* </label>
                        <input id="email" 
                        	   autocomplete="off"
                               name="email" 
                               type="email" 
                               ng-model="user.email" 
                               ng-class="{_error_textbox:adduser.email.$invalid && adduser.email.$dirty}"
                               required>
                        <span ng-show="adduser.email.$dirty &&  adduser.email.$invalid"  class="help-inline">
                            <span ng-show="adduser.email.$error.required">This field is mandatory</span>
                            <span ng-show="!adduser.email.$error.required && adduser.email.$invalid">Provide valid email address</span>
                        </span>
                    </div>
                    <div><label> Groups* </label>
                        <select id="groups" 
                                multiple 
                                name="groups"
                                ng-model="user.groups" 
                                required>
			 					<c:forEach var="gp" items="${model.assignableGroups}">
                					<option title="${gp.description}" value="${gp.name}">${gp.name}</option>
             					</c:forEach>
                        </select>
                        <div id="_selection_list" ng-show="adduser.groups.$valid">
                        	<ul><span>Selected Groups</span>
                        	<li ng-repeat="gp in user.groups">{{gp}}</li>
                        	</ul>
                        </div>
                    </div>
                    <div><label> Role* </label>
                        <select id="role" 
                                ng-multiple="false" 
                                name="role" 
                                ng-model="user.role" 
                                required>
			 					<c:forEach var="role" items="${model.assignableRoles}">
			 						<c:if test = "${role.visible}">
                						<option title="${role.description}" value="${role.rolename}">${role.rolename}</option>
                					</c:if>
             					</c:forEach>
                        </select>
                         <span ng-show="adduser.role.$dirty &&  adduser.role.$invalid"  class="help-inline">
                            <span ng-show="adduser.role.$invalid">This field is mandatory</span>
                        </span>
                    </div>
                    <div><label> Rest Access </label>
                        <input id="robot" 
                                type="checkbox" 
                                name="robot" 
                                ng-model="user.robot">
                        </select>
                    </div>
                </div>
                    <div><input id="addUser" 
                                ng-disabled="adduser.$invalid" 
                                type="submit"
                                ng-style="adduser.$invalid && {'background-color':'#999', 'outline': '#222 solid 1px'}"
                                value="ADD USER">
                    </div>
                </form>
            </div>