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

(defn fetch-current-streams []
  (->> (do-get "https://api.twitch.tv/kraken/search/streams" {:headers twitch-headers :query-params {:q "Go (Board Game)"}})
       :streams
       (map (fn [stream]
              (let [data {:viewers (:viewers stream)
                          :name (get-in stream [:channel :name])
                          :display_name (get-in stream [:channel :display_name])
                          :created_at (:created_at stream)
                          :url (get-in stream [:channel :url])}]
                [(:name data) data])))
       flatten
       (apply hash-map)))
