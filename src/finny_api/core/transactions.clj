(ns finny-api.core.transactions
  (:require [finny-api.db.config]
            [clojure.tools.logging :as log]
            [korma.core :refer :all]))

(defentity transactions)

(defn create-transaction [transaction]
  (let [record (select-keys transaction [:value :comments])]
    (log/debug "Creating transaction with " record)
    (insert transactions
            (values record))
    transaction))

(defn get-transactions-total []
  (log/debug "Getting total of transactions")
  (reduce + (map #(bigdec (get % :value)) (select transactions))))

(defn get-transactions []
  (log/debug "Getting all transactions")
  (select transactions))

(defn delete-transaction [id]
  (log/debug "Deleting transaction with id" id)
  (delete transactions
          (where {:id [= (Integer. id)]})))
