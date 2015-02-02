(ns finny-api.core.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [finny-api.core.handler :refer :all]
            [cheshire.core :refer :all]))

(deftest test-app
  (testing "api GET"
    (let [response (app (mock/request :get "/api"))]
      (is (= (:status response) 405))
      (is (empty? (:body response)))))
  (testing "api OPTIONS"
    (let [response (app (mock/request :options "/api"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "version"))))
  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))
  (testing "GET /"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (get-in (parse-string (:body response) true) [:message]) "Hello, world!"))))
  (testing "POST /"
    (let [response (app (mock/request :post "/"))]
      (is (= (:status response) 200))
      (is (= "not yet" "done")))))
