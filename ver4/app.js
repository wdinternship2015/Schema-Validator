var app= angular.module('evaluator', ['ui.bootstrap']); 
/*app.controller('uploadController', ['$scope', '$http', function($scope){
  $scope.uploadFile = function(){
     var f = document.getElementbyId("file1").files[0]
     r = new FileReader(); 
     r.onloadend = function(e){
       var data = e.target.result; 
       
     };
     //result is contents of file in string form
     // can change later to fit as needed 
     var result = r.readAsText(f); 
  } 
]};*/

app.controller('uploadController', function($scope, $window, $compile, $document){
  /*$scope.showInput= function($fileContent){
    $scope.inputContent= $fileContent; 
  };*/
  $scope.showInput = function($fileContent){
    $scope.inputContent = $fileContent; 
  }
  $scope.showSchema= function($fileContents){
    $scope.schemaContent = $fileContents; 
  }; 
  $scope.textArea="";
  $scope.inputName="";
  $scope.checkInput = function(){
    if($scope.showInput == null){
      return true; 
    }
  };
});
/*app.directive('onReadFile', function($parse, $window){
  return {
    restrict:'A',
    scope: false,  
    link: function(scope, element, attrs){
      var fn = $parse(attrs.onReadFile); 
      //var file = (document.querySelector('#file1')).files[0]; 
      
     //var file = element[0].querySelector('#file1').files[0]; 
      var file = $('#file1').files[0]; 
      var fileName = file.name; 
      var reader = new FileReader(); 
      scope.$apply(function(){
        fn(scope, {$fileContent: file}); 
      }); 
      if(fileName.search("txt") >= 0 || fileName.search("xml") >= 0){
        var f = reader.readAsText(file);
        console.log(f); 
       } else {
        $window.alert("Need txt or xml file!");
      }
    }
  }
});*/ 

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
           });
           };  
       var fileName = onChangeEvent.target.files[0].name; 
       if(fileName.search("txt") >= 0 || fileName.search("xml") >=0){
          reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
          console.log((onChangeEvent.target).files[0].name);
       } else {
       //reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
       //console.log((onChangeEvent.target).files[0].name);
         $window.alert("Need txt or xml file!");  
       }
       });
     }
  };
});


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
         if(fileName.search("xsd") >= 0){ 
           reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
           console.log((onChangeEvent.target).files[0].name);
         } else {
          $window.alert("Need an xsd file!"); 
         } 
       });
     }
  };
}); 

app.directive('downloadFile', function($compile, $window){
  return{
    restrict:'AE',  
    scope: false, 
    link: function(scope, element, attrs){
      element.on('click', function(onClickEvent) {
        console.log(scope.textArea);
        var data = new Blob([scope.textArea], {type: 'text/plain'}); 
        console.log(data); 
        var textFile = $window.URL.createObjectURL(data); 
        console.log(textFile); 
        console.log("Input: " + scope.inputName); 
        angular.element(document.querySelector('#add_link')).append($compile(
          '<a id=new_elem download="' + scope.inputName + '"'  + 'href="' + textFile + '">' + '</a>')(scope));
        //$window.location.href = textFile;
        var elem = document.querySelector('#new_elem'); 
        elem.click();   
     });  
       
      
      
     }
 
  }

});


function pingServer() {
	alert("ping server");
	var xhr = new XMLHttpRequest();
	xhr.open('GET', 'http://localhost:8080/ValidateService/webapi/testResponse');
	xhr.onload = function() {
		alert('success ' + xhr.responseText);
	    var text = JSON.parse(xhr.responseText);
	    alert('Response from CORS request to ' + url + ': ' + xhr.responseText);
	  };

	  xhr.onerror = function() {
	    alert('Woops, there was an error making the request.');
	  };
	
	xhr.send();
}

