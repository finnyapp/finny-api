(ns finny-api.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn as-json-response [request]
  (response {:body {:message "Hello world: "}}))

(defroutes app-routes
  (GET "/" [] (middleware/wrap-json-response as-json-response))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
