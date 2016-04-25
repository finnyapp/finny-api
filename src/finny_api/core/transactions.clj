(ns finny-api.core.transactions
  (:require [finny-api.db.transactions :as db]
            [clj-time.format :as date-formatter]
            [clojure.tools.logging :as log]))

(defn create-transaction [transaction]
  (log/debug "Creating transaction with" transaction)
  (let [record (select-keys transaction [:value :comments :category :date :type])]
    (db/create-transaction record)))

(defn total-value-of-transactions []
  (let [transactions (db/get-transactions {})]
    (log/debug "Getting total of transactions")
    (reduce + (map #(cond (= (:type %) "income") (:value %)
                          (= (:type %) "expense") (- (:value %))) transactions))))

(defn get-transaction [id]
  (log/debug "Getting transaction with id" id)
  (or (db/get-transaction id) {}))

(defn get-transactions [query-filter]
  (log/debug "Getting transacations with filter" query-filter)
  (db/get-transactions query-filter))

(defn update-transaction [id transaction]
  (let [record (select-keys transaction [:value :comments :category :date :type])]
   (log/debug "Updating transaction with id" id "with" record)
   (db/update-transaction id record)
   record))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (db/delete-transaction id)
  :deleted)
