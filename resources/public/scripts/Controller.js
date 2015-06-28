app.controller("MainController", ['$scope', 'DataService', function($scope, DataService) {
    DataService.getStreamers().then(function(response) {
        $scope.streamers = response.data;
    });
}]);