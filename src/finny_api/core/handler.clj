(ns finny-api.core.handler
  (:require [finny-api.hal.links :refer [wrap-hal-links]]
            [finny-api.core.transactions :as transactions]
            [finny-api.core.middleware :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.tools.logging :as log]))

(defn json-response [data request & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/hal+json; charset=utf-8"}
   :body    (json/generate-string (wrap-hal-links data request))})

(defn a-quote [] 
  {:message "Hello, world!"})

(defn response-to-create-transaction [request]
  (let [transaction (get-in request [:body])]
    (json-response (transactions/create-transaction transaction) request 201)))

(defn response-to-get-transaction [request]
  (json-response (transactions/get-transaction (get-in request [:params :id])) request))

(defn response-to-get-transactions-total [request]
  (json-response {:total (transactions/total-value-of-transactions)} request))

(defn response-to-get-transactions [request]
  (json-response
    {:transactions (transactions/get-transactions (or
                                     (get-in request [:body :filters])
                                     {}))}
    request))

(defn response-to-update-transaction [request]
  (let [id (get-in request [:params :id])
        transaction (get-in request [:body])]
    (json-response (transactions/update-transaction id transaction) request)))

(defn response-to-delete-transaction [request]
  (transactions/delete-transaction (get-in request [:params :id]))
  (json-response {} request))

(defroutes app-routes
  (GET "/" request (json-response (a-quote) request))
  (GET "/transactions/total" request (response-to-get-transactions-total request))
  (GET "/transactions" request (response-to-get-transactions request))
  (GET "/transaction/:id" request (response-to-get-transaction request))
  (POST "/transaction" request (response-to-create-transaction request))
  (PUT "/transaction/:id" request (response-to-update-transaction request))
  (DELETE "/transaction/:id" request (response-to-delete-transaction request))
  (OPTIONS "/" [] {:status 200
                   :headers {"Allow" "OPTIONS"
                             "Content-Type" "application/hal+json; charset=utf-8"}
                   :body (json/generate-string {:version "0.1.0-SNAPSHOT"})})
  (ANY "/" [] {:status 405
               :headers {"Allow" "OPTIONS"
                         "Content-Type" "application/hal+json; charset=utf-8"}})
  (ANY "*" [] (route/not-found "Not Found")))

(def app
  (wrap-response-logger (wrap-exception-handler (wrap-request-logger (wrap-json-response (wrap-json-body app-routes {:keywords? true :bigdecimals? true}))))))
