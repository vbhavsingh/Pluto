<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
 <div style="width: 95%;display: inline-block;margin-top: 50px;margin-left: 50px;" ng-controller="NodeAgentController">
 	<div  ng-show="nodes.length == 0" style="max-width: 90%;min-width:150px;display: inline;float: left;color: black;font-size: 14px;">
 		<span>There are either no nodes registered yet, or you don't have enough privilege to view and edit nodes.</span>
 	</div>
     <div id="_node_container"  ng-repeat="node in nodes" style="max-width: 100px;min-width:100px;height:70px;display: block;float: left;position: relative;">
             	 <div style="width: 10px;height: 10px;position: absolute;z-index: 1;">              	
                     <input  type="image" src="../../res/img/close.png" ng-click="deletenodedialog($index)"
							width="9" style="display: block;float: left: margin-left: 10px;"
							popover="Shutdown remote agent and delete its properties. Deleted agent need to be started manually for registration." 
							popover-placement="right"
							popover-append-to-body="true"
							popover-trigger="mouseenter">				
                 </div>
                 <div style="width: 10px;height: 10px;position: absolute;left: 50px;z-index:1;">              	
                     <input  type="image" src="../../res/img/edit-icon.png" ng-click="editnode($index)"
							width="7" style="display:block; ;float: right;"
							popover="Click to edit agent properties on this node" 
							popover-placement="right"
							popover-append-to-body="true"
							popover-trigger="mouseenter">				
                 </div>
                 <div style="width: 10px;height: 10px;position: absolute;top: 15px;left: 50px;z-index: 1;">
                     <input  type="image" src="../../res/img/user-icon.png"
							width="7" style="display: block;float: right;"
							popover-template="'user-list.html'" 
							popover-placement="bottom"
							popover-trigger="mouseenter"
							popover-append-to-body="true"
							popover-title="Mapped Users">				 
                 </div>
                 <div style="width: 10px;height: 10px;position: absolute;top: 30px;left: 50px;z-index: 1;">
                     <input  type="image" src="../../res/img/label-icon-black.png"
							width="7" style="display: block;float: right;"
							popover-template="'label-list.html'" 
							popover-placement="bottom"
							popover-trigger="mouseenter"
							popover-append-to-body="true"
							popover-title="Assigned Labels">				
                 </div>
                 <div style="width: 60px;position: absolute;z-index: 0;">
                 <img src="../../res/img/server-icon.png" width="40" ng-show="node.alive"
                 	style="display: block;top: 0px;margin-left: 10px;"
                 	popover-template="'node-props.html'" 
                 	popover-placement="bottom" 
                 	popover-trigger="mouseenter"
                 	popover-append-to-body="true"
                 	popover-title="Agent Properties">
                   <img src="../../res/img/server-icon-red.png" width="40" ng-show="!node.alive"
                 	style="display: block;top: 0px;margin-left: 10px;"
                 	popover="No response from agent on this node since last {{node.deadSince}}. No search will be attempted on this node." 
                 	popover-placement="right" 
                 	popover-trigger="mouseenter"
                 	popover-append-to-body="true"
                 	popover-title="Agent may be down !!!">    
                 </div>      
                 <div style="position: absolute;width: 60px;overflow:hidden;color: black;text-align: center;top:35px;z-index: 1;">
               	 	<span 
               	 	popover="{{node.nodeName}}" 
					popover-placement="bottom"
					popover-append-to-body="true"
					popover-trigger="mouseenter"
               	 	style="font-size: 10px;font-weight: bold;display: inline-block;">
               	 		{{node.nodeName.split('.')[0]}}
               	 	</span>
               	 </div>
           	</div>
 </div>
 
 <script type="text/ng-template" id="label-list.html">
		<div style="min-width:120px;" ng-repeat="label in node.labels">
			<span>{{label}}</span> 
		</div>
</script>
 
<script type="text/ng-template" id="user-list.html">
		<div style="min-width:120px;" ng-repeat="user in node.users">
			<span>{{user}}</span> 
		</div>
</script>
 
 <script type="text/ng-template" id="node-props.html">
     <div style="display:table;font-size:10px;;">
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Agent Version</span> 
			<span style="display:table-cell; width:100px;">{{node.version}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Node OS</span> 
			<span style="display:table-cell; width:100px;">{{node.osName}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Agent Host</span> 
			<span style="display:table-cell; width:100px;">{{node.agentName}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Agent Port</span> 
			<span style="display:table-cell; width:100px;">{{node.port}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Parallelism</span> 
			<span style="display:table-cell; width:100px;">{{node.parallelism}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Max Files to Search</span> 
			<span style="display:table-cell; width:100px;">{{node.maxFilesToSerach}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Max Lines Per File</span> 
			<span style="display:table-cell; width:100px;">{{node.maxLinesPerFile}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Max Lines In Result</span> 
			<span style="display:table-cell; width:100px;">{{node.maxLinesInresult}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Max Result Size</span> 
			<span style="display:table-cell; width:100px;">{{node.resultInKb}} KB</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:120px;">Last Heartbeat</span> 
			<span style="display:table-cell; width:100px;">{{node.lastHeartbeat}}</span> 
		</div>
		<div style="display:table-row; width:220px;">
 			<span style="display:table-cell; width:220px;text-align: center;padding-top:2px;color:#6D07F2;font-style:bold;">click on left top icon to edit.</span> 
		</div>
	 </div>
</script>
 