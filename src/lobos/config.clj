(ns lobos.config
  (:use [finny-api.db.config :as finny-db]
        lobos.connectivity)
  (:require [environ.core :as environ]))

(when (not (= (environ/env :finny-env) "test"))
        (with-connection finny-db/db-connection-info))

(def db finny-db/db-connection-info)
