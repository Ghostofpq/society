app.controller("main", function ($scope, $location, $http, $auth) {

	$scope.$location = $location;

	$scope.DATE_FMT = "dd/MM/yyyy HH:mm:ss";

	$http.get("../")
		.success(function(res){
			$scope.VERSION = res;
		});

    $scope.logout = $auth.logout;
});