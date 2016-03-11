(ns finny-api.core.transactions
  (:require [finny-api.db.transactions :as db]
            [clj-time.format :as date-formatter]
            [clojure.tools.logging :as log]))

(defn create-transaction [transaction]
  (let [transaction-with-formatted-date (update-in transaction [:date] #(date-formatter/parse (date-formatter/formatter "yyyy-MM-dd") %))
        record (select-keys transaction-with-formatted-date [:value :comments :category :date])]
    (log/debug "Creating transaction with" record)
    (db/create-transaction record)
    record))

(defn total-value-of-transactions []
  (log/debug "Getting total of transactions")
  (db/total-value-of-transactions))

(defn get-transaction [id]
  (log/debug "Getting transaction with id" id)
  (or (db/get-transaction id)
      {}))

(defn get-transactions-by-category [category]
  (log/debug "Getting transacations with category" category)
  (db/get-transactions-by-category category))

(defn all-transactions []
  (log/debug "Getting all transactions")
  (db/all-transactions))

(defn update-transaction [id transaction]
  (let [transaction-with-formatted-date (update-in transaction [:date] #(date-formatter/parse (date-formatter/formatter "yyyy-MM-dd") %))
        record (select-keys transaction-with-formatted-date [:value :comments :category :date])]
   (log/debug "Updating transaction with id" id "with" record)
   (db/update-transaction id record)
   record))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (db/delete-transaction id)
  :deleted)
