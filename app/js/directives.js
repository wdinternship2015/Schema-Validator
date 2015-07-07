
//loads the input file from the user's local file system and displays the file's contents onto the screen 
angular.module('evaluator').directive('onReadFile', function ($parse, $window) {
  return {
    restrict: 'A',
    scope: false,
    link: function(scope, element, attrs) {
       //loads new file everytime element changes 
       var fn = $parse(attrs.onReadFile);
       element.on('change', function(onChangeEvent) {
                  var text = document.querySelector('#comment');
                  text.value = "";
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
           scope.$apply(function(){
             scope.inputFile = null;
             scope.textArea = "";
           });
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
           console.log("SHould be nUll");
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
angular.module('evaluator').directive('onReadSchema', function ($parse, $window) {
  return {
    restrict: 'A',
    scope: false,
    link: function(scope, element, attrs) {
       //loads new file everytime element changes 
       var fn = $parse(attrs.onReadSchema);
       element.on('change', function(onChangeEvent) {
           document.getElementById("enter_name").value = "";
           scope.disable= true;
          var text = document.querySelector('#comment');
          text.value = "";
         var reader = new FileReader();
         reader.onload = function(onLoadEvent) {
           scope.$apply(function() {
           fn(scope, {$fileContents:onLoadEvent.target.result});
           });
         };
         //if no file selected disable button for download and clear text and file content area
         if(onChangeEvent.target.files[0] == null){
             scope.$apply(function(){
               scope.schemaFile = null;
               scope.textArea = "";
             });
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
           console.log("Should be null");
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
angular.module('evaluator').directive('downloadFile', function($compile, $window){
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






