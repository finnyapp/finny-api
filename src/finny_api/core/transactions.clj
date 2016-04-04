(ns finny-api.core.transactions
  (:require [finny-api.db.transactions :as db]
            [clj-time.format :as date-formatter]
            [clojure.tools.logging :as log]))

(defn- format-date-in [transaction]
  (if (:date transaction)
    (update-in transaction [:date] #(date-formatter/parse (date-formatter/formatter "yyyy-MM-dd") %))
    transaction))

(defn create-transaction [transaction]
  (log/debug "Creating transaction with" transaction)
  (let [transaction-with-formatted-date (format-date-in transaction)
        record (select-keys transaction-with-formatted-date [:value :comments :category :date])]
    (db/create-transaction record)))

(defn total-value-of-transactions []
  (log/debug "Getting total of transactions")
  (db/total-value-of-transactions))

(defn get-transaction [id]
  (log/debug "Getting transaction with id" id)
  (or (db/get-transaction id) {}))

(defn get-transactions [query-filter]
  (log/debug "Getting transacations with filter" query-filter)
  (db/get-transactions query-filter))

(defn update-transaction [id transaction]
  (let [transaction-with-formatted-date (format-date-in transaction)
        record (select-keys transaction-with-formatted-date [:value :comments :category :date])]
   (log/debug "Updating transaction with id" id "with" record)
   (db/update-transaction id record)
   record))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (db/delete-transaction id)
  :deleted)
