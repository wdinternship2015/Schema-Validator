var app = angular.module('evaluator', ['ui.bootstrap', 'ngStorage', 'ngIdle' ]); 

//idle logout config
app.config(['KeepaliveProvider', 'IdleProvider', function(KeepaliveProvider, IdleProvider) {
	  IdleProvider.idle(10);  //seconds
	  IdleProvider.timeout(5*60); //seconds
//	  KeepaliveProvider.interval(10);
}]);