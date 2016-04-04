(ns finny-api.core.middleware
  (:use compojure.core
        [clojure.string :only [upper-case]])
  (:require [clojure.tools.logging :as log]
            [ring.util.response :refer [response]]))

(defn wrap-request-logger [handler]
  (fn [req]
    (let [{remote-addr :remote-addr request-method :request-method uri :uri} req]
      (log/debug remote-addr (upper-case (name request-method)) uri)
      (handler req))))

(defn wrap-response-logger [handler]
  (fn [req]
    (let [response (handler req)
          {remote-addr :remote-addr request-method :request-method uri :uri} req
          {status :status body :body} response]
      (if (instance? Exception body)
        (log/warn body remote-addr (upper-case (name request-method)) uri "->" status body)
        (log/debug remote-addr (upper-case (name request-method)) uri "->" status))
      response)))

(defn wrap-exception-handler [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        (->
          (response {:status  500
                     :headers {"Content-Type" "application/hal+json; charset=utf-8"}
                     :body    "Something went wrong"}))))))
