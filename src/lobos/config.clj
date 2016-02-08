(ns lobos.config
  (:use [finny-api.db.config :as finny-db]
        lobos.connectivity))

(when (not (= (System/getenv "FINNY_ENV") "test"))
        (open-global finny-db/db-connection-info))
