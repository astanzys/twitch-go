(ns twitch-go-dashboard.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [twitch-go-dashboard.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
