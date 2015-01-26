(ns finny-api.core.handler
  (:require [finny-api.hal.links :refer [wrap-hal-links]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn a-quote [] {:message "Hello, world!"})

(defn json-response [data request & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/hal+json; charset=utf-8"}
   :body    (json/generate-string (wrap-hal-links data request))})

(defroutes app-routes
  (GET "/" request (json-response (a-quote) request))
  (POST "/" request (json-response (get-in request [:body]) request))
  (ANY "*" [] (route/not-found "Not Found")))

(def app
  (wrap-json-response (wrap-json-body app-routes {:keywords? true :bigdecimals? true})))
