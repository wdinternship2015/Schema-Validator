angular.module('evaluator').controller('LoginCtrl', ['$scope', '$location', '$state','$window','$localStorage','authService',function($scope, $location, $state, $window, $localStorage,authService){
  //var user={}; 
  $scope.$storage = $localStorage.$default({
    token: ""
  });
  //$localStorage.$reset(); 
  $scope.token = $scope.$storage.token;
  console.log("Main Ctrl Local Storage: " + $localStorage.token);
  if($scope.token.length >0 ){
    //alert($localStorage.token);
    $window.location.href = "http://localhost:8000/app/index.html";
  } else {
    //alert($localStorage.token);
    $window.location.href = "http://localhost:8000/app/login.html";
  }


  

  $scope.username="";
  $scope.password="";
  $scope.submit = function(){
   console.log("Username: " + $scope.username);
   console.log("Password: " + $scope.password);
   authService.getResponse($scope.username, $scope.password).then(
     function(success){
       if(success.data == "ImAToken"){
         /*$state.go('successLogin', {});
         $state.current.name="successLogin";
         console.log($state.current.name);*/
         $scope.$storage.token = success.data;
         //$scope.$localStorage.token = "token";
         //console.log($localStorage.token);
         $window.location.href= "http://localhost:8000/app/index.html";
       } else {
         alert('Failed on Success');
         
       }  
     }, 
     function(error){
        $window.location.href = error.data
       //alert('Failed to login');
       //$state.go('login');
       //$location.path("/login");
     }
   ) 
   
  };

}]);
  var submitForm = function(){
    var form = document.forms.namedItem("userForm");
var data = {};
for (var i = 0, ii = form.length; i < ii; ++i) {
var input = form[i];
if (input.name) {
data[input.name] = input.value;
}
}
var xhr = new XMLHttpRequest();
xhr.open('POST',
"http://localhost:8080/ValidateService/webapi/login",
true);
  xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
xhr.onload = function() {
if (xhr.status == 200) {
var result = xhr.responseText;
document.getElementById("loginResult").value = result;
} else {
var result = xhr.responseText;
document.getElementById("loginResult").value = result;
}
};
xhr.onerror = function() {
alert('Woops, there was an error making the request.');
};
xhr.send(JSON.stringify(data));
} 



 
