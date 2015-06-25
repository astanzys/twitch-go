(ns twitch-go-dashboard.client
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http])
  (:import (java.net URLEncoder)))

(def twitch-headers
  {"Accept" "application/vnd.twitchtv.v3+json"})

(defn do-get [url options]
  (-> url
      (http/get options)
      deref
      :body
      (json/read-str :key-fn keyword)))

(defn format-url [url & vars]
  (apply format url vars))

(defn fetch-channel [name]
  (-> (format-url "https://api.twitch.tv/kraken/channels/%s" name)
      (do-get {:headers twitch-headers})))

(defn fetch-channel-broadcasts [name]
  (-> (format-url "https://api.twitch.tv/kraken/channels/%s/videos" name)
      (do-get {:headers twitch-headers
               :query-params {:broadcasts "true"}})))

(defn fetch-game-streams [game]
  (-> "https://api.twitch.tv/kraken/search/streams"
      (do-get {:headers twitch-headers
               :query-params {:query (URLEncoder/encode game "UTF-8")}})))
