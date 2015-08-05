angular.module('evaluator').controller('LoginCtrl',function($scope, $location, $state, $window, shareDataService, authService){

	console.log("loginctrl");
	console.log("get shared token: " + shareDataService.getToken());
  $scope.username="";
  $scope.password="";
  $scope.submit = function(){
   console.log("Username: " + $scope.username);
   console.log("Password: " + $scope.password);
   
   authService.getResponse($scope.username, $scope.password, shareDataService).then(
     function(success){
      // if(success.data == "ImAToken"){
         /*$state.go('successLogin', {});
         $state.current.name="successLogin";
         console.log($state.current.name);*/
    	 shareDataService.setToken(success.data);
    	 console.log("success get shared token: " + shareDataService.getToken());
         //$scope.$localStorage.token = "token";
         //console.log($localStorage.token);
         $scope.contentUrl = "index.html";
     //  } else {
      //   alert('Failed on Success');
         
     //  }  
     }, 
     function(error){
        //$window.location.href = error.data
       //alert('Failed to login');
       //$state.go('login');
       //$location.path("/login");
    	 console.log("failed to login");
     }
   ) 
   
  };

});


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
};



 
