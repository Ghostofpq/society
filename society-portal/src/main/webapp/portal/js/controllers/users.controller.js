app.controller("users", function ($scope, $http, $auth, $society, toaster) {
    console.log("users");
    $scope.pagination= {
		page: 0,
		size: 100
    };

    var selectedIds = $scope.selectedIds = {};
    $scope.search = {};

    var update = $scope.update = function(){
        $society.getUsers($scope.pagination.page,$scope.pagination.size)
            .success(function(res){$scope.results = res;})
            .error(function(err){
            	toaster.pop("error", "Error", "error while listing users");
            });
    }
    $scope.update()
})