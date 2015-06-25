(ns twitch-go-dashboard.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [redirect]]))

(defroutes app-routes
  (GET "/" [] (redirect "index.html"))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))


(def app
  (wrap-defaults app-routes site-defaults))
