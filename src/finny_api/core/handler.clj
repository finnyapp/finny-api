(ns finny-api.core.handler
  (:require [finny-api.hal.links :refer [wrap-hal-links]]
            [finny-api.core.middleware :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.tools.logging :as log]))

(defn a-quote [] 
  {:message "Hello, world!"})

(defn json-response [data request & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/hal+json; charset=utf-8"}
   :body    (json/generate-string (wrap-hal-links data request))})

(defroutes app-routes
  (GET "/" request (json-response (a-quote) request))
  (context "/api" []
     (OPTIONS "/" [] {:status 200
                      :headers {"Allow" "OPTIONS"
                                "Content-Type" "application/hal+json; charset=utf-8"}
                      :body (json/generate-string {:version "0.1.0-SNAPSHOT"})})
     (ANY "/" [] {:status 405
                  :headers {"Allow" "OPTIONS"
                            "Content-Type" "application/hal+json; charset=utf-8"}}))
  (POST "/" request (json-response (get-in request [:body]) request))
  (ANY "*" [] (route/not-found "Not Found")))

(def app
  (wrap-response-logger (wrap-request-logger (wrap-json-response (wrap-json-body app-routes {:keywords? true :bigdecimals? true})))))
