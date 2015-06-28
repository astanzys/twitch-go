(ns twitch-go-dashboard.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [twitch-go-dashboard.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(def sample-response
  {:_total 1,
   :_links
           {:self
            "https://api.twitch.tv/kraken/search/streams?limit=10&offset=0&q=Go+%28Board+Game%29",
            :next
            "https://api.twitch.tv/kraken/search/streams?limit=10&offset=10&q=Go+%28Board+Game%29"},
   :streams
           [{:_id 15023785216,
             :viewers 6,
             :game "Go (Board Game)",
             :video_height 720,
             :channel
             {:_id 59528540,
              :logo
                   "http://static-cdn.jtvnw.net/jtv_user_pictures/adeonce-profile_image-2bb9709e550bd055-300x300.jpeg",
              :partner false,
              :game "Go (Board Game)",
              :broadcaster_language "en",
              :name "adeonce",
              :background nil,
              :profile_banner_background_color nil,
              :profile_banner nil,
              :mature nil,
              :updated_at "2015-06-25T20:56:54Z",
              :status "solve Tsumego with me",
              :language "de",
              :url "http://www.twitch.tv/adeonce",
              :video_banner nil,
              :banner nil,
              :_links
              {:commercial
                        "https://api.twitch.tv/kraken/channels/adeonce/commercial",
               :features
                        "https://api.twitch.tv/kraken/channels/adeonce/features",
               :stream_key
                        "https://api.twitch.tv/kraken/channels/adeonce/stream_key",
               :editors "https://api.twitch.tv/kraken/channels/adeonce/editors",
               :videos "https://api.twitch.tv/kraken/channels/adeonce/videos",
               :follows "https://api.twitch.tv/kraken/channels/adeonce/follows",
               :chat "https://api.twitch.tv/kraken/chat/adeonce",
               :teams "https://api.twitch.tv/kraken/channels/adeonce/teams",
               :subscriptions
                        "https://api.twitch.tv/kraken/channels/adeonce/subscriptions",
               :self "https://api.twitch.tv/kraken/channels/adeonce"},
              :display_name "Adeonce",
              :followers 43,
              :delay nil,
              :created_at "2014-03-23T22:44:28Z",
              :views 530},
             :average_fps 29.9482758621,
             :preview
             {:small
              "http://static-cdn.jtvnw.net/previews-ttv/live_user_adeonce-80x45.jpg",
              :medium
              "http://static-cdn.jtvnw.net/previews-ttv/live_user_adeonce-320x180.jpg",
              :large
              "http://static-cdn.jtvnw.net/previews-ttv/live_user_adeonce-640x360.jpg",
              :template
              "http://static-cdn.jtvnw.net/previews-ttv/live_user_adeonce-{width}x{height}.jpg"},
             :_links {:self "https://api.twitch.tv/kraken/streams/adeonce"},
             :created_at "2015-06-25T20:46:32Z"}]})
