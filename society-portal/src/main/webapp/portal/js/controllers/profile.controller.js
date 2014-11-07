app.controller("profile", function ($scope, $auth, $societyBusiness, toaster) {
    console.log("profile");
    var update = $scope.update = function(){
        $auth.update()
            .success(function(user){
                $scope.user = user;
            })
    };
    update();

    // edit fields
    var editDeviceField = function(field){
        return function(value){
            var d = {}; d[field] = value;
            $umDm.updateDevice(deviceId, angular.extend($scope.device, d))
    		    .success(function(){
    		    	toaster.pop("success", field + "Updated", field + "successfully updated");
    		    	update();
    		    })
    		    .error(function(err){
    		    	toaster.pop("error", "Error", "error while updating " + field);
    		    });
        }
    }

    var editLogin = $scope.editLogin = function(login){
        console.log("editLogin");
        $societyBusiness.updateUserLogin($scope.user.id,login)
    	    .success(function(){
    	    	toaster.pop("success", "Login Updated", "Login successfully updated");
    	    	update();
    	    })
    	    .error(function(err){
    	    	toaster.pop("error", "Error", "error while updating login");
    	    });
    }
});