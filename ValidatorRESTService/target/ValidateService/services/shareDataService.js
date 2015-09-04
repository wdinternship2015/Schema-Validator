 angular.module('evaluator').factory('shareDataService', function() {

	 var schemaOptions;	 
	 var setSchemaOptions = function (data) {
		 schemaOptions = data;
	 };
	 
	 var getSchemaOptions = function () {
	  return schemaOptions;
	 };
 
	 
	 return {
		 setSchemaOptions: setSchemaOptions,
		 getSchemaOptions: getSchemaOptions,
	 }
	 
});