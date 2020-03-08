<%@page import="com.log.analyzer.commons.Constants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div id="_edit_node_save" style="display: table;" ng-controller="SaveNodeChangesController">
	<form name="editnode" ng-submit="savechanges()" novalidate>
		<div>
			<div>
				<label>Node Name</label> 
				<span>{{node.nodeName}}</span>
			</div>
			
			<div>
				<label>Port</label> 
				<span>{{node.port}}</span>
			</div>
			
			<div>
				<label>Labels</label> 
				<div style="max-width: 250px;">
					<span  ng-repeat="label in node.labels" 
							style="background-color:#6699FF; 
								   padding: 0px 2px 0px 2px;color: #ffffff;
								   outline: 1px solid #8AB8E6;margin-right: 5px;
								   display:inline-block;
								   margin:2px 10px 2px 0px;
								   float: left;">
						<img style="height: 10px; display: inline;" src="../../res/img/label-icon.png">
						<span style="display: inline;font-size: 10px;padding: 0px 0px 0px 0px;height: 12px;margin: 0 1px 0 0;">{{label}}</span>
						<img ng-show="delteable($index)" ng-click="removeLabel($index)" style="width:8px;display: inline;position:absolute;" src="../../res/img/cross-icon.png">
					</span>
				</div>
			</div>	
			
			<div>

			</div>					
			
			<div>
				<label>Add Labels</label> 
				<input style="width: 200px;" ng-keypress="addLabel($event)" placeholder="Press space key to add label" id="agentName" name="agentName"
					type="text" ng-model="node.newlabel" title="Maximum 15 characters" maxlength="15"> 
			</div>	
			
			<div  ng-class="{_div_error:editnode.agentName.$invalid && editnode.agentName.$dirty}">
				<label title="Any custom name can be given to agent">Agent Name*</label>
				<input style="width: 200px;"	placeholder="maximum 30 characters" id="agentName" name="agentName"
					type="text" ng-model="node.agentName" maxlength="30"
					ng-class="{_error_textbox:editnode.agentName.$invalid && editnode.agentName.$dirty}"
					required> 
				<span ng-show="editnode.agentName.$dirty && editnode.agentName.$invalid"> 
					<span ng-show="editnode.agentName.$error.required">required field</span>
				</span>
			</div>
			
			<div ng-class="{_div_error:editnode.parallelism.$invalid && editnode.parallelism.$dirty}">
				<label title="Parallel file allowed to be searched in a request">Parallel*</label>
				<input 	placeholder="<%=Constants.PROPERTIES.MAX_THREADS%>" id="parallelism" name="parallelism"
					type="number" ng-model="node.parallelism" min="1" max="<%=Constants.PROPERTIES.MAX_THREADS%>"
					ng-class="{_error_textbox:editnode.parallelism.$invalid && editnode.parallelism.$dirty}"
					required> 
				<span ng-show="editnode.parallelism.$dirty && editnode.parallelism.$invalid"> 
					<span ng-show="editnode.parallelism.$error.required">required field</span>
					<span ng-show="editnode.parallelism.$error.max">maximum allowed is '<%=Constants.PROPERTIES.MAX_THREADS%>' </span>
					<span ng-show="editnode.parallelism.$error.min">minimum allowed is '1'</span>	
					<span ng-show="editnode.parallelism.$error.number">numeric only</span>
				</span>
			</div>
			
			<div ng-class="{_div_error:editnode.maxFilesToSerach.$invalid && editnode.maxFilesToSerach.$dirty}">
				<label title="Maximum files that can be searched ">Maximum Files To Search*</label>
				<input 	placeholder="<%=Constants.PROPERTIES.MAX_FILES_TO_SEARCH%>" id="maxFilesToSerach" name="maxFilesToSerach"
					type="number" ng-model="node.maxFilesToSerach" min="1" max="<%=Constants.PROPERTIES.MAX_FILES_TO_SEARCH%>"
					ng-class="{_error_textbox:editnode.maxFilesToSerach.$invalid && editnode.maxFilesToSerach.$dirty}"
					required> 
				<span ng-show="editnode.maxFilesToSerach.$dirty && editnode.maxFilesToSerach.$invalid"> 
					<span ng-show="editnode.maxFilesToSerach.$error.required">required field</span>
					<span ng-show="editnode.maxFilesToSerach.$error.max">maximum allowed is '<%=Constants.PROPERTIES.MAX_FILES_TO_SEARCH%>' </span>
					<span ng-show="editnode.maxFilesToSerach.$error.min">minimum allowed is '1'</span>	
					<span ng-show="editnode.maxFilesToSerach.$error.number">numeric only</span>					
				</span>
			</div>
		
			<div ng-class="{_div_error:editnode.maxLinesPerFile.$invalid && editnode.maxLinesPerFile.$dirty}">
				<label title="Maximum lines that can be picked from a single file">Maximum Lines Per File*</label>
				<input 	placeholder="<%=Constants.PROPERTIES.MAX_LINES_PER_FILE%>" id="maxLinesPerFile" name="maxLinesPerFile"
					type="number" ng-model="node.maxLinesPerFile" min="1" max="<%=Constants.PROPERTIES.MAX_LINES_PER_FILE%>"
					ng-class="{_error_textbox:editnode.maxLinesPerFile.$invalid && editnode.maxLinesPerFile.$dirty}"
					required> 
				<span ng-show="editnode.maxLinesPerFile.$dirty && editnode.maxLinesPerFile.$invalid"> 
					<span ng-show="editnode.maxLinesPerFile.$error.required">required field</span>
					<span ng-show="editnode.maxLinesPerFile.$error.max">maximum allowed is '<%=Constants.PROPERTIES.MAX_LINES_PER_FILE%>' </span>
					<span ng-show="editnode.maxLinesPerFile.$error.min">minimum allowed is '1'</span>	
					<span ng-show="editnode.maxLinesPerFile.$error.number">numeric only</span>							
				</span>
			</div>			
					
			<div ng-class="{_div_error:editnode.maxLinesInresult.$invalid && editnode.maxLinesInresult.$dirty}">
				<label title="Maximum total lines in a search result">Maximum Lines In Result*</label>
				<input 	placeholder="<%=Constants.PROPERTIES.MAX_LINES_IN_RESULT%>" id="maxLinesInresult" name="maxLinesInresult"
					type="number" ng-model="node.maxLinesInresult" min="1" max="<%=Constants.PROPERTIES.MAX_LINES_IN_RESULT%>"
					ng-class="{_error_textbox:editnode.maxLinesInresult.$invalid && editnode.maxLinesInresult.$dirty}"
					required> 
				<span ng-show="editnode.maxLinesInresult.$dirty && editnode.maxLinesInresult.$invalid"> 
					<span ng-show="editnode.maxLinesInresult.$error.required">required field</span>
					<span ng-show="editnode.maxLinesInresult.$error.max">maximum allowed is '<%=Constants.PROPERTIES.MAX_LINES_IN_RESULT%>' </span>
					<span ng-show="editnode.maxLinesInresult.$error.min">minimum allowed is '1'</span>	
					<span ng-show="editnode.maxLinesInresult.$error.number">numeric only</span>						
				</span>
			</div>		
			
			<div ng-class="{_div_error:editnode.resultInKb.$invalid && editnode.resultInKb.$dirty}">
				<label title="Maximum size of results in kilobytes">Result Size (kilobytes)*</label>
				<input 	placeholder="<%=Constants.PROPERTIES.RESULT_SIZE_KB%>" id="resultInKb" name="resultInKb"
					type="number" ng-model="node.resultInKb" min="1" max="<%=Constants.PROPERTIES.RESULT_SIZE_KB%>"
					ng-class="{_error_textbox:editnode.resultInKb.$invalid && editnode.resultInKb.$dirty}"
					required> 
				<span ng-show="editnode.resultInKb.$dirty && editnode.resultInKb.$invalid"> 
					<span ng-show="editnode.resultInKb.$error.required">required field</span>
					<span ng-show="editnode.resultInKb.$error.min">minimum allowed is '1' </span>
					<span ng-show="editnode.resultInKb.$error.max">maximum allowed is '<%=Constants.PROPERTIES.RESULT_SIZE_KB%>' </span>
					<span ng-show="editnode.resultInKb.$error.number">numeric only</span>	
				</span>
			</div>				
						
			<div>
				<label style="color:#5C0442;padding-top: 10px;">{{nodemessage}}</label>
				<input id="saveNode" type="submit" ng-disabled="editnode.$invalid"
					ng-style="editnode.$invalid && {'background-color':'#999', 'outline': '#222 solid 1px'}"
					value="SAVE CHANGES">
			</div>
		</div>
	</form>
</div>

