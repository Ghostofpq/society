app.controller("dashboard", function ($scope, $society, $auth, toaster) {
    console.log("dashboard");
    var update = $scope.update = function(){
        $auth.update()
            .success(function(userToken){
                $society.getUser(userToken.principal)
                    .success(function(user){
                        $scope.user = user;
                    })
            })
    };
    update();

    $scope.pop = function(){
        toaster.pop('success', "title", "text");
    };

    $scope.createOrga = function(){
        toaster.pop('success', "ORGA", "text");
    };
});