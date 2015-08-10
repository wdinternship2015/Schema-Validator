angular.module('evaluator').controller('ModalCtrl', function($scope, $modal, $log, Idle, Keepalive){
           //$scope.items = ['Instruction 1', 'Instruction 2', 'Instruction 3']; 
           $scope.open = function(){
           $scope.$modalInstance = $modal.open({
              scope:$scope,
              templateUrl: 'myModalContent.html',
              controller: 'ModalCtrl'
            });
              $scope.$modalInstance.result.then(function (selectedItem) {
              //$scope.selected = selectedItem;
            }, function () {
              $log.info('Modal dismissed at: ' + new Date());
            });
          };
           $scope.ok = function(){
             $scope.$modalInstance.close();
           };
          $scope.onLoad = function(){
           $scope.$modalInstance = $modal.open({
              scope:$scope,
              templateUrl: 'myModalContent.html',
              controller: 'ModalCtrl'
            });
          };
          
           //logs out of the application 
          $scope.logOut = function(){
             $scope.$parent.parentToken.token = "";
             //console.log("Validate Ctrl Before: " + $scope.$parent.pageContent); 
             $scope.$parent.pageContent = "partials/login.html";
             //console.log("Validate Ctrl After: " + $scope.$parent.pageContent); 
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

        });

