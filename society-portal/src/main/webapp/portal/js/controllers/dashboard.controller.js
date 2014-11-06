app.controller("dashboard", function ($scope, $auth) {
    console.log("dashboard");
    var update = $scope.update = function(){
        $auth.update()
            .success(function(overview){
                $scope.user = overview;
            })
    };
    update();
});