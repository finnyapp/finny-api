(ns finny-api.db.transactions
  (:require [finny-api.db.config :refer [db-spec]]
            [finny-api.db.query-filters :as query-filters]
            [clojure.java.jdbc :as j]
            [clj-time.coerce :as coercer]
            [honeysql.core :as h]
            [honeysql.helpers :refer :all]))

(def postgresql-undesired-exception-messaged "No results were returned by the query.")

(defn- format-date-in [transaction]
  (if (:date transaction)
    (update-in transaction [:date] #(coercer/to-sql-date %))
    transaction))

(defn- insert [transaction]
  (j/insert! db-spec :transactions transaction))

(defn- run-query [query]
  (try
    (j/query db-spec (h/format query))
    (catch Exception e
      (if (= (.getMessage e) postgresql-undesired-exception-messaged)
        :ok
        (throw e)))))

(defn create-transaction [transaction]
  (let [transaction-with-formatted-date (format-date-in transaction)
        record (insert transaction-with-formatted-date)]
    (assoc transaction :id (:id (first record)))))

(defn get-transaction [id]
  (first (run-query (-> (select :*)
                        (from :transactions)
                        (where [:= :id (Integer. id)])))))

(defn all-transactions []
  (run-query (-> (select :*)
                 (from :transactions))))

(defn get-transactions [filters]
  (let [base-query (h/build :select :*
                            :from :transactions)]
    (run-query (merge base-query (query-filters/to-sql filters)))))

(defn update-transaction [id transaction]
  (let [transaction-with-formatted-date (format-date-in transaction)]
    (run-query (-> (update :transactions)
                   (sset transaction-with-formatted-date)
                   (where [:= :id (Integer. id)])))))

(defn delete-transaction [id]
  (run-query (-> (delete-from :transactions)
                 (where [:= :id (Integer. id)]))))
