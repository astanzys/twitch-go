(ns twitch-go-dashboard.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [redirect response]]
            [overtone.at-at :as schedule]
            [clojure.edn :as edn]
            [ring.middleware.json :refer [wrap-json-response]]
            [twitch-go-dashboard.client :as twitch-client]))

(defn fetch-data []
  (edn/read-string (slurp "data.txt")))

(def data (atom (fetch-data)))

(defroutes app-routes
  (GET "/" [] (redirect "index.html"))
  (GET "/api/streamers" [] (response (:streamers @data)))
  (GET "/api/videos/latest" [] (response (:videos @data)))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-defaults site-defaults)))

(defn merge-data [current-data new-data]
  {:streamers (merge (:streamers current-data) (:streamers new-data))
   :videos (:videos new-data)})

(defn update-data! []
  (let [current-streamers (twitch-client/fetch-current-streams)
        latest-videos (->> @data
                           :streamers
                           keys
                           twitch-client/fetch-videos
                           (take 20))
        new-data {:streamers current-streamers :videos latest-videos}]
    (println (str "Currently streaming: " current-streamers))
    (swap! data merge-data new-data)
    (println "updating data on file...")
    (spit "data.txt" (prn-str @data))))

(defn init! []
  (println "Initializing")
  (let [pool (schedule/mk-pool)]
    (schedule/every 600000
                    update-data!
                    pool)))