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
});
