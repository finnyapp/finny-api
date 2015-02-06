(ns lobos.config
  (:use lobos.connectivity))

(defn db-url []
  (or (System/getenv "DATABASE_URL") "//localhost:5432/finny"))

(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname (db-url)})

(open-global db)
