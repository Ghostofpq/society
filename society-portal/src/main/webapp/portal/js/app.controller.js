app.controller("main", function ($scope, $location, $http, $auth, toaster) {

	$scope.$location = $location;

	$scope.DATE_FMT = "dd/MM/yyyy HH:mm:ss";

	// TOP messages
	$scope.alert = function (type, title, msg) {
		toaster.pop(type, title, msg);
	}

	$http.get("../")
		.success(function(res){
			$scope.VERSION = res;
		});

    $scope.logout = $auth.logout;
});