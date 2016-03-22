(ns finny-api.db.transactions
  (:require [finny-api.db.config :refer [db-spec]]
            [finny-api.db.query-filters :as query-filters]
            [clojure.tools.logging :as log]
            [clojure.java.jdbc :as j]
            [clj-time.coerce :as coercer]
            [honeysql.core :as h]
            [honeysql.helpers :refer :all]))

(defn- format-date-in [transaction]
  (if (:date transaction)
    (update-in transaction [:date] #(coercer/to-sql-date %))
    transaction))

(defn- run-query [query]
  (j/query db-spec (h/format query)))

(defn create-transaction [transaction]
  (log/debug "Creating transaction with" transaction)
  (let [transaction-with-formatted-date (format-date-in transaction)
        record (select-keys transaction-with-formatted-date [:value :comments :category :date])]
    (let [query (-> (insert-into :transactions)
                    (values [record]))]
      (run-query query))
    transaction))

(defn total-value-of-transactions []
  (log/debug "Getting total of transactions")
  (reduce + (map #(bigdec (get % :value)) (run-query (-> (select :*)
                                                         (from :transactions))))))

(defn get-transaction [id]
  (log/debug "Getting transaction with id" id)
  (first (run-query (-> (select :*)
                        (from :transactions)
                        (where [:= :id (Integer. id)])))))

(defn all-transactions []
  (log/debug "Getting all transactions")
  (run-query (-> (select :*)
                 (from :transactions))))

(defn get-transactions [filters]
  (let [base-query (h/build :select :*
                            :from :transactions)]
    (log/debug "Getting transactions with filters" filters)
    (run-query (merge base-query (query-filters/to-sql filters)))))

(defn update-transaction [id transaction]
  (let [transaction-with-formatted-date (format-date-in transaction)
        record (select-keys transaction-with-formatted-date [:value :comments :category :date])]
    (log/debug "Updating transaction with id" id "with" record)
    (run-query (-> (update :transactions)
                   (sset record)
                   (where [:= :id (Integer. id)])))))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (run-query (-> (delete-from :transactions)
                 (where [:= :id (Integer. id)]))))
