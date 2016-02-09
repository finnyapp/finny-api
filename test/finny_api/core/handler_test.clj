(ns finny-api.core.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [finny-api.core.transactions :as transactions]
            [finny-api.core.handler :refer :all]
            [cheshire.core :refer :all]))

(fact "Root is a `Hello, world!` message"
      (let [response (app (mock/request :get "/"))]
        (:status response) => 200
        (get-in (parse-string (:body response) true) [:message]) => "Hello, world!"))

(fact "Gets the total value of transactions"
      (against-background (transactions/get-transactions-total) => 5)
      (let [response (app (mock/request :get "/transactions/total"))]
        (:status response) => 200
        (:total (parse-string (:body response) true)) => 5))

(fact "Gets all transactions"
      (against-background (transactions/get-transactions) => [{:value 1} {:value 2}])
      (let [response (app (mock/request :get "/transactions"))]
        (:status response) => 200
        (:transactions (parse-string (:body response) true)) => [{:value 1} {:value 2}]))

(fact "Gets transaction"
      (against-background (transactions/get-transaction "7") => {:value 14})
      (let [response (app (mock/request :get "/transaction/7"))]
        (:status response) => 200
        (:value (parse-string (:body response) true)) => 14))

(fact "Creates a transaction"
      (against-background (transactions/create-transaction {:value 10}) => {:value 10})
      (let [response (app (mock/content-type
                                 (mock/body
                                   (mock/request :post "/transaction")
                                   (generate-string {:value 10}))
                                 "application/json"))]
        (:status response) => 201
        (:value (parse-string (:body response) true)) => 10))

(fact "Updates a transaction"
      (against-background (transactions/update-transaction "9" {:value 999}) => {:value 999})
      (let [response (app (mock/content-type
                            (mock/body
                              (mock/request :put "/transaction/9")
                              (generate-string {:value 999}))
                            "application/json"))]
        (:status response) => 200
        (:value (parse-string (:body response) true)) => 999))

(fact "Deletes a transaction"
      (against-background (transactions/delete-transaction "21") => {})
      (let [response (app (mock/request :delete "/transaction/21"))]
        (:status response) => 200
        (empty? (get-in response [:body :value])) => true))

(fact "OPTIONS is available"
      (let [response (app (mock/request :options "/"))]
        (:status response) => 200
        (.contains (:body response) "version") => true))

(fact "An invalid route 404s"
      (let [response (app (mock/request :get "/invalid-route"))]
        (:status response) => 404
        (.contains (:body response) "Not Found") => true))

(fact "POSTing to root gives you nothing"
      (let [response (app (mock/request :post "/"))]
        (:status response) => 405))
