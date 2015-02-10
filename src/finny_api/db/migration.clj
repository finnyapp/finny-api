(ns finny-api.db.migration
  (:use lobos.core))

(defn db-rollforward []
  (migrate))

(defn db-rollback []
  (rollback))
