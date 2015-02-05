(ns finny-api.core.transactions
  (:require [clojure.tools.logging :as log]))
   

(defn create-transaction [transaction]
  (log/debug "Creating transaction with " transaction)
  transaction)
