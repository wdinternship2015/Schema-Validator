var services = angular.module('services', ['ngResource']); 

services.factory('UserFactory', function($resource){
  return $resource('/ValidateService/webapi/myresource', {},{
    query:{
       method:'GET',
       params: {}, 
       isArray: false
     }
  })
}); 
