(ns finny-api.core.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finny-api.core.transactions :as transactions]
            [finny-api.core.handler :refer :all]
            [cheshire.core :refer :all]))

(defn get-transactions-total [] (app (mock/request :get "/transactions/total")))

(defn options [] (app (mock/request :options "/")))

(defn get-invalid-route [] (app (mock/request :get "/invalid-route")))

(defn get-root [] (app (mock/request :get "/")))

(defn post-to-root [] (app (mock/request :post "/")))

(fact "OPTIONS is available"
      (:status (options)) => 200
      (.contains (:body (options)) "version") => true)

(fact "An invalid route 404s"
      (:status (get-invalid-route)) => 404)

(fact "Root is a `Hello, world!` message"
      (:status (get-root)) => 200
      (get-in (parse-string (:body (get-root)) true) [:message]) => "Hello, world!")

(fact "POSTing to root gives you nothing"
      (:status (post-to-root)) => 405)

(fact "Gets the total value of transactions"
      (:status (get-transactions-total)) => 200
      (:total (parse-string (:body (get-transactions-total)) true)) => 5
      (provided
        (transactions/get-transactions-total) => 5))
