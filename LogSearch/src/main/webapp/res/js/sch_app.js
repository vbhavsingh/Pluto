/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var serachApp = angular.module('search-app', [ 'ngRoute', 'ngDialog','ui.bootstrap', 'ngAnimate','infinite-scroll','ui.bootstrap.contextMenu','angularjs-datetime-picker']);

serachApp.config(function($routeProvider) {
			$routeProvider.when('/', {
				templateUrl : 'get_search_form.htm'
			}).when('/search', {
				templateUrl : 'jsp/secure/views/search_result_view.jsp'
			}).when('/download', {
				templateUrl : 'download/download.jsp'
			}).otherwise({
				redirectTo : '/'
			});
		})
		.config(['$httpProvider',
				function($httpProvider) {
					$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
				}]);

serachApp.controller('ScrollController',function($scope,$rootScope,$http,$element,ngDialog){
	$scope.menuOptions =[
	                     ['show more lines from this file',function($itemScope, $event, modelValue, text, $li){
	                    	 var fileLineReadPayload ={
	                    			 nodeName : $itemScope.result.clientNode,	
	                    			 fileName : $itemScope.result.logFileName,
	                    			 startIndex :  $itemScope.result.logLineNumber -10,
	                    			 endIndex :  $itemScope.result.logLineNumber +10,
	                    			 lineNumber : $itemScope.result.logLineNumber,
	                    			 viewTz : $itemScope.input.viewTz
	                    	 };
	                    	 if(fileLineReadPayload.startIndex <=0){
	                    		 fileLineReadPayload.startIndex = 1;
	                    	 }
	                    	$scope.fileLineReadPayload = fileLineReadPayload;
	                 		var dialog = ngDialog.open({
	                			template : 'jsp/secure/views/lines_from_file_read_form.jsp',
	                			controller : 'LineReaderController',
	                			scope : $scope
	                		});
	                     }]];
	$rootScope.input.pageLength= Math.round(screen.height/20);
	$scope.doScroll=function(){
		$rootScope.input.scrollType=$rootScope.currentTab;
		if($rootScope.currentTab == "warnings"){
			return 0;
		}
		if($rootScope.currentTab == "dated"){
			if($scope.scroll_datedTab_busy){
				return 0;
			}
			$scope.scroll_datedTab_busy =true;
			$rootScope.input.paginationIndex=$rootScope.datedTab.length;
			
		}
		if($rootScope.currentTab == "undated"){
			if($scope.scroll_unDatedTab_busy){
				return 0;
			}
			$scope.scroll_unDatedTab_busy =true;
			$rootScope.input.paginationIndex=$rootScope.unDatedTab.length;
		}
		var httpRequest = $http.post("next.htm", $.param($rootScope.input, true));
		httpRequest.success(function(response) {
			if($rootScope.input.scrollType == "dated"){
				$rootScope.datedTab.length +=response.data.length;
				Array.prototype.push.apply($rootScope.model.datedList,response.data);
				$scope.scroll_datedTab_busy = false;
			}else{
				$rootScope.unDatedTab.length +=response.data.length;
				Array.prototype.push.apply($rootScope.model.unDatedList,response.data);
				$scope.scroll_unDatedTab_busy = false;
			}
			//alert($rootScope.currentTab);
		});
		httpRequest.error(function(data, status) {
			//$scope.scroll.busy =false;
		});
	};
});

serachApp.controller('LineReaderController',function($scope,$http,ngDialog){
	//$scope.fileLineReadPayload = $scope.$parent.fileLineReadPayload;
	$scope.fetchlines = function(){
		$scope.hasLinesLoaded=false;
		$scope.lineLoadingErrored = false;
		var newScope = $scope.$new();
		newScope.fileLineReadPayload = $scope.fileLineReadPayload;

		ngDialog.open({
			template : 'jsp/secure/views/line_view.jsp',
			className: 'ngdialog-theme-default line-dialog',
			scope : newScope
		});
		//alert(JSON.stringify(newScope));
		var payload = angular.copy($scope.fileLineReadPayload);
		var httpRequest = $http.post("getlinesfromfile.htm", $.param(payload, true));
		httpRequest.success(function(response) {
			$scope.hasLinesLoaded=true;
			$scope.fileData = response;
		});
		httpRequest.error(function(data, status) {
			$scope.hasLinesLoaded=true;
			$scope.lineLoadingErrored = true;
		});
	}
});

serachApp.controller('SearchController',function($scope, $rootScope, $location) {	
	$rootScope.datedTab={
			length : 0
	};
	
	$rootScope.unDatedTab={
			length : 0
	};
	
	$scope.doSearch = function() {
		$rootScope.input = $scope.input;
		$rootScope.tzOptions = $scope.tzOptions;
		$location.path('/search');
	};

	$scope.getArray = function(jstlString) {
		$scope.input = {}
		var arr = jstlString.replace("[", "").replace("]", "").replace(
				/\s/g, '').split(",");
		var now = new Date().toString();
		var TZ = now.indexOf('(') > -1 ? now.match(/\([^\)]+\)/)[0]
				.match(/[A-Z]/g).join('') : now.match(/[A-Z]{3,4}/)[0];
		if (TZ == "GMT" && /(GMT\W*\d{4})/.test(now))
			TZ = RegExp.$1;
		if (arr.indexOf(TZ) < 0) {
			$scope.input.viewTz = "EST";
		} else {
			$scope.input.viewTz = TZ;
		}
		return arr;
	};
			
});

serachApp.controller('SearchResultController', function($scope, $http,$rootScope, $location) {
	var payload = angular.copy($rootScope.input);
	if(payload == null){
		$location.path('/').replace();
	}
	$rootScope.loading=true;
	$rootScope.noresult=false;
	$rootScope.servererror=false;
	google.charts.load('current', {'packages':['corechart']});
	var httpRequest = $http.post("search.htm", $.param(payload, true));
	httpRequest.success(function(response) {
		$rootScope.loading=false;
		$rootScope.model = response;
		$rootScope.message = $scope.printMessage();
		$rootScope.datedTab.length +=$rootScope.model.datedList.length;
		$rootScope.unDatedTab.length += $rootScope.model.unDatedList.length;
		if($rootScope.model.datedList.length + $rootScope.model.unDatedList.length == 0){
			$rootScope.noresult=true;
		}
		google.charts.setOnLoadCallback(drawChart(response));		
	});
	httpRequest.error(function(data, status) {
		$rootScope.loading=false;
		$rootScope.noresult=true;
		$rootScope.servererror=true;
	});
	
	$scope.doSearch = function() {
		$rootScope.loading=true;
		$rootScope.reload=true;
		var payload = angular.copy($rootScope.input);
		var httpRequest = $http.post("search.htm", $.param(payload, true));
		httpRequest.success(function(response) {
			$rootScope.loading=false;
			$rootScope.datedTab.length = 0;
			$rootScope.unDatedTab.length = 0;
			$rootScope.model = response;
			$rootScope.message = $scope.printMessage();
			if($rootScope.model.datedList.length + $rootScope.model.unDatedList.length == 0){
				$rootScope.noresult=true;
			}else{
				$rootScope.noresult=false;
			}
			google.charts.setOnLoadCallback(drawChart(response));
		});
		httpRequest.error(function(data, status) {
			$rootScope.loading=false;
			$rootScope.noresult=true;
			$rootScope.servererror=true;
		});
	};
	
	$scope.validateTimeRange = function(){
		alert('validating');
	};
	
	$scope.printMessage=function(){
		var model=$rootScope.model;
        var message="";
        if (model.serversSearched == 1) {
            message=message+"One node searched,";
        } else {
            message=message+model.serversSearched;
            message=message+" nodes were searched,";
        }
        if (model.linesFetched == 1) {
            message=message+" only one line found";
            if (model.filesWithMatchCount == 1) {
                message=message+" in one file";
            } else {
                message=message+" in ";
                message=message+model.filesWithMatchCount;
                message=message+" files.";
            }
            if (model.linesFetched == 1 && model.datedTextLines == 0) {
                message=message+" No date found in the line.";
            }
            if (model.linesFetched > 1 && model.datedTextLines == 0) {
                message=message+" All are without date.";
            }
        } else {
            message=message+model.linesFetched;
            message=message+" lines are in result";
            if (model.filesWithMatchCount == 1) {
                message=message+" from one file";
            } else {
                message=message+" from ";
                message=message+model.filesWithMatchCount;
                message=message+" files.";
            }
            if (model.unDatedTextLines > 0) {
                if (model.unDatedTextLines == model.linesFetched){
                	message=message+" All are without date.";
                }
                else if (model.unDatedTextLines == 1) {
                    message=message+"One line is wothout date in result.";
                } else {
                    message=message+model.unDatedTextLines;
                    message=message+" lines are without date.";
                }
            }
        }
        return message;
	};
	
	var drawChart = function(model) {
        // Create daily occurrence chart
        var dataDaily = new google.visualization.DataTable();
        dataDaily.addColumn('date', 'Date');
        dataDaily.addColumn('number', 'Occurrence');
        var j = $.parseJSON(model.graphicsData.dateFrequencyChart);
        $.each(j,function(k,v){
        	dataDaily.addRow([eval(v[0]),v[1]]);
        })
	
        var dataDailyOptions = {
        						title:'Results by Date',
        						colors : ['#b3d1ff'],
        						dataOpacity : 0.8,
        						legend : 'none',
        						width: $(window).width()*0.99,
        						height: $(window).height()*0.25,
        						hAxis : {
        					            format: 'M/d/yy',
        					            gridlines: {color: 'none'},
        					            textStyle : {fontSize: 12},
        					          },
        					    vAxis: {
        					    	   	title:'Occurrences',
        					    	   	minValue: 0,
        					    	   	gridlines: {color: '#b3d1ff'},
        					    	   	textStyle : {fontSize: 12},
        					           }
        						};
        var chartDaily = new google.visualization.ColumnChart(document.getElementById('date_frequency_chart'));
        chartDaily.draw(dataDaily, dataDailyOptions);
        
        //Create occurrence by File chart
        var dataOfFiles = new google.visualization.DataTable();
        dataOfFiles.addColumn('string', 'File');
        dataOfFiles.addColumn('number', 'Occurrence');
        dataOfFiles.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
        var fD = $.parseJSON(model.graphicsData.fileFrequencyChart);
        $.each(fD,function(k,v){
        	var vArr = v[0].split(":");
        	var tooltip = "<div style=\"width:200px;\"><span><b>Node:</b> "+vArr[1]+"</span><br/>";
        	tooltip = tooltip + "<span><b>File:</b> "+ vArr[0]+"</span><br/>"
        	tooltip = tooltip + "<span><b>Occurrence:</b> "+ v[1]+"</span</div>";
        	dataOfFiles.addRow([vArr[0],v[1],tooltip]);
        });
       // dataOfFiles.addRows($.parseJSON($rootScope.model.graphicsData.fileFrequencyChart));
        var dataOfFilesOptions = {
        					   title:'Results by File',
        					   colors : ['#b3d1ff'],
        					   dataOpacity:0.8,
        					   width: $(window).width()*0.99,
        					   height: $(window).height()*0.25,
        					   legend : 'none',
        					   tooltip: {isHtml: true},
        					   hAxis : {
   					            	    gridlines: {color: 'none'},
   					            	    slantedText:true,
   					            	    textStyle : {fontSize: 9},
   					          			},
   					          vAxis: {
   					        	  	  title:'Occurrences',
   					        	  	  minValue: 0,
   					        	  	  gridlines: {color: '#b3d1ff'},
   					        	  	  textStyle : {fontSize: 12},
   					           		}
                       		  };
        var chartNode = new google.visualization.ColumnChart(document.getElementById('file_frequency_chart'));
        chartNode.draw(dataOfFiles, dataOfFilesOptions);	        
       
        //Create occurrence by Node chart
        var dataNode = new google.visualization.DataTable();
        dataNode.addColumn('string', 'Node');
        dataNode.addColumn('number', 'Occurrence');
        dataNode.addRows($.parseJSON(model.graphicsData.nodeFrequencyChart));
        var dataNodeOptions = {
        					   title:'Results by Nodes',
        					   colors : ['#b3d1ff'],
        					   dataOpacity:0.8,
        					   width: $(window).width()*0.99,
        					   height: $(window).height()*0.25,
        					   legend : 'none',
        					   hAxis : {
   					            	    gridlines: {color: 'none'},
   					            	    textStyle : {fontSize: 12},
   					          			},
   					          vAxis: {
   					        	  	  title:'Occurrences',
   					        	  	  minValue: 0,
   					        	  	  gridlines: {color: '#b3d1ff'},
   					        	  	  textStyle : {fontSize: 12},
   					           		}
                       		  };
        var chartNode = new google.visualization.ColumnChart(document.getElementById('node_frequency_chart'));
        chartNode.draw(dataNode, dataNodeOptions);
 };
});

serachApp.controller('tabbed', function($scope,$rootScope) {
	$rootScope.currentTab="dated";
	$scope.check= function(tab){
		$rootScope.currentTab=tab;
	};
});

serachApp.filter('ShortNodeName', function() {
	return function(input) {
		if(input == null){
			return input;
		}
		return input.split('.')[0];
	}
});

serachApp.filter('ShortFileName', function() {
	return function(input) {
		var arr = input.split(/\//);
		if (arr.length > 0) {
			return arr[arr.length - 1];
		}
		return input;
	}
});
serachApp.filter('RenderAsHtml', function($sce) {
	return function(input) {
       return $sce.trustAsHtml(input);
   };
});

serachApp.directive('autoFillHelp',function() {
    return {
    	require: 'ngModel',
        link: function(scope, elem, attrs, ngModel) {
        	var labetStr = attrs.autoFillHelp;
        	var labelList = labetStr.split(",");
        	
        	function split(val) {
        		return val.split(/ \s*/);
        	}
        	function extractLast(term) {
        		return split(term).pop();
        	}

        	elem.bind(
        			"keydown",
        			function(event) {
        				if (event.keyCode === $.ui.keyCode.TAB && $(this).autocomplete("instance").menu.active) {
        					event.preventDefault();
        				}
        			}).autocomplete(
        			{
        				minLength : 0,
        				source : function(request, response) {
        					// delegate back to autocomplete, but extract the last term
        					response($.ui.autocomplete.filter(labelList,extractLast(request.term)));
        				},
        				focus : function() {
        					return false;
        				},
        				select : function(event, ui) {
        					var terms = split(this.value);
        					// remove the current input
        					terms.pop();
        					// add the selected item
        					var label = ui.item.value.split(":",1).toString().trim();
        					// add placeholder to get the comma-and-space at the end
        					terms.push("");
        					if(terms.includes(label) == false){
        						terms.push(label);	
        						this.value = terms.join(" ");
            					terms.push(this.value);
            					ngModel.$setViewValue(this.value);
        					}
        					/*terms.push(label);	
        					this.value = terms.join(" ");
        					terms.push(this.value);
        					ngModel.$setViewValue(this.value);*/
        					return false;
        				}
        			});
        	}
        };
});


