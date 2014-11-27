app.controller("modalOrganizationCreation", function ($scope, $modalInstance, creator) {
    $scope.ok = function () {
        var organizationCreationRequest;
        organizationCreationRequest.name = $scope.name;
        organizationCreationRequest.description = $scope.description;
        $modalInstance.close(organizationCreationRequest);
    };
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});