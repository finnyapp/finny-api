(ns finny-api.incomes-expenses-acceptance-test
  (:require [midje.sweet :refer :all]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [finny-api.test-env :as test-env]
            [finny-api.transactions-fixtures :refer :all]))

(def port 3001)

(def host (str "http://localhost:" port "/"))

(def a-brand-new-transaction {:value 123456.0 :comments "This income was just created!" :type "income"})

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

  (fact "Creates an income transaction and retrieves it by id and from all transactions" :at
        (let [response-for-create (a-post)
              brand-new-id (id-from response-for-create)
              response-for-all-transactions (get-path "transactions")
              response-for-get-transaction (get-path (str "transaction/" brand-new-id))]
          (:status response-for-create) => 201
          (:id (body-of response-for-create)) => brand-new-id
          (select-keys (body-of response-for-get-transaction) [:value :comments :type]) => a-brand-new-transaction
          (contains? (set (map #(select-keys % [:value :comments :type]) (:transactions (body-of response-for-all-transactions)))) a-brand-new-transaction)
            => true))
)
