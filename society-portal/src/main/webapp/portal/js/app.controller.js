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

    $scope.hasRole = $auth.hasRole;
});

app.controller("register", function ($scope, $location,$window, $society, $auth, toaster) {
    var addUser = $scope.addUser = function(){
        var username = $scope.username;
        var password = $scope.password;
        var repassword = $scope.repassword;
        var email = $scope.email;

        console.log("addUser");
        console.log($scope.newUser);
        $society.addUser(username,password,email)
    	    .success(function(user){
                console.log(user);
                $auth.login(username,password)
                    .success(function(){
                        console.log("OK");
                        $window.location.href ="/portal/index.html";
                    })
                    .error(function(err){
                        console.log(err);
                    })
    	    })
    	    .error(function(err){
    	        console.log(err);
    	    	toaster.pop("error", "Error", err);
    	    });
    }

    var cancel = $scope.cancel = function(){
        $window.location.href ="/portal/login.html";
    }

});