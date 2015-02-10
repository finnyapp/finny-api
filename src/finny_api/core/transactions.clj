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
