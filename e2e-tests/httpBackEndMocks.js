'use strict';

function passThrough($httpBackend) {
    $httpBackend.whenGET(/^\/scripts\//).passThrough();
};

module.exports.build = function(funcs) {
	var funcStr = "angular.module('httpBackEndMock', ['ngMockE2E'])";

    if (Array.isArray(funcs)) {
    	for (var i = 0; i < funcs.length; i++) {
    		funcStr += "\r.run(" + funcs[i] + ")"
    	};
    } else {
  		funcStr += "\r.run(" + funcs + ")"
    }

    funcStr += "\r.run(" + passThrough + ")";

    var funcTyped = Function(funcStr);

    //console.log(funcTyped.toString())
    return funcTyped;
}


module.exports.serverMsg = function ($httpBackend) {

    var msg = "filename.ext result string from server";
    $httpBackend.whenPOST('http://localhost:8080/ValidateService/webapi/runSchema/byFileName').respond(msg);
};
