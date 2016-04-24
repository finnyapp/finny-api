(ns finny-api.test-env
  (:require [finny-api.core.handler :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [finny-api.db.config :refer [db-spec]]
            [clojure.java.jdbc :as j]
            [honeysql.core :as sql]
            [honeysql.helpers :as db-helpers]
            [finny-api.db.transactions :as db]
            [finny-api.transactions-fixtures :refer :all]))

(def server (atom nil))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))

(defn run-query [query]
  (j/query db-spec (sql/format query)))

(defn insert-transaction [transaction]
  (db/create-transaction transaction))

(defn clear-db []
  (try
    (run-query (-> (db-helpers/delete-from :transactions)))
    (catch Exception e :ok)))

(defn prepare-db []
  (clear-db)
  (insert-transaction small-expense)
  (insert-transaction heavy-expense)
  (insert-transaction small-income)
  (insert-transaction large-income))
