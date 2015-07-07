angular.module('evaluator').controller('uploadController',["$scope", "$http", function($scope, $http){
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
      alert("clear");
      $scope.textArea="";
      return;
    }
    //create form to send to server
    var formData = new FormData();
    formData.append("schemaFile", $scope.schemaFile);
    formData.append("inputFile", $scope.inputFile);
    formData.append("direction", $scope.direction);
    $http({
       url: "http://localhost:8080/ValidateService/webapi/runSchema/byFileName",
       data: formData,
       method: 'POST',
       transformResponse: function(data){
         return data;
       },
       headers : {'Content-Type' : undefined},
     }).success(function (data, status, headers, config) {
        var extIndex = data.indexOf(".");
        var space = data.indexOf(" ", extIndex);
        var fileName = data.substring(0, space);
        var outText = data.substring(space + 1);
        //alert(fileName + "\n" + outText);
        //display the text response with the correct style   
        //enable button so user can save response in file and choose a file name 
        $scope.textArea = outText;
        var text = document.querySelector('#comment');
        text.value = outText;
        text.style.color="black";
        $scope.disable = false;
        $scope.inputName = "fileName";
        (document.querySelector('#enter_name')).value=fileName;  
     }).error(function (data, status, headers, config) {
        var extIndex = data.indexOf(".");
        var space = data.indexOf(" ", extIndex);
        var fileName = data.substring(0, space);
        var outText = data.substring(space + 1);

        //display the text error response with the correct style
        //disable button so user cannot save error response
        $scope.textArea = outText;
        var text = document.querySelector('#comment');
        text.value = outText;
        text.style.color="red";
        $scope.disable = false;
        $scope.inputName =fileName;
        (document.querySelector('#enter_name')).value=fileName;  
     });
   }
}]);

