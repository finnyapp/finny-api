(ns finny-api.db.transactions
  (:require [finny-api.db.config :refer [db-spec]]
            [clojure.tools.logging :as log]
            [clojure.java.jdbc :as j]
            [honeysql.core :as h]
            [honeysql.helpers :refer :all]))

(defn- run-query [query]
  (j/query db-spec (h/format query)))

(defn create-transaction [transaction]
  (let [record (select-keys transaction [:value :comments :category])]
    (log/debug "Creating transaction with" record)
    (let [query (-> (insert-into :transactions)
                   (values [record]))]
      (log/debug "Running query" query)
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

(defn get-transactions-by-category [category]
  (run-query (-> (select :*)
                 (from :transactions)
                 (where [:= :category category]))))

(defn all-transactions []
  (log/debug "Getting all transactions")
  (run-query (-> (select :*)
                 (from :transactions))))

(defn update-transaction [id transaction]
  (let [record (select-keys transaction [:value :comments :category])]
    (log/debug "Updating transaction with id" id "with" record)
    (run-query (-> (update :transactions)
                   (sset record)
                   (where [:= :id (Integer. id)])))))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (run-query (-> (delete-from :transactions)
                 (where [:= :id (Integer. id)]))))
