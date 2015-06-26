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

/*app.factory('UserFactory', function(){
  return {
    sayHello: function(){
    return "Hello!"; 
  }
 }
});*/
 
app.controller('uploadController',["$scope","UserFactory", "$http", function($scope, UserFactory, $http){
  $scope.showInput = function($fileContent){
    $scope.inputContent = $fileContent; 
  }
  $scope.showSchema= function($fileContents){
    $scope.schemaContent = $fileContents; 
  };
  $scope.inputFile; 
  $scope.schemaFile; 
  /*UserFactory.get(function(track){
    $scope.textArea = track.singer;
 });*/
  $scope.textArea ="";
   /*$http.get('http://localhost:8080/ValidateService/webapi/json/getFile').success(function(data){
    $scope.textArea = data; 
  });*/
  //$scope.textArea = UserFactory.get(); 
  $scope.inputName="";
  //$scope.restResult="";
}]);


//loads the input file  and displays content onto the screen  
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
       if(fileName.search("txt") >= 0 || fileName.search("xml") >=0){
          reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
          scope.inputFile = onChangeEvent.target.files[0]; 
          //console.log(scope.inputFile); 
          //console.log(JSON.stringify(onChangeEvent.target.files[0]));
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

//loads the schema file onto the screen 
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
        $http.post("http://localhost:8080/ValidateService/webapi/json/getForm", formData, {
          transformRequest: angular.identity, 
          withCredentials: true, 
          headers: {'Content-type':undefined} 
        }); 
      });
   
    }
  };


});











//creates file from text on the text area box. 
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

//upload both files to the server wrapped in a form. 
/*app.directive('uploadFile', function($scope, $http){
  return{
    restrict: 'A', 
    scope: false, 
    link: function(scope, element, attrs){
      element.on('click', function(onClickEvent){
      //var formData = new FormData();
      console.log(scope.inputFile); 
      console.log(scope.schemaFile); 
      formData.append('inputFile', scope.inputFile);
      formData.append('schemaFile', scope.schemaFile); 
      $http.post("http://localhost:8080/ValidateService/webapi/json/getForm", formData, {
        transformRequest: angular.identity, 
        withCredentials: true,
        headers: {'Content-Type': undefined}
      }) 
      .success(function(){
      })
      .error(function(){
      });  
    })
   }
 }
});*/



function pingServer(){
        alert("ping server");
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'http://localhost:8080/ValidateService/webapi/testResponse');
        xhr.onload = function() {
                alert('success ' + xhr.responseText);
            //var text = JSON.parse(xhr.responseText);
            //alert('Response from CORS request to ' + url + ': ' + xhr.responseText);
          };
          xhr.onerror = function() {
            alert('Woops, there was an error making the request.');
          };
        xhr.send();
}





