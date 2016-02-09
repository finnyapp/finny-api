(ns finny-api.core.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finny-api.core.transactions :as transactions]
            [finny-api.core.handler :refer :all]
            [cheshire.core :refer :all]))

(defn get-root [] (app (mock/request :get "/")))
(fact "Root is a `Hello, world!` message"
      (:status (get-root)) => 200
      (get-in (parse-string (:body (get-root)) true) [:message]) => "Hello, world!")

(defn get-transactions-total [] (app (mock/request :get "/transactions/total")))
(fact "Gets the total value of transactions"
      (:status (get-transactions-total)) => 200
      (:total (parse-string (:body (get-transactions-total)) true)) => 5
      (provided
        (transactions/get-transactions-total) => 5))

(defn get-transactions [] (app (mock/request :get "/transactions")))
(fact "Gets all transactions"
      (:status (get-transactions)) => 200
      (:transactions (parse-string (:body (get-transactions)) true)) => [{:value 1} {:value 2}]
      (provided
        (transactions/get-transactions) => [{:value 1} {:value 2}]))

(defn get-transaction [] (app (mock/request :get "/transaction/7")))
(fact "Gets transaction"
      (:status (get-transaction)) => 200
      (:value (parse-string (:body (get-transaction)) true)) => 14
      (provided
        (transactions/get-transaction "7") => {:value 14}))

(defn options [] (app (mock/request :options "/")))
(fact "OPTIONS is available"
      (:status (options)) => 200
      (.contains (:body (options)) "version") => true)

(defn get-invalid-route [] (app (mock/request :get "/invalid-route")))
(fact "An invalid route 404s"
      (:status (get-invalid-route)) => 404
      (.contains (:body (get-invalid-route)) "Not Found") => true)

(defn post-to-root [] (app (mock/request :post "/")))
(fact "POSTing to root gives you nothing"
      (:status (post-to-root)) => 405)
