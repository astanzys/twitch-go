(ns twitch-go-dashboard.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [redirect response]]
            [ring.middleware.json :refer [wrap-json-response]]
            [twitch-go-dashboard.service :as service]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn wrap-exception-handling
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (timbre/error e)
        (throw e)))))

(defroutes app-routes
  (GET "/" [] (redirect "index.html"))
  (GET "/api/streamers" [] (response (service/get-streamers)))
  (GET "/api/videos/latest" [] (response (service/get-videos)))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> app-routes
      wrap-json-response
      wrap-exception-handling
      (wrap-defaults site-defaults)))

(defn init! []
  (timbre/merge-config!
    {:appenders {:spit (appenders/spit-appender {:fname "log.txt"})}})
  (service/start-monitoring! 600000))

(defn -main []
  (init!)
  (run-jetty app {:port (if (nil? (System/getenv "PORT"))
                             3000
                             (Integer/parseInt (System/getenv "PORT")))}) )