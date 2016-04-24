(ns finny-api.transactions-acceptance-test
  (:require [midje.sweet :refer :all]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [finny-api.test-env :as test-env]
            [finny-api.transactions-fixtures :refer :all]))

(def port 3000)

(def host (str "http://localhost:" port "/"))

(def a-brand-new-transaction {:value 123456.0 :comments "This expense was just created!" :type "expense"})

(def a-post (fn [] (client/post (str host "transaction")
                                {:content-type :json
                                 :body (json/generate-string a-brand-new-transaction)})))

(defn body-of [response]
  (json/parse-string (:body response) true))

(defn- id-from [response]
  (:id (json/parse-string (:body response) true)))

(defn- get-path [path]
  (client/get (str host path)))

(against-background [(before :facts (test-env/start-server port))
                     (before :facts (test-env/prepare-db))
                     (after  :facts (test-env/clear-db))
                     (after  :facts (test-env/stop-server))]

  (fact "Gets options" :at
        (let [response (client/options host)]
          (:status response) => 200
          (:version (body-of response)) => "0.1.0-SNAPSHOT"))

  (fact "Gets root" :at
        (let [response (get-path "")]
          (:status response) => 200
          (:message (body-of response)) => "Hello, world!"))

  (fact "Gets the total of all transactions" :at
        (let [response (get-path "transactions/total")
              income (+ (:value small-income) (:value large-income))
              expense (+ (:value small-expense) (:value heavy-expense))]
          (:status response) => 200
          (:total (body-of response)) => (- income expense)))

  (fact "Gets all transactions" :at
        (let [response (get-path "transactions")]
          (:status response) => 200
          (map #(select-keys % [:value :comments :type]) (:transactions (body-of response)))
            => (vector small-expense heavy-expense small-income large-income)))

  (fact "Updates a transaction" :at
        (let [response-for-create (a-post)
              brand-new-id (id-from response-for-create)
              response-for-update-transaction (client/put (str host "transaction/" brand-new-id)
                                                           {:content-type :json
                                                            :body (json/generate-string {:value (* 3 (:value a-brand-new-transaction))
                                                                                         :comments "Just updated"})})
              response-for-get-transaction (get-path (str "transaction/" brand-new-id))]
          (:status response-for-create) => 201
          (:status response-for-update-transaction) => 200
          (select-keys (body-of response-for-get-transaction) [:value :comments]) => {:value (* 3 (:value a-brand-new-transaction))
                                                                                      :comments "Just updated"}))

  (fact "Creates a transaction and retrieves it by id and from all transactions" :at
        (let [response-for-create (a-post)
              brand-new-id (id-from response-for-create)
              response-for-all-transactions (get-path "transactions")
              response-for-get-transaction (get-path (str "transaction/" brand-new-id))]
          (:status response-for-create) => 201
          (:id (body-of response-for-create)) => brand-new-id
          (select-keys (body-of response-for-get-transaction) [:value :comments :type]) => a-brand-new-transaction
          (contains? (set (map #(select-keys % [:value :comments :type]) (:transactions (body-of response-for-all-transactions)))) a-brand-new-transaction)
            => true))

  (fact "Deletes a transaction" :at
        (let [response-for-create (a-post)
              brand-new-id (id-from response-for-create)
              response-for-delete-transaction (client/delete (str host "transaction/" brand-new-id) {:content-type :json})
              response-for-get-transaction (client/get (str host "transaction/" brand-new-id) {:throw-exceptions false})]
          (:status response-for-create) => 201
          (:status response-for-delete-transaction) => 200
          (:status response-for-get-transaction) => 404))

  (fact "Anything else at root goes nuts" :at
        (let [response (client/delete host {:throw-exceptions false})]
          (:status response) => 405))

  (fact "Anything else other path 404s" :at
        (let [response (client/get (str host "missing-path") {:throw-exceptions false})]
          (:status response) => 404)))
