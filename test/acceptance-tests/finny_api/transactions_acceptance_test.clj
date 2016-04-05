(ns finny-api.transactions-acceptance-test
  (:require [finny-api.core.handler :refer [app]]
            [midje.sweet :refer :all]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [finny-api.db.config :refer [db-spec]]
            [clojure.java.jdbc :as j]
            [honeysql.core :as sql]
            [honeysql.helpers :as db-helpers]
            [finny-api.db.transactions :as db]
            [finny-api.transactions-fixtures :refer [small-expense heavy-expense]]))

(def server (atom nil))

(defn start-server []
  (swap! server
         (fn [_] (run-jetty app {:port 3000 :join? false}))))

(defn stop-server []
  (.stop @server))

(defn- run-query [query]
  (j/query db-spec (sql/format query)))

(defn- insert-transaction [transaction]
  (db/create-transaction transaction))

(defn- clear-db []
  (try
    (run-query (-> (db-helpers/delete-from :transactions)))
    (catch Exception e :ok)))

(defn- prepare-db []
  (clear-db)
  (insert-transaction small-expense)
  (insert-transaction heavy-expense))

(def host "http://localhost:3000/")

(def a-brand-new-transaction {:value 123456.0 :comments "This one was just created!"})

(def a-post (fn [] (client/post (str host "transaction")
                                {:content-type :json
                                 :body (json/generate-string a-brand-new-transaction)})))

(defn body-of [response]
  (json/parse-string (:body response) true))

(defn- id-from [response]
  (:id (json/parse-string (:body response) true)))

(defn- get-path [path]
  (client/get (str host path)))

(against-background [(before :contents (start-server))
                     (before :contents (prepare-db))
                     (after  :contents (clear-db))
                     (after  :contents (stop-server))]

  (fact "Gets root" :at
        (let [response (get-path "")]
          (:status response) => 200
          (:message (body-of response)) => "Hello, world!"))

  (fact "Gets the total of all transactions" :at
        (let [response (get-path "transactions/total")]
          (:status response) => 200
          (:total (body-of response)) => (+ (:value small-expense) (:value heavy-expense))))

  (fact "Gets all transactions" :at
        (let [response (get-path "transactions")]
          (:status response) => 200
          (map #(select-keys % [:value :comments]) (:transactions (body-of response)))
            => (vector small-expense heavy-expense)))

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
          (select-keys (body-of response-for-get-transaction) [:value :comments]) => a-brand-new-transaction
          (contains? (set (map #(select-keys % [:value :comments]) (:transactions (body-of response-for-all-transactions)))) a-brand-new-transaction)
            => true))
)
