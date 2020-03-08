<%@page import="com.log.analyzer.commons.Constants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.log.server.LocalConstants" %>
<%@taglib prefix="d" uri="http://www.decorator.tags" %>
<div class="overlay" ng-show="loading">
    <div id="loading-img"></div>
</div>
<div ng-controller="SearchResultController" class="_search_header">
    <form id="form" name="searchform"  ng-submit="doSearch()">
        <div style="display: inline-block;">
            <div class="_table_header">
                <div class="_table_div_row_header">
                    <div class="_table_div_label_header"><label>Log File Patterns</label></div>
                    <div class="_table_div_textbox_header">
                        <div class="_div_textbox_header">
                        		<input ng-model="input.logFileNamePtterns" 
                        			class="_textbox_header mandatory" 
                        			id="logFileNamePtterns"  
                        			type="text" 
                        			auto-fill-help="${d:previousSearchCriterias(LocalConstants.FIELD_FILENAME)}"
                        			autocomplete="off" 
                        			maxlength="2048" />
                        </div>
                    </div>
                    <div class="_table_div_label_header"><label>Log Path Patterns</label></div>
                    <div class="_table_div_textbox_header">
                        <div class="_div_textbox_header">
                        		<input ng-model="input.logPathPatterns" 
                        		class="_textbox_header mandatory" 
                        		id="logPathPatterns" 
                        		type="text" 
                        		auto-fill-help="${d:previousSearchCriterias(LocalConstants.FIELD_LOGPATH)}"
                        		autocomplete="off" 
                        		maxlength="2048"/>
                        	</div>
                    </div> 
                    <div class="_table_div_label_header _table_div_label_time_header"><label>From Date</label></div>
                    <div class="_table_div_textbox_header _table_div_textbox_time_header">
                        <div class="_div_textbox_header">
                        	<input 
                        	ng-model="input.fromDateTime" 
                        	datetime-picker
                        	ng-change="alert('hello')"
                        	date-format="<%=Constants.SEARCH_DATE_FORMAT.toPattern() %>"
                        	close-on-select="false"
                        	class="_textbox_header" 
                        	id="search" 
                        	type="text" 
                        	autocomplete="off" 
                        	maxlength="19"
                        	readonly/>
                        </div>
                    </div>   
                    <div class="_table_div_label_header"><label title="Timezone in which you want to convert the result">Result Timezone</label></div>
                    <div class="_table_div_textbox_header" style="width: 100px;">
                        <div class="_div_textbox_header">
                            <select  ng-model="input.viewTz" class="_textbox_header" style="width: 60px;">
                                <option ng-repeat="option in tzOptions" value="{{option}}" ng-selected="{{option==input.viewTz}}">{{option}}</option>
                            </select >  
                        </div>
                    </div>
                </div>
                <div class="_table_div_row_header">
                    <div class="_table_div_label_header"><label>Labels To Search</label></div>
                    <div class="_table_div_textbox_header">
                        <div class="_div_textbox_header">
                        	<input ng-model="input.searchOnLabels" 
                        	class="_textbox_header" 
                        	id="searchOnLabels"  
                        	type="text" 
                        	auto-fill-help="${d:labels()}"
                        	autocomplete="off" 
                        	maxlength="2048"/>
                        </div>
                    </div>
                    <div class="_table_div_label_header"><label>Search Text</label></div>
                    <div class="_table_div_textbox_header">
                        <div class="_div_textbox_header">
                        	<input ng-model="input.search" 
                        	class="_textbox_header" 
                        	id="search" 
                        	type="text" 
                        	autocomplete="off" 
                        	auto-fill-help="${d:previousSearchCriterias(LocalConstants.FIELD_KEYTEXT)}"
                        	maxlength="400"
                        	required/>
                        </div>
                    </div>
                    <div class="_table_div_label_header _table_div_label_time_header"><label>To Date</label></div>
                    <div class="_table_div_textbox_header _table_div_textbox_time_header">
                        <div class="_div_textbox_header">
                        	<input ng-model="input.toDateTime" 
                        	datetime-picker
                        	date-format="<%=Constants.SEARCH_DATE_FORMAT.toPattern() %>"
                        	close-on-select="false"
                        	class="_textbox_header" 
                        	id="search" 
                        	type="text" 
                        	autocomplete="off" 
                        	maxlength="19"
                        	readonly/>
                        </div>
                    </div>
                    <div class="_table_div_btn_header">
                        <input type="submit" class="_submit_result_header" value="search logs" name="submit" id="submitButton" />
                    </div>
                </div>
            </div> 
        </div>
        <div id="errors" style="display: inline-block;color: crimson;font-size: 13px;width: 350px;padding-top: -15px;position: absolute;"></div>
    </form>
</div>

<div id="metadata" ng-show="noresult == false">{{message}}</div> 
<div id="tabSet" ng-controller="tabbed" ng-show="noresult == false" style="top: 102px;position: absolute; width=100%;">
 <tabset>
     <!-- Dated Result tab Start-->
     <tab heading="Results with date" select="check('dated')">
    	 <div id="dated" ng-controller="ScrollController" ng-init="scroll_datedTab_busy=false" infinite-scroll='doScroll()' infinite-scroll-distance='0.1'>
           <div id="tabrow" ng-repeat="result in model.datedList">
               <div class="_cell_nodename">
                   <span popover="{{result.clientNode}}"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						 {{result.clientNode|ShortNodeName}}
					</span>
               </div>
               <div class="_cell_logfilename">
                   <span popover-template="'file-path.html'"
                         popover-class="popover-file-path"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						 {{result.logFileName|ShortFileName}}
					</span>
               </div>
               <div class="_cell_date">{{result.displayDate}}</div>
               <div class="_cell_text" context-menu="menuOptions"
               			 popover-template="'line-number.html'"
						 popover-placement="left"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter"
						 ng-bind-html="result.logText|RenderAsHtml"> 
               		<!-- {{result.logText|RenderAsHtml}} -->
               	</div>
           </div>
           <div ng-bind="selected"></div>
          </div>
     </tab>
     <tab heading="Results without date" select="check('undated')">
      <div id="undated" ng-controller="ScrollController" ng-init="scroll_unDatedTab_busy=false" infinite-scroll='doScroll()' infinite-scroll-distance='0.1'>
           <div id="tabrow" ng-repeat="result in model.unDatedList">
               <div class="_cell_nodename">
                   <span popover="{{result.clientNode}}"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter">
						 {{result.clientNode|ShortNodeName}}
					</span>
               </div>
               <div class="_cell_logfilename">
                   <span popover-template="'file-path.html'"
                         popover-class="popover-file-path"
						 popover-placement="right"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter"">
						 {{result.logFileName|ShortFileName}}
				  </span>
               </div>
               <div class="_cell_text" context-menu="menuOptions" 
               			 popover-template="'line-number.html'"
						 popover-placement="left"
						 popover-append-to-body="true"
						 popover-trigger="mouseenter"
						 ng-bind-html="result.logText|RenderAsHtml">
						 <!-- {{result.logText|RenderAsHtml}} -->
				</div>
           </div>
           <div ng-bind="selected"></div>
        </div>
     </tab>
     <tab heading="Result Summary">
          <div id="date_frequency_chart" style="width=100%;">
          	<!-- Placeholder Div for Chart -->
          </div>
          
          <div id="node_frequency_chart" style="width=100%;">
          	<!-- Placeholder Div for Chart -->
          </div>
          
           <div id="file_frequency_chart" style="width=100%;">
          	<!-- Placeholder Div for Chart -->
          </div>
      </tab>
     <tab heading="Warnings" select="check('warnings')">
        		<div ng-repeat='(node,messages) in model.nodeMessages'>
	        		<div id="tabrow"  ng-repeat="message in messages" >
	                     <div class="_cell_nodename_full">
	                         <span>{{node}}</span>
	                     </div>
	                     <div class="_cell_text" >
	                         <span>{{message}}</span>
	                     </div>
	                 </div>
                </div>   
        </tab>
    </tabset>
</div>
<div id="errormessages" ng-show="noresult" style="top: 102px;position: absolute; width=100%;">
 <div  ng-show="servererror == false">
  <h2>Your Search <b>{{input.search}}</b> has no result!</h2>
  <div ng-show="model.faultNodeMessages.length == rootScope.model.serversSearched">
  	<div>System encountered following issue while trying to perform search</div>
  	<ul>
	   <li ng-repeat='(key, value) in model.faultNodeMessages'>
	       <span>{{key}} : {{value}}</span>
	   </li>
	</ul>
  </div>
  <div  ng-show="allnodesinerror == false">
  	<div>xyz</div>
  </div>
 </div>
 <div  ng-show="servererror">
 	<h2>Cannot establsih connection with server. HTTP error encountered.</h2>
 </div>
</div>

<script  type="text/ng-template" id="file-path.html">
<div style="font-size:11px;">
  <span>{{result.logFileName}}</span>
</div>
</script>

<script  type="text/ng-template" id="line-number.html">
<div>
 	<div style="font-size:12px;">Line Number : {{result.logLineNumber}}</div>
	<div  style="font-size:10px;">Right click to fetch more lines</div>
</div>
</script>

