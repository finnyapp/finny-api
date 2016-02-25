(ns lobos.config
  (:use [finny-api.db.config :as finny-db]
        lobos.connectivity))

(when (not (= (System/getenv "FINNY_ENV") "test"))
        (with-connection finny-db/db-connection-info))

(def db finny-db/db-connection-info)
