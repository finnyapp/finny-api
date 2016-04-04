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
  (try
    (db/create-transaction transaction)
    (catch Exception e :ok)))

(defn- clear-db []
  (try
    (run-query (-> (db-helpers/delete-from :transactions)))
    (catch Exception e :ok)))

(defn- prepare-db []
  (clear-db)
  (insert-transaction small-expense)
  (insert-transaction heavy-expense))

(against-background [(before :contents (start-server))
                     (before :contents (prepare-db))
                     (after  :contents (clear-db))
                     (after  :contents (stop-server))]

  (fact "Gets root" :at
        (let [response (client/get "http://localhost:3000")]
          (:status response) => 200
          (:message (json/parse-string (:body response) true)) => "Hello, world!"))

  (fact "Gets all transactions" :at
        (let [response (client/get "http://localhost:3000/transactions")]
          (:status response) => 200
          (map #(select-keys % [:value :comments]) (:transactions (json/parse-string (:body response) true)))
            => (vector small-expense heavy-expense))))
