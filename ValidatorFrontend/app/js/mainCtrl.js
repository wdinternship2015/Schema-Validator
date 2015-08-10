angular.module('evaluator').controller('mainCtrl', function($scope, $localStorage, $window, authService){
  /*$scope.$storage = $localStorage.$default({
    token: ""
  });*/
  $scope.parentToken = {};
  $scope.parentToken.token = "";
  //$scope.parentToken.token = $scope.$storage.token; 
  //$scope.parentToken.token = "test";
  //if($scope.parentToken.token == "ImAToken"){
  
  $scope.$watch('parentToken.token', function(){

  if($scope.parentToken.token.length > 0){
    //console.log("Length is greater than zero");
    //alert("Navigating to MainContent");
    $scope.pageContent = "partials/mainContent.html";
  } else {
    //alert('Here!');
    //$window.location.href = 'http://localhost:8000/#/index.html';
    $scope.pageContent = "partials/login.html";
  }
 });
 
});
