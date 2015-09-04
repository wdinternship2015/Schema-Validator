angular.module('evaluator').factory('schemaSVNService', ['$http', function($http){
	
	var service = {};
	  service.getSchemas = function(){
	    var response = 
	      $http({
	        url: "http://localhost:8080/ValidateService/webapi/schemas/options",
	        method: 'GET',
	        transformResponse: function(data){
	            return data;
	          },
//	        headers: {'Content-Type': 'application/json'}, 
	        }).success(function(data, status, headers, config){
	        return data;
	      }).error(function(data, status, headers, config){
	        return data; 
	      });
	    return response;
	  };
	  
	  service.getSchemaContent = function(fileName){
		  console.log("schemaSVNService fileName: " + fileName);
		    var response = 
		      $http({
		        url: "http://localhost:8080/ValidateService/webapi/schemas/getSchema",
		        params: {fileName: fileName},
		        method: 'GET',
		        transformResponse: function(data){
		            return data;
		          },
//		        headers: {'Content-Type': 'application/json'}, 
		        }).success(function(data, status, headers, config){
		        return data;
		      }).error(function(data, status, headers, config){
		        return data; 
		      });
		    return response;
	};
		  
	  return service;
}]);