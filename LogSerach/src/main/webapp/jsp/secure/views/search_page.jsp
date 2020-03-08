<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.log.server.LocalConstants" %>
<%@ taglib prefix="d" uri="http://www.decorator.tags" %>
	<div id="_search_main_form" class="centered" ng-controller="SearchController">
		<form id="form" name="searchform" ng-submit="doSearch()" >
			<div class="_table">
				<div class="_table_div_row">
					<div class="_table_div_label">
						<label>Log File Patterns</label>
					</div>
					<div class="_table_div_textbox">
						<div class="_div_textbox">
							<input					
							placeholder="Provide File Name Pattern"
							class="_textbox mandatory" 
							ng-model="input.logFileNamePtterns"
							name="logFileNamePtterns"
							id="logFileNamePtterns"
							path="logFileNamePtterns" 
							type="text" 
							maxlength="2048"
							auto-fill-help="${d:previousSearchCriterias(LocalConstants.FIELD_FILENAME)}"
							ng-class="{_error_textbox:searchform.logFileNamePtterns.$invalid}"
							ng-required="((input.logFileNamePtterns.length + input.logPathPatterns.length)<1)">
						</div>
					</div>
					<div popover-template="'log-file-pattern-help.html'"
						 popover-class="help-popover"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						<img class="_img_help" alt="" src="res/img/help-icon.png">
					</div>
				</div>
				<div class="_table_div_row">
					<div class="_table_div_label">
						<label>Log Path Patterns</label>
					</div>
					<div class="_table_div_textbox">
						<div class="_div_textbox">
							<input 
							placeholder="Provide Path Pattern"
							class="_textbox mandatory" 
							ng-model="input.logPathPatterns"
							name="logPathPatterns"
							id="logPathPatterns"
							type="text" 
							maxlength="2048" 
							auto-fill-help="${d:previousSearchCriterias(LocalConstants.FIELD_LOGPATH)}"
							ng-class="{_error_textbox:searchform.logFileNamePtterns.$invalid}"
							ng-required="((input.logFileNamePtterns.length + input.logPathPatterns.length)<1)">
						</div>
					</div>
					<div popover-template="'log-path-pattern-help.html'"
                         popover-class="help-popover"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						<img class="_img_help" alt="" src="res/img/help-icon.png">
					</div>					
				</div>
				<div class="_table_div_row">
					<div class="_table_div_label">
						<label>Labels To Search</label>
					</div>
					<div class="_table_div_textbox">
						<div class="_div_textbox">
							<input 
							placeholder="Click to Select Label, Multiple Labels Allowed"
							class="_textbox" 
							ng-model="input.searchOnLabels"
							name="searchOnLabels"
							id="searchOnLabels"
							type="text" 
							maxlength="2048" 
							auto-fill-help="${d:labels()}"
							ng-class="{_error_textbox:searchform.searchOnLabels.$invalid}"
							required>
						</div>
					</div>
					<div popover-template="'label-help.html'"
                         popover-class="help-popover"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						<img class="_img_help" alt="" src="res/img/help-icon.png">
					</div>
				</div>
				<div class="_table_div_row">
					<div class="_table_div_label">
						<label>Search Text</label>
					</div>
					<div class="_table_div_textbox">
						<div class="_div_textbox">
							<input 
								placeholder="Search Keyword or Phrase"
								class="_textbox" 
								ng-model="input.search"
								name="search"
								type="text"
								maxlength="400" 
								auto-fill-help="${d:previousSearchCriterias(LocalConstants.FIELD_KEYTEXT)}"
								ng-class="{_error_textbox:searchform.search.$invalid}"
								required>
						</div>
					</div>
					<div popover-template="'search-text-help.html'"
                         popover-class="help-popover"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						<img class="_img_help" alt="" src="res/img/help-icon.png">
					</div>					
				</div>
				<div class="_table_div_row">
					<div class="_table_div_label">
						<label title="Timezone in which you want to convert the result">Result
							Timezone</label>
					</div>
					<div class="_table_div_textbox" style="width: 100px;">
						<div class="_div_textbox">
							<select name="viewTz" 
									class="_textbox" 
									ng-model="input.viewTz"
									style="width: 60px;"									
									required
									ng-init="tzOptions=getArray('${model.allowedTimeZoneList}')"
									>
								<option ng-repeat="option in tzOptions" value="{{option}}" ng-selected="{{option==input.viewTz}}">{{option}}</option>
							</select>
						</div>
					</div>
					<div popover-template="'timezone-help.html'"
                         popover-class="help-popover"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						<img class="_img_help" alt="" src="res/img/help-icon.png">
					</div>					
				</div>
			</div>
			<div style="padding-left: 200px; position: inherit">
				<input type="submit" class="_submit_result" value="search logs"	 id="submitButton" />
			</div>
			<div id="errors" style="padding-left: 150px; padding-top: 10px; position: inherit; color: crimson; font-size: 14px;">
				<span ng-show="!(input.logFileNamePtterns.length || input.logPathPatterns.length) && (searchform.logFileNamePtterns.$dirty || searchform.logPathPatterns.$dirty)">Provide either log pattern or path</span>
				<br/>
				<span ng-show="searchform.searchOnLabels.$dirty && searchform.searchOnLabels.$invalid">Select one of the suggested labels</span>
				<br/>
				<span ng-show="searchform.search.$dirty && searchform.search.$invalid">Search text is mandatory field</span>
			</div>
		</form>
	</div>
	<script  type="text/ng-template" id="log-file-pattern-help.html">
	<div>
		<h3>File Names</h3>
		<p>All the files with extension <b>.log</b>, <b>.log.gz</b>, <b>.log.tar</b> can be searched. The log files which are present in users' home in Linux/ Unix platform and /var/log can be searched.</p>
		<p>Wildcards like '*' are allowed in the search.</p>
		<p>Search can be performed on a narrow set of files in a single request, a limitation to safeguard guest platforms. So try to be precise with your search for accurate results.</p>
		<p>Providing either file path or file name is mandatory to perform a search.</p>
	</div>
	</script>
	
	<script  type="text/ng-template" id="log-path-pattern-help.html">
	<div>
		<h3>File Paths</h3>
		<p>All file path in users' <b>home</b> and <b>/var/log</b> can be included in the search. Multiple paths can also be used by using space character between paths.</p>
		<p>Wildcards like '*' are allowed in the search.</p>
		<p>Search can be performed on a narrow set of files in a single request, a limitation to safeguard guest platforms. So try to be precise with your search for accurate results.</p>
		<p>Providing either file path or file name is mandatory to perform a search.</p>
	</div>
	</script>
		
	<script  type="text/ng-template" id="label-help.html">
	<div>
		<h3>Node Labels</h3>
		<p>Every agent can be assigned friendly labels from <b>Administration > Node Administration</b> screen. By default, every node is automatically assigned a label using node name. E.g. node <b>mynode.org.com</b> will automatically be labeled as <b>mynode</b>.</p>
		<p>Multiple labels can be assigned to a single node, e.g. if a node is used both in dev and test, you can assign labels <b>dev</b> and <b>test</b> both. Likewise, multiple nodes can be grouped together by assigning the same label.</p>
		<p>The search would be performed on all the nodes, which have mentioned label. Aggregated search result has a default limitation of 10Mb.</p>
		<p>Multiple labels can be used in a search, by using space character. Always choose the label from the suggestion, as these are only available labels.</p>   
		<p>Providing at least one label is mandatory.</p>
	</div>
	</script>
	
	<script  type="text/ng-template" id="search-text-help.html">
	<div>
		<h3>Search Text</h3>
		<p>Wildcards are allowed in the search. Search can be performed using a word or a phrase.</p>
		<p>If a phrase is used, then it must be contained within double quotes, e.g. "some error", will search for the phrase rather than individual words.</p>
		<p>Multiple words and phrases or the combination of both are allowed. All words and phrases need to be separated by space character.</p>
		<p>Search also supports exclusions, any word or phrase prefixed with <b>'-'</b> will be removed from the result. E.g. <b>'-info'</b>, will remove all lines which have word <b>info</b> in it.</p>
		<p>Providing at least one search keyword is mandatory.</p>
	</div>
	</script>
	
	<script  type="text/ng-template" id="timezone-help.html">
	<div>
 		<h3>Result Timezone</h3>
		<p>It is possible that in a distributed environment, nodes in the request might be in different time zones. The timezone of a request can be selected as per convenience.</p>
		<p>This selection makes all the date time recognized from log text to convert to requested timezone for viewing purpose. E.g. a user from <b>pacific</b> zone can view all the log records of <b>eastern</b> zone servers in the local timezone.</p>
		<p>Daylight savings are not accounted for in the result.</p>	
		<p>By default browsers timezone is used.</p>	
	</div>
	</script>