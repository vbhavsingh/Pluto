<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<div>
	<form name="readlineform" ng-controller="LineReaderController">
		<div style="font-size: 14px;width: 100%;padding-left: 0;"><label>File : </label><span>{{fileLineReadPayload.fileName|ShortFileName}}</span></div>
		<div>
			<div>
				<label>From line number</label>
			</div>
			<div>
				<input type="text" ng-model="fileLineReadPayload.startIndex">
			</div>
		</div>
		<div>
			<div>
				<label>To line number</label>
			</div>
			<div>
				<input type="text" ng-model="fileLineReadPayload.endIndex">
			</div>
		</div>
		<div>
			<input type="submit" value="Get Lines" ng-click="fetchlines()">
		</div>
	</form>
	<div>