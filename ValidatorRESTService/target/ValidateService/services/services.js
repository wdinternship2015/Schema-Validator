
//service for making an http request to send the form data 
//form data contains two input files 
angular.module('evaluator').service('responseService', ['$http', function($http){
  this.getContent = function(formData){
    var response = 
      $http({
        url: "http://localhost:8080/ValidateService/webapi/runSchema",
        data: formData,
        method: 'POST',
        transformResponse: function(data){
          return data;
        },
        headers: {'Content-Type': undefined},
      }).success(function(data, status, headers, config){
        return data;
      }).error(function(data, status, headers, config){
        return data;
      });
    return response;
  };

}]);


//factor for making an http request to validate the username and password entered 
//send the data (username, password) as a JSON object 
//receive JSON object response from REST server 
angular.module('evaluator').factory('authService', ['$http', function($http){
	
	var service = {};
	  service.authenticate = function(username, password){
	    var response = 
	      $http({
	        url: "http://localhost:8080/ValidateService/webapi/login",
	        data: JSON.stringify({"username": username, "password": password}),
	        method: 'POST',
	        transformResponse: function(data){
	            return data;
	          },
	        headers: {'Content-Type': 'application/json'}, 
	        }).success(function(data, status, headers, config){
	        return data;
	      }).error(function(data, status, headers, config){
	        return data; 
	      });
	    return response;
	  };
	  
	  return service;
}]);

