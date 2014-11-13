app.controller("profile", function ($scope, $http, $auth, $society, toaster) {
    console.log("profile");
    var update = $scope.update = function(){
        $auth.update()
            .success(function(userToken){
                $society.getUser(userToken.principal)
                    .success(function(user){
    	                console.log(user);
                        $scope.user = user;
                    })
     		        .error(function(err){
    	                console.log(err);
     		        	toaster.pop("error", "Error", "error while updating");
     		        });
            })
            .error(function(err){
                console.log(err);
                toaster.pop("error", "Error", "error while $auth.update()");
            });
    };
    update();

    var editLogin = $scope.editLogin = function(login){
        console.log("editLogin");
        $society.updateUserLogin($scope.user.id,login)
    	    .success(function(){
    	        console.log("success");
    	    	toaster.pop("success", "Login Updated", "Login successfully updated");
    	    	update();
    	    })
    	    .error(function(err){
    	        console.log(err);
    	    	toaster.pop("error", "Error", err);
    	    });
    }
});