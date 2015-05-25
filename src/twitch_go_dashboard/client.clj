(ns twitch-go-dashboard.client
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]))

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
