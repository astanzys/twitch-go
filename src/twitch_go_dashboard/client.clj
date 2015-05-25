(ns twitch-go-dashboard.client
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]))

(def headers
  {:Accept "application/vnd.twitchtv.v3+json"})

(defn do-get [url]
  (-> url
      (http/get headers)
      deref
      :body
      (json/read-str :key-fn keyword)))

(defn format-url [url & vars]
  (apply format url vars))

(defn fetch-channel [name]
  (-> (format-url "https://api.twitch.tv/kraken/channels/%s" name)
      do-get))
