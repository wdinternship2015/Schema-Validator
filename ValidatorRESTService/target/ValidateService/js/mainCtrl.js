angular.module('evaluator').controller('mainCtrl', function($scope, $localStorage, $window, authService, $modal,Idle, Keepalive, shareDataService){
	$scope.$storage = $localStorage.$default({
		token : "",
		schemaOptions: []
	});
	shareDataService.setSchemaOptions($scope.$storage.schemaOptions);
	console.log("schemafiles: " + JSON.stringify($scope.$storage.schemaOptions));
	if($scope.$storage.token.length > 0){
	    $scope.pageContent = "partials/mainContent.html";
	  } else {
	    $scope.pageContent = "partials/login.html";
	  }
	
	  $scope.login = function(username, password) {
			authService.authenticate(username, password).then(
				function(success) {
					$scope.$storage.token = "success";
					var obj = JSON.parse(success.data);
					$scope.$storage.schemaOptions = obj.options;
					shareDataService.setSchemaOptions($scope.$storage.schemaOptions);
					console.log("schemafiles: " + JSON.stringify($scope.$storage.schemaOptions));					
					$scope.pageContent = "partials/mainContent.html";
				},
				
				function(error) {
					console.log("failed to login");
					$scope.loginFail = true;
					$scope.loginFailMsg = "Login failed";
				})
		};
  
		//logs out of the application 
		$scope.logOut = function(){
			$scope.$storage.token = ""; 
			$scope.$storage.schemaOptions = {};
			$scope.pageContent = "partials/login.html";
		}
 
	// idle logout functions
	function closeModals() {
		if ($scope.warning) {
			$scope.warning.close();
			$scope.warning = null;
		}
		if ($scope.timedout) {
			$scope.timedout.close();
			$scope.timedout = null;
		}
	}
	$scope.$on('IdleStart', function() {
		closeModals();
		/*
		 * $scope.warning = $modal.open({ templateUrl :
		 * 'warning-dialog.html', windowClass : 'modal-danger'
		 * });
		 */
	});
	$scope.$on('IdleEnd', function() {
		closeModals();
	});
	$scope.$on('IdleTimeout', function() {
		console.log("idle timeout");
		closeModals();
		$scope.logOut();
		$scope.timedout = $modal.open({
			templateUrl : 'timedout-dialog.html',
			windowClass : 'modal-danger'
		});
	});
	//end idle logout functions

	$scope.clearLoginError = function() {
		$scope.loginFail = false;
		$scope.loginFailMsg = "";
	}


});

 

