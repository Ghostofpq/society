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

app.controller("register", function ($scope, $location, $society, toaster) {
    var addUser = $scope.addUser = function(){
        console.log("addUser");
        console.log($scope.newUser);
        $society.addUser($scope.newUser.username,$scope.newUser.password,$scope.newUser.email)
    	    .success(function(user){
                console.log(user);
    	    })
    	    .error(function(err){
    	        console.log(err);
    	    	toaster.pop("error", "Error", err);
    	    });
    }
});