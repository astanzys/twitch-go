(ns twitch-go-dashboard.client
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [taoensso.timbre :as timbre]))

(def twitch-headers
  {"Accept" "application/vnd.twitchtv.v3+json"})

(defn do-get [url options]
  (timbre/debug url)
  (-> url
      (http/get options)
      :body
      (json/read-str :key-fn keyword)))

(defn format-url [url & vars]
  (apply format url vars))

(defn fetch-videos [streamers]
  (let [map-videos (fn [videos]
                     (map (fn [video]
                            {:display_name (get-in video [:channel :display_name])
                             :name (get-in video [:channel :name])
                             :url (:url video)
                             :title (:title video)
                             :recorded_at (:recorded_at video)}) videos))
        only-go-videos (fn [videos]
                         (filter #(= (:game %) "Go (Board Game)") videos))
        fetch-streamer-videos (fn [streamer]
                                (-> (format-url "https://api.twitch.tv/kraken/channels/%s/videos" streamer)
                                    (do-get {:headers twitch-headers :query-params {:broadcasts "true"}})
                                    :videos
                                    only-go-videos
                                    map-videos))]
    (->> streamers
         (map fetch-streamer-videos)
         flatten
         (sort-by :recorded_at)
         reverse)))


(defn fetch-live-streams []
  (->> (do-get "https://api.twitch.tv/kraken/streams"
               {:headers twitch-headers
                :query-params {:game "Go (Board Game)"}})
       :streams
       (map (fn [stream]
              {:viewers (:viewers stream)
               :name (get-in stream [:channel :name])
               :display_name (get-in stream [:channel :display_name])
               :created_at (:created_at stream)
               :url (get-in stream [:channel :url])}))))
