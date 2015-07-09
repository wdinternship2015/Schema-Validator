angular.module('evaluator').controller('uploadController',["$scope", "$http","responseService", function($scope, $http, responseService){
  
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
}]);


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


