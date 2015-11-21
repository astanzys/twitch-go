(ns twitch-go-dashboard.service
  (:require [overtone.at-at :as scheduler]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre]
            [twitch-go-dashboard.client :as twitch-client]
            [clojure.edn :as edn]))

(defn fetch-data []
  (edn/read-string (slurp "data.txt")))

(def ^{:private true} data
  (atom (fetch-data)))

(defn- with-logging [f]
  (fn []
    (try
      (f)
      (catch Exception e
        (timbre/error e)))))

(defn- map-by-name [streams]
  (->> streams
       (map #(vector (:name %) %))
       flatten
       (apply hash-map)))

(defn update-data! []
  (let [old-data @data
        current-streamers (->> (twitch-client/fetch-live-streams)
                               (map #(assoc % :live true)))

        old-streamers (->> old-data
                           :streamers
                           (map #(assoc % :live false)))

        all-streamers (vals                          ; temporarily convert streamer lists into maps for an easy merge
                        (merge
                          (map-by-name old-streamers)
                          (map-by-name current-streamers)))

        latest-videos (->> all-streamers
                           (map :name)
                           twitch-client/fetch-videos
                           (take 30))

        new-data {:streamers all-streamers
                  :videos latest-videos}]

    (timbre/debug (str "Currently streaming: " (pr-str current-streamers)))
    (reset! data new-data)
    (spit "data.txt" (prn-str @data))))

(defn start-monitoring! [interval]
  (if-not (.exists (io/as-file "data.txt"))
    (spit "data.txt" {:streamers [] :videos []}))
  (let [pool (scheduler/mk-pool)]
    (scheduler/every interval
                     (with-logging update-data!)
                     pool)))

(defn get-streamers []
  (:streamers @data))

(defn get-videos []
  (:videos @data))


