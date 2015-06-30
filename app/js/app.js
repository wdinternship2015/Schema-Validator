var app= angular.module('evaluator', ['ui.bootstrap', 'ngResource']); 

//retrieves data from the restful service 
app.factory('UserFactory', function($resource){
  return $resource('http://localhost:8080/ValidateService/webapi/json/getFile', {},{
    get:{
       method:'GET',
       params: {}, 
       isArray: false
     }
  })
});

 
app.controller('uploadController',["$scope","UserFactory", "$http", function($scope, UserFactory, $http){
  $scope.showInput = function($fileContent){
    $scope.inputContent = $fileContent; 
  }
  $scope.showSchema= function($fileContents){
    $scope.schemaContent = $fileContents; 
  };
  $scope.inputFile; 
  $scope.schemaFile; 
  $scope.inputName ="";
  $scope.direction; 
  $scope.textArea="";
  $scope.disable = true;
  $scope.inputFileName="";
  $scoepe.schemaName="";
  //sends the multipart form to the server, and receives the text data and puts into the text area 
  $scope.submit = function() {
    var formData = new FormData();
    formData.append("schemaFile", $scope.schemaFile);
    formData.append("inputFile", $scope.inputFile);
    formData.append("direction", $scope.direction);
    $http({
       url: "http://localhost:8080/ValidateService/webapi/runSchema/byFileName",
       data: formData,
       method: 'POST',
       transformResponse: function(data){
         //$scope.textArea = data;
         //return JSON.parse(data);
         return data;
       }, 
       headers : {'Content-Type' : undefined},
     }).success(function (data, status, headers, config) {
        //alert("success - status(" + status + ")");
        var space = data.indexOf(" ");
        var fileName = data.substring(0, space);
        var outText = data.substring(space + 1); 
        $scope.textArea = outText;
        var text = document.querySelector('#comment'); 
        text.style.color="black";
        $scope.disable = false;
        $scope.inputName =fileName;
        (document.querySelector('#enter_name')).value=fileName;  
     }).error(function (data, status, headers, config) {
        //alert("error - status(" + status + ")");
        //alert("Validation Failed!"); 
        var space = data.indexOf(" ");
        var fileName = data.substring(0, space);
        var outText = data.substring(space + 1);
        $scope.textArea = outText;
        var text = document.querySelector('#comment');
        text.style.color="red"; 
        $scope.disable = true;
        (document.querySelector('#enter_name')).value="";        
        $scope.inputName="";
 
     });
   }
}]);


//loads the input file from the user's local file system and displays the file's contents onto the screen 
app.directive('onReadFile', function ($parse, $window) {
  return {
    restrict: 'A',
    scope: false,
    link: function(scope, element, attrs) {
       var fn = $parse(attrs.onReadFile);    
       element.on('change', function(onChangeEvent) {
         var reader = new FileReader();       
	 reader.onload = function(onLoadEvent) {
           scope.$apply(function(){
           fn( scope,{$fileContent: onLoadEvent.target.result}); 
           //fn(scope, {inputFile: onLoadEvent.target.files[0]}); 
           }); 
       };       
       var fileName = onChangeEvent.target.files[0].name; 
       scope.inputFileName = fileName; 
       if(fileName.search("txt") >= 0 || fileName.search("xml") >=0){
          reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
          scope.inputFile = onChangeEvent.target.files[0];  
          console.log((onChangeEvent.target).files[0].name); 
       } else {
         $window.alert("Need txt or xml file!");  
       }
       });
     }
  };
});

//loads the schema file from user's local file system and displays file's contents onto the screen  
app.directive('onReadSchema', function ($parse, $window) {
  return {
    restrict: 'A',
    scope: false,
    link: function(scope, element, attrs) {
       var fn = $parse(attrs.onReadSchema);
       element.on('change', function(onChangeEvent) {
         var reader = new FileReader();
         reader.onload = function(onLoadEvent) {
           scope.$apply(function() {
           fn(scope, {$fileContents:onLoadEvent.target.result});
           });
         };
         var fileName = onChangeEvent.target.files[0].name;
         scope.schemaName = fileName; 
         if(fileName.search("xsd") >= 0){ 
           reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
           scope.schemaFile = onChangeEvent.target.files[0]; 
           console.log((onChangeEvent.target).files[0].name);
         } else {
          $window.alert("Need an xsd file!"); 
         } 
       });
     }
  };
}); 



//upload both files to the server wrapped in a form. 
app.directive('sendFile',function($http){
  return{
    restrict: 'A',
    scope: false, 
    link:function(scope, element, attrs){
      element.on('click', function(onClickEvent){
        console.log(scope.inputFile);
        console.log(scope.schemaFile);
        var formData = new FormData();
        formData.append("schemaFile", scope.schemaFile);
        formData.append("inputFile", scope.inputFile);
        formData.append("direction", "text to xml"); 
        $http.post("http://localhost:8080/ValidateService/webapi/runSchema/byFileName", formData, {
          transformRequest: angular.identity, 
          withCredentials: true, 
          headers: {'Content-type':undefined} 
        });
        $http.post('http://localhost:8080/ValidateService/webapi/runSchema/byFileName').success(function(data){
    $scope.textArea = data; 
  }); 
      });
      
    }
  };


});

//creates file from text on the text area box and saves file onto user's local file system 
app.directive('downloadFile', function($compile, $window){
  return{
    restrict:'AE',  
    scope: false, 
    link: function(scope, element, attrs){
      var newElement; 
      element.on('click', function(onClickEvent) {
        console.log(scope.textArea);
        var data = new Blob([scope.textArea], {type: 'text/plain'}); 
        console.log("Blob: " + data); 
        var textFile = $window.URL.createObjectURL(data); 
        console.log("URL: " + textFile); 
        console.log("Input: " + scope.inputName); 
        angular.element(document.querySelector('#add_link')).append($compile(
         '<a id=new_elem download="' + scope.inputName + '"'  + 'href="' + textFile + '">' + '</a>')(scope));
        //$window.location.href = textFile;
        var elem = document.querySelector('#new_elem'); 
         elem.click();
         delete elem;
         var elem2 = document.querySelector('#new_elem');
         elem2.remove();  
         //elem.remove(); 
         //elem.remove(); 
         //$(".save").remove(); 
         //elem.remove();
         //element.removeAttr('new_elem'); 
        //elem.removeAttr("href"); 
        //$window.URL.revokeObjectURL(textFile);   
     });  
      
      
     }
 
  }

});

//checks the direction of the input file. 
/*app.directive('checkDirection', function(){
  return{
    restrict: 'A',
    scope: false, 
    link: function(scope, element, attrs){
      var input = scope.inputFileName; 
      var schema = scope.schemaName; 
      
    });
  }; 

});*/




function scriptSubmit(){
var form = document.forms.namedItem("validationForm");
var outForm = new FormData(form);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/ValidateService/webapi/runSchema/byFileName', true);
    xhr.onload = function() {
    if (xhr.status == 200) {
    var responseJson = JSON.parse(xhr.responseText);
    document.getElementById("comment").value = responseJson.outText;
    document.getElementById("comment").style.color = "black";
    document.getElementById("enter_name").value = responseJson.fileName;
    document.getElementById("add_link").disabled = false;
    } else {
    var responseJson = JSON.parse(xhr.responseText);   
    document.getElementById("comment").value = responseJson.outText;
    document.getElementById("comment").style.color = "red";    
    document.getElementById("enter_name").value = "";
    document.getElementById("add_link").disabled = true;
    }
    };
    xhr.onerror = function() {
        alert('Woops, there was an error making the request.');
    };
    xhr.send(outForm);
}


