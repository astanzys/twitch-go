app.controller("Controller", ['DataService', function(DataService) {
    var vm = this;
    var DATE_FAR_IN_FUTURE = new Date(2100, 0, 0);

    DataService.getStreamers().then(function(response) {
        vm.streamers = response.data;
        vm.streamers.sort(streamersByFreshness);
    });

    DataService.getVideos().then(function(response) {
        vm.videos = response.data;
    });

    function streamersByFreshness(s1, s2) {

        if (!s1.created_at && s2.created_at) {
            return 1;
        }
        if (s1.created_at && !s2.created_at) {
            return -1;
        }
        if (s1.created_at === s2.created_at) {
            return 0;
        }

        var s1Date = s1.live ? DATE_FAR_IN_FUTURE : new Date(s1.created_at),
            s2Date = s2.live ? DATE_FAR_IN_FUTURE : new Date(s2.created_at);
        return (s1Date > s2Date) ? -1 : (s2Date > s1Date ? 1 : 0);
    }
}]);