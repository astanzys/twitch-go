(defproject twitch-go-dashboard "0.1.0-SNAPSHOT"
  :description "twitch-go-app"
  :url "https://github.com/astanzys/twitch-go"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [http-kit "2.1.11"]
                 [org.clojure/data.json "0.2.6"]
                 [liftoff/at-at "1.3.0"]
                 [ring/ring-json "0.3.1"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler twitch-go-dashboard.handler/app
         :init twitch-go-dashboard.handler/init}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
