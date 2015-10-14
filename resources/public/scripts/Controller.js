app.controller("MainController", ['DataService', function(DataService) {
    var vm = this;
    DataService.getStreamers().then(function(response) {
        vm.streamers = response.data;
        vm.streamers.sort(function(s1, s2) {

            if (!s1.created_at && s2.created_at) {
                return 1;
            }
            if (s1.created_at && !s2.created_at) {
                return -1;
            }
            if (s1.created_at === s2.created_at) {
                return 0;}

            var s1Date = new Date(s1.created_at),
                s2Date = new Date(s2.created_at);
            return (s1Date > s2Date) ? -1 : (s2Date > s1Date ? 1 : 0);
        });
    });

    DataService.getVideos().then(function(response) {
        vm.videos = response.data;
    })
}]);