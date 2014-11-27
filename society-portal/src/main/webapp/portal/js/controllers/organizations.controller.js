app.controller("organizations", function ($scope, $http, $auth, $society, toaster) {
    console.log("organizations");
    $scope.pagination= {
		page: 0,
		size: 100
    };
    $scope.searchName;
    $scope.searchUser;

    var selectedIds = $scope.selectedIds = {};
    $scope.search = {};

    var update = $scope.update = function(){
        $society.getOrganizations($scope.searchName,$scope.searchUser,$scope.pagination.page,$scope.pagination.size)
            .success(function(res){
                console.log(res);
                $scope.results = res;
            })
            .error(function(err){
            	toaster.pop("error", "Error", "error while listing organizations");
            });
    }
    $scope.update()
});

app.controller("organization-new", function ($scope, $auth, $society, toaster) {
    var owner;
    $auth.update()
        .success(function(userToken){owner = userToken.principal })
        .error(function(err){toaster.pop("error", "Error", "Can't get current user")});
    var addOrganization = $scope.addOrganization = function(){
        console.log("addOrganization");
        console.log($scope.newUser);
        $society.createOrganization($scope.organizationName,$scope.organizationDescription,owner)
    	    .success(function(organization){
                console.log(organization);
    	    })
    	    .error(function(err){
    	        console.log(err);
    	    	toaster.pop("error", "Error", err);
    	    });
    }
});