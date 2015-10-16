angular.module('evaluator').controller('validateCtrl', function($scope, $http, responseService, schemaSVNService, shareDataService){

	
	$scope.inputFile;
	$scope.schemaFile;
	$scope.inputName = "";
	$scope.textArea = "";
	$scope.disable = true;
	$scope.inputFileName = "";
	$scope.displayInput = true;
	$scope.enter_name = "";
	$scope.schemaSources = [{"source":"Local"}, {"source":"SVN"}];

	$scope.schemaFiles = shareDataService.getSchemaOptions();
	console.log("schemafiles: " + JSON.stringify($scope.schemaFiles));
	
	$scope.showInput = function($fileContent){
		$scope.inputContent = $fileContent;
	}
	
	$scope.showSchema= function($fileContents){
		$scope.schemaContent = $fileContents;
	};
  
  //sends the multipart form to the server, and receives the text data and displays text area 
	$scope.submit = function() {
		//if no input or schema file, do not send to server and clear text area 
	    if($scope.inputFile == null){
	    	$scope.textArea="";
	    	return;
	    } else if((! $scope.schemaFile && $scope.schemaSource.source == "Local") || ($scope.schemaSource.source == "SVN" && !$scope.svnSchemaName)){
	    	var text = document.querySelector('#comment');
	        text.value = "Schema File is required";
	        text.style.color="red";
	        return;
	    }
	    //create form to send to server
	    var formData = new FormData();
	    formData.append("schemaSource", $scope.schemaSource.source);
	    formData.append("inputFile", $scope.inputFile);
	    if ($scope.schemaSource.source == "SVN") {
	    	formData.append("schemaFileName", $scope.svnSchemaName.option);
	    	console.log("form add file name" + $scope.svnSchemaName.option);
	    } else {
	    	formData.append("schemaFile", $scope.schemaFile);
	    	console.log("form add local schema file " + $scope.svnSchemaName);
	    }
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
			  }
	    );
   };
   
   $scope.getSchemaFile = function (scope) {   
		document.querySelector('#svnSchema').style.display="none";
		$scope.textArea = "";
		$scope.disable = true;
		$scope.inputName = "";
	   console.log("get schema file " + $scope.svnSchemaName.option);
		schemaSVNService.getSchemaContent($scope.svnSchemaName).then(
			function(success) {
				$scope.svnSchemaContent = success.data;
			//	console.log("schema content: " + $scope.schemaContent);
				document.querySelector('#svnSchema').style.display="block";
				$scope.textArea = "";
				$scope.disable = true;
				$scope.inputName = "";
			},
			function(error) {
		        var text = document.querySelector('#comment');
		        document.querySelector('#svnSchema').style.display="none";
		        text.value = error.data;
		        text.style.color="red";
			}
	);
   }
   
   
	$scope.changeSchemaSource = function (scope) {
		$scope.textArea = "";
		$scope.disable = true;
		$scope.inputName = "";
	}
});


//helper function used in submit function to get suggested output file name
function getSaveAsResultName(inputFileName) {
	var i = inputFileName.indexOf(".");
	var name = inputFileName.substring(0, i);
	if (endsWith(inputFileName, ".txt"))  {		
		return  name + "_TextToXML.xml";
	} else if (endsWith(inputFileName, ".xml")) {
		return name + "_XMLToText.txt";
	} else {
		return result;
	}
}

//helper function used in submit function to get suggested error file name
function getSaveAsErrorName(inputFileName) {
	var i = inputFileName.indexOf(".");
	var name = inputFileName.substring(0, i);
	if (endsWith(inputFileName, ".txt"))  {
		return name + "_TextToXML_error.txt";
	} else if (endsWith(inputFileName, ".xml")) {
		return name + "_XMLToText_error.txt";
	} else {
		return "error.txt";
	}
}

function endsWith(str, suffix) {
    return str.toUpperCase().indexOf(suffix.toUpperCase(), str.length - suffix.length) !== -1;
}
