var app= angular.module('evaluator', ['ui.bootstrap']); 


 
app.controller('uploadController',["$scope", "$http", function($scope, $http){
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
        //display the text response with the correct style   
        //enable button so user can save response in file and choose a file name 
        $scope.textArea = outText;
        var text = document.querySelector('#comment'); 
        text.style.color="black";
        $scope.disable = false;
        $scope.inputName =fileName;
        (document.querySelector('#enter_name')).value=fileName;  
     }).error(function (data, status, headers, config) {
    	var extIndex = data.indexOf(".");
        var space = data.indexOf(" ", extIndex);
        var fileName = data.substring(0, space);
        var outText = data.substring(space + 1);
        alert(data + "\n msg \n" + outText);
        //display the text error response with the correct style
        //disable button so user cannot save error response
        $scope.textArea = outText;
        var text = document.querySelector('#comment');
        text.style.color="red"; 
        $scope.disable = false;
        $scope.inputName =fileName;
        (document.querySelector('#enter_name')).value=fileName;  
//        document.getElementById("enter_name").value = "";
//        document.getElementById("enter_name").disabled = true;
//        document.getElementById("add_link").disabled = true;
 
     });
   }
}]);


//loads the input file from the user's local file system and displays the file's contents onto the screen 
app.directive('onReadFile', function ($parse, $window) {
  return {
    restrict: 'A',
    scope: false,
    link: function(scope, element, attrs) {
       //loads new file everytime element changes 
       var fn = $parse(attrs.onReadFile);    
       element.on('change', function(onChangeEvent) {
           document.getElementById("enter_name").value = "";
           scope.disable= true;
           var reader = new FileReader();       
           reader.onload = function(onLoadEvent) {
           scope.$apply(function(){
           fn( scope,{$fileContent: onLoadEvent.target.result}); 
           }); 
       };   
       //if no file selected disable button for download and clear text and file content area
       if(onChangeEvent.target.files[0] == null){
           document.querySelector('#input').style.display="none";   
           return;
       } 
       document.querySelector('#input').style.display="block";
       scope.displayInput = false; 
       var fileName = onChangeEvent.target.files[0].name;
       scope.inputFileName = fileName; 
       //check extension of the input file. 
       //if correct extension, display file contents onto screen 
       //if incorrect, then display error message onto text area and do not display on screen 
       if(fileName.search("txt") >= 0 || fileName.search("xml") >=0){
    	  var text = document.querySelector('#comment'); 
    	  text.value = "";
          reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
          scope.inputFile = onChangeEvent.target.files[0];  
          console.log((onChangeEvent.target).files[0].name); 
       } else {
         var text = document.querySelector('#comment'); 
         text.value = "Input file needs to be a txt or xml file!";
         text.style.color="red";
         document.querySelector('#input').style.display="none";
         scope.$apply(function(){
           scope.inputFile = null; 
           scope.textArea = "Input file needs to be a txt or xml file!!"; 
         });
         element.val(null);   
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
       //loads new file everytime element changes 
       var fn = $parse(attrs.onReadSchema);
       element.on('change', function(onChangeEvent) {
           document.getElementById("enter_name").value = "";
           scope.disable= true;
         var reader = new FileReader();
         reader.onload = function(onLoadEvent) {
           scope.$apply(function() {
           fn(scope, {$fileContents:onLoadEvent.target.result});
           });
         };
         //if no file selected disable button for download and clear text and file content area
         if(onChangeEvent.target.files[0] == null){
             document.querySelector('#schema').style.display="none";
             return;
           }        
         document.querySelector('#schema').style.display="block";
         var fileName = onChangeEvent.target.files[0].name;
         scope.schemaName = fileName; 
         //check extension of the input file. 
         //if correct extension, display file contents onto screen 
         //if incorrect, then display error message onto text area and do not display on screen 
         if(fileName.search("xsd") >= 0){ 
       	  var text = document.querySelector('#comment'); 
    	  text.value = "";
          reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
          scope.schemaFile = onChangeEvent.target.files[0]; 
         } else {
          document.querySelector('#schema').style.display="none";
          var text = document.querySelector('#comment'); 
          text.value = "Schema File needs to be an xsd file!";
          text.style.color="red"; 
          scope.$apply(function(){
           scope.schemaFile = null; 
           scope.textArea = "Schema File needs to be an xsd file!"; 
         });
         element.val(null); 
         } 
       });
     }
  };
}); 



//creates file from text on the text area box and saves file onto user's local file system 
//only supported by Chrome and Firefox (download field in attribute tag not supported by IE or safari)
app.directive('downloadFile', function($compile, $window){
  return{
    restrict:'AE',  
    scope: false, 
    link: function(scope, element, attrs){
      var newElement; 
      //create URL with text data and click on link to download file using download attribute 
      element.on('click', function(onClickEvent) {
        var data = new Blob([scope.textArea], {type: 'text/plain'}); 
        var textFile = $window.URL.createObjectURL(data); 
        angular.element(document.querySelector('#add_link')).append($compile(
         '<a id=new_elem download="' + scope.inputName + '"'  + 'href="' + textFile + '">' + '</a>')(scope));
        var elem = document.querySelector('#new_elem'); 
         elem.click();
         delete elem;
         var elem2 = document.querySelector('#new_elem');
         elem2.remove();  
     });  
     }
  }
});



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
    document.getElementById("enter_name").disabled = false;
    document.getElementById("add_link").disabled = false;
    } else {
    var responseJson = JSON.parse(xhr.responseText);   
    document.getElementById("comment").value = responseJson.outText;
    document.getElementById("comment").style.color = "red";    
    document.getElementById("enter_name").value = "";
    document.getElementById("enter_name").disabled = true;
    document.getElementById("add_link").disabled = true;
    }
    };
    xhr.onerror = function() {
        alert('Woops, there was an error making the request.');
    };
    xhr.send(outForm);
}


