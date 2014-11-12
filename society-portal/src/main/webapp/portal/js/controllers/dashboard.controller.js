app.controller("dashboard", function ($scope, $societyBusiness, $auth) {
    console.log("dashboard");
    var update = $scope.update = function(){
        $auth.update()
            .success(function(userToken){
                $societyBusiness.getUser(userToken.principal)
                    .success(function(user){
                        $scope.user = user;
                    })
            })
    };
    update();
});