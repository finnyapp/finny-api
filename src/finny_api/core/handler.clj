(ns finny-api.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn print-request-message [request]
  (response (get-in request [:message])))

(defn as-json-response [request]
  (response {:body {:message "Hello world: "}}))

(defroutes app-routes
  (GET "/" [] as-json-response)
  (POST "/" {body :body} (print-request-message body))
  (route/not-found "Not Found"))

(def app
  (wrap-json-response (wrap-json-body app-routes {:keywords? true :bigdecimals? true})))
