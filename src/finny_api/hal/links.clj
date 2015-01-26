(ns finny-api.hal.links)

(defn hal-links [base-uri]
  {:_links {:self {:href base-uri} :api {:href (str base-uri "/api")}}})

(defn get-base-uri [request]
  "Generate a base uri from a ring request. For example 'http://localhost:5000/api'."
  (let [scheme (name (:scheme request))
        context (:context request)
        hostname (get (:headers request) "host")]
    (str scheme "://" hostname context)))

(defn wrap-hal-links [data request]
  (merge (hal-links (get-base-uri request)) data))
