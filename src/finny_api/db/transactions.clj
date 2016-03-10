(ns finny-api.db.transactions
  (:require [finny-api.db.config]
            [clojure.tools.logging :as log]
            [korma.core :refer :all]))

(defentity transactions)

(defn create-transaction [transaction]
  (let [record (select-keys transaction [:value :comments :category])]
    (log/debug "Creating transaction with" record)
    (insert transactions
            (values record))
    transaction))

(defn total-value-of-transactions []
  (log/debug "Getting total of transactions")
  (reduce + (map #(bigdec (get % :value)) (select transactions))))

(defn get-transaction [id]
  (log/debug "Getting transaction with id" id)
  (first (select transactions
           (where {:id [= (Integer. id)]}))))

(defn all-transactions []
  (log/debug "Getting all transactions")
  (select transactions))

(defn update-transaction [id transaction]
  (let [record (select-keys transaction [:value :comments :category])]
   (log/debug "Updating transaction with id" id "with" record)
   (update transactions
           (set-fields record)
           (where {:id [= (Integer. id)]}))))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (delete transactions
          (where {:id [= (Integer. id)]})))
