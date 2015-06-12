var app= angular.module('evaluator', []); 
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
app.controller('uploadController', function($scope){
  $scope.showContent = function($fileContent){
    $scope.content= $fileContent; 
  };
  $scope.display= function($fileContents){
    $scope.schemaContent = $fileContents; 
  };
}); 

app.directive('onReadFile', function ($parse) {
  return {
    restrict: 'A',
    scope: false,
    link: function(scope, element, attrs) {
       var fn = $parse(attrs.onReadFile);     
       element.on('change', function(onChangeEvent) {
         var reader = new FileReader();       
	 reader.onload = function(onLoadEvent) {
           scope.$apply(function() {
	   fn(scope, {$fileContent:onLoadEvent.target.result});
           });
         };
        reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
       console.log((onChangeEvent.target).files[0].type); 
       });
     }
  };
});


app.directive('onReadSchema', function ($parse) {
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
         reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
         console.log((onChangeEvent.target).files[0].type); 
       });
     }
  };
}); 
