app.factory('DataService', ['$http', function($http) {
    return {
        getStreamers: function() {
            return $http.get("api/streamers");
        },

        getVideos: function() {
            return $http.get("api/videos/latest");
        }
    }
}]);