(ns finny-api.core.handler-test
  (:require [ring.mock.request :as mock]
            [finny-api.core.handler :refer :all]
            [cheshire.core :refer :all])
  (:use midje.sweet))

(fact "OPTIONS is available"
      (let [response (app (mock/request :options "/"))]
        (:status response) => 200
        (.contains (:body response) "version") => true))

(fact "An invalid route 404s"
      (let [response (app (mock/request :get "/invalid-route"))]
        (:status response) => 404))

(fact "Root is a `Hello, world!` message"
      (let [response (app (mock/request :get "/"))]
        (:status response) => 200
        (get-in (parse-string (:body response) true) [:message]) => "Hello, world!"))

(fact "POSTing to root gives you nothing"
      (let [response (app (mock/request :post "/"))]
        (:status response) => 405))
