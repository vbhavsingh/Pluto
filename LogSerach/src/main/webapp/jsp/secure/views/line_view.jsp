<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<div style="width: 100%; height: 500px;">
	<div id="fileMetaData">
		<div>
			<div>
				<span>File Name : </span>
				<span>{{fileLineReadPayload.fileName|ShortFileName}}</span>
			</div>
			<div>
				<span>Node : </span>
				<span>{{fileLineReadPayload.nodeName}}</span>
			</div>
		</div>
		<div>
			<div>
				<span>Result Time Zone : </span>
				<span>{{fileLineReadPayload.viewTz}}</span>
			</div>
			<div>
				<span>Size on Disk : </span>
				<span ng-show="hasLinesLoaded">{{fileData.readableFileSize}}</span>
				<span ng-show="!hasLinesLoaded">fetching..</span>
				<span ng-show="lineLoadingErrored" style="color: red;">failed to fetch!</span>
			</div>
		</div>
		<div>
			<div title="Some operating systems do not capture create date, in that case modified date and create date will be same.">
				<span>Created Date : </span>
				<span ng-show="hasLinesLoaded">{{fileData.createdDate}}</span>
				<span ng-show="!hasLinesLoaded">fetching..</span>
				<span ng-show="lineLoadingErrored" style="color: red;">failed to fetch!</span>
			</div>
			<div>
				<span>Last Modified Date : </span>
				<span ng-show="hasLinesLoaded">{{fileData.lastModifiedDate}}</span>
				<span ng-show="!hasLinesLoaded">fetching..</span>
				<span ng-show="lineLoadingErrored" style="color: red;">failed to fetch!</span>
			</div>
		</div>
	</div>
	<div class="ngdialog-scroll-container"
		style="overflow-y: auto; width: 100%; height: 400px;">
		<div id="fileTabRow">
			<div>Line</div>
			<div>Date</div>
			<div>Text</div>
		</div>
		<div id="fileTabRow" ng-repeat="line in fileData.logLines">
			<div>{{line.logLineNumber}}</div>
			<div>{{line.displayDate}}</div>
			<div>{{line.logText}}</div>
		</div>
	</div>
	<div id="loading-lines-img" ng-show="!hasLinesLoaded"></div>
	<div id="loading-lines-error" ng-show="lineLoadingErrored">Some
		error happened while loading data!</div>
</div>