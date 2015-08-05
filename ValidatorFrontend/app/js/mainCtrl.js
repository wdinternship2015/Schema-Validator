angular.module('evaluator').controller('uploadController', function($scope, $http, $window,shareDataService, authService, responseService){

	console.log("mainctrl");
console.log("Main Ctrl Local Storage: " + shareDataService.getToken());
 /* 
$window.$localStorage.token = "";
  $scope.token = $window.$localStorage.token;
  console.log("Main Ctrl Local Storage: " + $window.$localStorage.token);
  if($scope.token.length <= 0 ){
      $window.location.href = "http://localhost:8000/app/login.html";
  }
*/

if(shareDataService.getToken() <= 0 ){
    $scope.contentUrl = "login.html";
}
   

  //handle log out
  $scope.logout = function(){
	  console.log("logout");
	  shareDataService.resetToken();
	  $scope.contentUrl = "login.html";
  }

  $scope.showInput = function($fileContent){
    $scope.inputContent = $fileContent;
  }
  $scope.showSchema= function($fileContents){
    $scope.schemaContent = $fileContents;
  };
  $scope.inputFile;
  $scope.schemaFile;
  $scope.inputName ="";
  $scope.direction="TXT to XML";
  $scope.textArea="";
  $scope.disable = true;
  $scope.inputFileName="";
  $scope.schemaName="";
  $scope.displayInput=true;
  $scope.enter_name="";
  //$window.location.href="http://localhost:8000/app/login.html";
  //sends the multipart form to the server, and receives the text data and displays text area 
  $scope.submit = function() {
    //if no input or schema file, do not send to server and clear text area 
    if($scope.inputFile == null || $scope.schemaFile == null){
      $scope.textArea="";
      return;
    }
    //create form to send to server
    var formData = new FormData();
    formData.append("schemaFile", $scope.schemaFile);
    formData.append("inputFile", $scope.inputFile);
    formData.append("direction", $scope.direction);
    responseService.getContent(formData).then(
      function(success){
        $scope.textArea = success.data;
        var text = document.querySelector('#comment');
        text.value = success.data;
        text.style.color="black";
        $scope.disable = false;
        var saveAsName = getSaveAsResultName($scope.inputFileName, $scope.direction);
        $scope.inputName = saveAsName;
        
      }, 
      function(error){
        $scope.textArea = error.data;
        var text = document.querySelector('#comment');
        text.value = error.data;
        text.style.color="red";
        $scope.disable = false;
        $scope.inputName = getSaveAsErrorName($scope.inputFileName, $scope.direction);
    });
   }
  
	  $scope.submit = function(){

			console.log("log in get shared token: " + shareDataService.getToken());
console.log("Username: " + $scope.username);
	   console.log("Password: " + $scope.password);
	   
	   authService.getResponse($scope.username, $scope.password, shareDataService).then(
	     function(success){
	    	 shareDataService.setToken(success.data);
	    	 console.log("success get shared token: " + shareDataService.getToken());
	         $scope.contentUrl = "index.html";
	     }, 
	     function(error){
	    	 console.log("failed to login");
	     }
	   ) 
	   
	  };
});


function getSaveAsResultName(inputFileName, direction) {
	var i = inputFileName.indexOf(".") - 1;
	var name = inputFileName.substring(0, i);
	if (direction.toUpperCase() === "TXT TO XML")  {		
		return  name + "_TextToXML.xml";
	} else if (direction.toUpperCase() === "XML TO TXT") {
		return name + "_XMLToText.txt";
	} else {
		return result;
	}
}

function getSaveAsErrorName(inputFileName, direction) {
	var i = inputFileName.indexOf(".") - 1;
	var name = inputFileName.substring(0, i);
	if (direction.toUpperCase() === "TXT TO XML")  {
		return name + "_TextToXML_error.txt";
	} else if (direction.toUpperCase() === "XML TO TXT") {
		return name + "_XMLToText_error.txt";
	} else {
		return "error.txt";
	}
}


angular.module('evaluator').factory('shareDataService', function($localStorage) {
	 $localStorage.$default({
         token: ""
       });
	 var setToken = function (data) {
		 $localStorage.token = data;
	 };
	 var getToken = function () {
	  return $localStorage.token;
	 };
	 var resetToken = function () {
		 $localStorage.$reset();
	 };
	 
	 return {
	  setToken: setToken,
	  getToken: getToken,
	  resetToken: resetToken,
	  
	 }
	 
});


