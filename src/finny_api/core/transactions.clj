(ns finny-api.core.transactions
  (:require [finny-api.db.transactions :as db]
            [clojure.tools.logging :as log]))

(defn create-transaction [transaction]
  (let [record (select-keys transaction [:value :comments :category])]
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

(defn all-transactions []
  (log/debug "Getting all transactions")
  (db/all-transactions))

(defn update-transaction [id transaction]
  (let [record (select-keys transaction [:value :comments :category])]
   (log/debug "Updating transaction with id" id "with" record)
   (db/update-transaction id transaction)
   transaction))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (db/delete-transaction id)
  :deleted)
