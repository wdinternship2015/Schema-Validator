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
