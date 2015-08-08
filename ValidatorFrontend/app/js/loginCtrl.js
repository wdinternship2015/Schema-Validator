angular.module('evaluator').controller('LoginCtrl',function($scope, $location, $window, $localStorage, authService){

  console.log("loginCtrl token: " + $scope.parentToken.token);
  $scope.$storage = $localStorage.$default({
    token: ""
  });
  //console.log("get shared token: " + shareDataService.getToken());
  $scope.username="";
  $scope.password="";
  $scope.submit = function(){
   console.log("Username: " + $scope.username);
   console.log("Password: " + $scope.password);

   authService.getResponse($scope.username, $scope.password).then(
     function(success){
         //alert("Response: " + success.data);
         $scope.$storage.token = success.data;
         $scope.parentToken.token = $scope.$storage.token;
         $scope.$parent.pageContent = "partials/mainContent.html";
         console.log('LoginCtrl Parent: ' + $scope.$parent.parentToken.token);
         console.log('LoginCtrl Parent Length: ' + $scope.$parent.parentToken.token.length);
     },
     function(error){
         console.log("failed to login");
     }
   )
  };

});

