(ns lobos.config
  (:use [finny-api.db.config :as finny-db]
        lobos.connectivity))

(open-global finny-db/db-connection-info)
