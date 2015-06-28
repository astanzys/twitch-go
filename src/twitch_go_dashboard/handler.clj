(ns twitch-go-dashboard.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [redirect response]]
            [overtone.at-at :as schedule]
            [clojure.edn :as edn]
            [ring.middleware.json :refer [wrap-json-response]]
            [twitch-go-dashboard.client :as twitch-client]))

(def streamers (atom {}))

(def thread-pool (schedule/mk-pool))

(defroutes app-routes
  (GET "/" [] (redirect "index.html"))
  (GET "/api/streamers" [] (response @streamers))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-defaults site-defaults)))

(defn fetch-streamers-from-storage []
  (edn/read-string (slurp "data.txt")))

(defn update-and-cache []
  (let [current-streamers (twitch-client/fetch-current-streams)]
    (println (str "Currently streaming: " current-streamers))
    (if-not (empty? current-streamers)
      (swap! streamers merge current-streamers))
    (println "updating data on file...")
    (spit "data.txt" (prn-str @streamers))))

(defn init []
  (println "Initializing")
  (let [streamer-set (fetch-streamers-from-storage)]
    (reset! streamers streamer-set)
    (schedule/every 600000
                    update-and-cache
                    thread-pool)))