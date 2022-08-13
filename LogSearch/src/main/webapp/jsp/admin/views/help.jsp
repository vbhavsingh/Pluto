<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
<head>
<title>TODO supply a title</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<div>
		<h1>Log Analyzer Administration Console</h1>
	</div>
	<div style="width: 75%; margin-left: 100px; margin-top: 40px;">
		<ul>
			<h3>User Administration :</h3>
			<p>
				Create and edit users in the application. If this application is
				installed new, there will be a default user <b>admin</b>, delete
				this user after creating other id with same privileges.
			</p>
			<p>
				This function is accessible to people with <b>admin</b> or <b>group
					admin</b> roles only. <b>Group members</b> are not allowed to access
				this function.
			</p>
			<li><h4>Create User :</h4>
				<p>Users which have access on servers where search is to be
					done, will only be able to run search against those servers. Use
					the same user name while creating users in this system. As soon a
					user is created he is automatically mapped to all the servers where
					users' id is present.</p>
					<p><h4>API Access:</h4> Individual users can be granted API access. 
					API access works with basic HTTP authentication using the same credentials.
					API accepts input as GET and POST (JSON).</p>
					<p>The following service URL's can be used to access the API.</p>
					<ul>
						<li><b>http(s)://{server:port}/rs/search/json</b> accepts JSON input for following fields and basic authetication over POST.</li>
						<li><b>http(s)://{server:port}/rs/search</b> accepts input for following fields and basic authetication over GET.</li>
						<ul>
							<li><b>logPathPatterns :</b> Either this or file name must be provided. Multiple data must be space seperated.</li>
							<li><b>logFileNamePatterns :</b> Either this or file path must be provided. Multiple data must be space seperated.</li>
							<li><b>search :</b> Mandatory field. Search keywords and this is a Mandatory field. Multiple data must be space seperated.</li>
							<li><b>searchOnLabels :</b> Mandatory field. Server groups on which search need to be executed.</li>
							<li><b>fromDateTime :</b> Optional field.</li>
							<li><b>toDateTime :</b> Optional field.</li>
							<li><b>viewTz :</b> Mandatory field. The timezone in which you want results to be translated.</li>
						</ul>
					</ul>
					<p>The input conforms to the same standards as allowed from the user interface.</p>
			</li>
			<li><h4>Edit User :</h4>
				<p>User can edit own profile and other profiles depending upon
					role and what group user belongs to. A user from one group cannot
					access users from another group. A user with super user role can
					access and edit users from other groups.</p></li>
		</ul>
		<ul>
			<h3>Group Administration :</h3>
			<p>
				Groups are created to aggregate search against a set of servers. One
				server can be part of multiple groups and likewise a user can be
				part of multiple groups, but will not be able to run search against
				the server where users' userid is not present. There is an exception
				in system, user having <b>super-group</b> are allowed to access
				nodes irrespective of their user id's presence on the server.
			</p>
			<p>
				This function is accessible to people with <b>admin</b> or <b>group
					admin</b> roles only. <b>Group members</b> are not allowed to access
				this function.
			</p>
			<li><h4>Create Group :</h4>
				<p>Group name is a logical name to aggregate users. Groups can
					be useful in case multiple applications want to use same instance
					of this system in shared fashion. This screen allows one to define
					a logical name only, providing a clear description will come handy
					in future, so put proper description for every group that is
					created.</p></li>
			<li><h4>Edit Group :</h4>
				<p>A group can only be edited by group members with group admin
					privileges. Users who are not part of a group, will not see that
					group on the edit screen. Groups can only be deleted when they have
					no users associated with them.</p></li>
		</ul>
		<ul>
			<h3>Node Administration :</h3>
			<p>As soon an agent is turned on, it registers itself back with
				the system. System also automatically associates the present users
				with the server, depending whether those userid's are present on the
				server. There are some default parameters that are set against each
				agent, deciding features of the agent, they can be changed within a
				limit to fine tune system to one's requirement. The changes are
				applied on real time.</p>
		</ul>
	</div>
</body>
</html>
