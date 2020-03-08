var adminApp = angular.module('admin-app', [ 'ngRoute', 'ngDialog',	'ui.bootstrap','ngAnimate' ]);

adminApp.config(function($routeProvider) {
			$routeProvider.when('/', {
				templateUrl : '../../jsp/admin/views/help.jsp',
				controller : 'AdminController'
			}).when('/createUser', {
				templateUrl : '../../secure/adduserview.htm',
				controller : 'AddUserController'
			}).when('/editUser', {
				templateUrl : '../../jsp/admin/views/userlist.jsp',
				controller : 'EditUserController'
			}).when('/createGroup', {
				templateUrl : '../../jsp/admin/views/addGroup.jsp',
				controller : 'AdminController'
			}).when('/editGroup', {
				templateUrl : '../../jsp/admin/views/grouplist.jsp',
				controller : 'EditGroupController'
			}).when('/nodeAdmin', {
				templateUrl : '../../jsp/admin/views/nodes.jsp',
				controller : 'AdminController'
			}).when('/message', {
				templateUrl : '../../jsp/admin/views/message.jsp',
				controller : 'AdminController'
			}).otherwise({
				redirectTo : '/'
			});
		})
		.config(
				['$httpProvider',
				  function($httpProvider) {
					$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
				} ]);

adminApp.directive('ngConfirmBoxClick', [ function() {
	return {
		link : function(scope, element, attr) {
			var msg = attr.ngConfirmBoxClick || "Are you sure want to delete?";
			var clickAction = attr.confirmedClick;
			element.bind('click', function(event) {
				if (window.confirm(msg)) {
					scope.$eval(clickAction)
				}
			});
		}
	};
} ]);

adminApp.controller('AdminController', function($scope, $http) {
	/*var httpRequest = $http.post("../../open/createlogin.htm");
	httpRequest.success(function(data, status, headers, config) {
		$scope.users = data;
	});*/
});

adminApp.controller('AddUserController',function($scope, $http, $location, $rootScope) {
			console.log($scope.user);
			$scope.add = function() {
				var data = $scope.user;
				var httpRequest = $http.post("../../secure/adduser.htm", $.param(
						data, true));
				httpRequest.success(function(data, status, headers, config) {
					$rootScope.message = data;
					$location.path("/message").replace();
				});
			};
		});

adminApp.controller('EditUserController', function($scope, $http, $location,ngDialog) {
	var httpRequest = $http.post("../../secure/fetchusers.htm");
	httpRequest.success(function(response) {
		$scope.users = response;
	});
	$scope.editUser = function(index) {
		$scope.user = angular.copy($scope.users[index]);
		$scope.index = index;
		ngDialog.openConfirm({
			template : '../../secure/profile.htm',
			scope : $scope
		}).then(function(value) {
		}, function(value) {
		});
	};
	$scope.deleteUser = function(index) {
		var name = $scope.users[index].username;
		var httpRequest = $http.post("../../secure/delete/" + name + ".htm");
		httpRequest.success(function(response) {
			$scope.users.splice(index, 1);
		});
	};
	$scope.dynamicPopover  = {
			content : '',
			templateUrl : '',
			title : ''
		};
});

adminApp.controller('SaveUserChagesController', function($scope, $http) {
	$scope.saveChanges = function() {
		var payload = angular.copy($scope.user);
		delete payload.createdDate;
		delete payload.modifiedDate;
		var httpRequest = $http.post("../../secure/updateprofile.htm", $.param(payload, true));
		httpRequest.success(function(response) {
			$scope.users[$scope.index] = $scope.user;
			$scope.message = response;
		});
		httpRequest.error(function(data, status) {
			console.error(data, status);
		});
	};
});

adminApp.controller('CreateGroupController', function($scope, $http,$location, $rootScope) {
	$scope.add = function() {
		var httpRequest = $http.post("../../secure/addgroup.htm", $.param(
				$scope.group, true));
		httpRequest.success(function(response) {
			$rootScope.message = response;
			$location.path("/message").replace();
		});
	};
});

adminApp.controller('EditGroupController', function($scope, $http,ngDialog) {
	var httpRequest = $http.post("../../secure/fetchgroups.htm");
	httpRequest.success(function(response) {
		$scope.groups = response;
	});
	$scope.editGroup = function(index) {
		$scope.group = angular.copy($scope.groups[index]);
		$scope.index = index;
		ngDialog.openConfirm({
			template : '../../jsp/admin/views/editGroup.jsp',
			scope : $scope
		}).then(function(value) {
		}, function(value) {
		});
	};
	$scope.deleteGroup = function(index) {
		var gpname = $scope.groups[index].name;
		var httpRequest = $http.post("../../secure/deletegroup/"+gpname+".htm");
		httpRequest.success(function(response) {
			if(response=="SUCCESS"){
			$scope.groups.splice(index, 1);
			}
		});
	};
	$scope.usersexist = function(index) {
		var name = $scope.groups[index].users;
		var len = name.length;
		if(len==0){
			return false;
		}
		return true;
	};
	$scope.dynamicPopover  = {
		content : '',
		templateUrl : 'user-list.html',
		title : 'Description'
	};
});

adminApp.controller('SaveChangesGroupController', function($scope, $http) {
	$scope.savechanges = function() {
		var tempName=$scope.groups[$scope.index].name;
		var payload = angular.copy($scope.group);
		payload.oldName=tempName;
		delete payload.createdDate;
		delete payload.modifiedDate;
		delete payload.users;
		delete payload.modifiedBy;
		var httpRequest = $http.post("../../secure/updategroup.htm", $.param(payload, true));
		httpRequest.success(function(response) {
			$scope.groups[$scope.index] = $scope.group;
			$scope.message = response;
		});
		httpRequest.error(function(data, status) {
			console.error(data, status);
		});
	};
});
adminApp.controller('SaveNodeChangesController', function($scope, $http) {
	$scope.savechanges = function(){
		var payload = angular.copy($scope.node);
		delete payload.lastModifed;
		delete payload.lastHeartbeat;
		var httpRequest = $http.post("../../secure/updatenode.htm", $.param(payload, true));
		httpRequest.success(function(response) {
			$scope.nodes[$scope.index_node] = $scope.node;
			if(response == "FAILED"){
				$scope.nodemessage = "agent update failed";
			}else{
				$scope.nodemessage = response;
			}
		});
		httpRequest.error(function(data, status) {
			console.error(data, status);
		});		
	};
	$scope.removeLabel = function(index){
		var data={"nodeName":$scope.node.nodeName,"labelName":$scope.node.labels[index]};
		data.labelName=	$scope.node.labels[index];
		var httpRequest = $http.post("../../secure/deletenodelabelmap.htm", $.param(data, true)); 
		httpRequest.success(function(response) {
			if(response == "FAILED"){
				$scope.nodemessage = "label deletion failed !";
			}else{
				$scope.node.labels.splice(index,1);
				$scope.nodes[$scope.index_node] = $scope.node;
			}
		});
	};
	$scope.addLabel = function(event){
		if(event.which == 32){
			var data={"nodeName":$scope.node.nodeName,"labelName":$scope.node.newlabel};
			var httpRequest = $http.post("../../secure/addnodelabelmap.htm", $.param(data, true)); 
			httpRequest.success(function(response) {
				if(response == "FAILED"){
					$scope.nodemessage = "label creation failed !";
				}else{
					$scope.node.labels.push($scope.node.newlabel);
					$scope.node.newlabel='';
					$scope.nodes[$scope.index_node] = $scope.node;
				}
			});
		}
	};
	$scope.delteable=function(index){
		var nodeName=$scope.node.nodeName;
		var labelName=$scope.node.labels[index];
		if(nodeName.substring(0,labelName.length)==labelName){
			return false;
		}
		return true;
	};
});
adminApp.controller('NodeAgentController', function($scope, $http, ngDialog) {
	var httpRequest = $http.post("../../secure/nodelist.htm");
	httpRequest.success(function(response) {
		$scope.nodes = response;
	});
	$scope.editnode = function(index) {
		$scope.node = angular.copy($scope.nodes[index]);
		$scope.index_node = index;
		ngDialog.openConfirm({
			template : '../../jsp/admin/views/editnode.jsp',
			scope : $scope
		}).then(function(value) {
		}, function(value) {
		});
	};
	$scope.deletenodedialog = function(index) {
		$scope.nodename =$scope.nodes[index].nodeName;
		$scope.index_node = index;
		$scope.msg_warning = true;
		$scope.msg_confirmation = false;
		$scope.msg_failed= false;
		var dialog = ngDialog.openConfirm({
			template : '../../html/nodedeletewarning.html',
			scope : $scope
		}).then(function(value) {
		}, function(value) {
		});
	};
	$scope.deletenode = function(nodename,index_node) {
		var httpRequest = $http.post("../../secure/remove/"+nodename+".htm"); 
		httpRequest.success(function(response) {
			$scope.msg_warning = false;
			if (response=="NO-NETWORK"){
				$scope.msg_confirmation = true;
				$scope.msg_sub_agentdown = true;
				$scope.nodes.splice(index_node, 1);
			}else if (response=="SUCCESS"){
				$scope.msg_confirmation = true;
				$scope.nodes.splice(index_node, 1);
			}else{
				$scope.msg_failed=true;
				$scope.msg_serverresponse=response;
			}
		});
	};
	$scope.dynamicPopover  = {
			content : '',
			templateUrl : 'node-props.html'
		};
});