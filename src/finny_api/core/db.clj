(ns finny-api.core.db
  (:use lobos.core))

(defn db-rollforward []
  (migrate))

(defn db-rollback []
  (rollback))
