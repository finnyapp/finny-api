(ns finny-api.transactions-acceptance-test
  (:require [finny-api.core.handler :refer [app]]
            [midje.sweet :refer :all]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as client]
            [cheshire.core :as json]))

(def server (atom nil))

(defn start-server []
  (swap! server
         (fn [_] (run-jetty app {:port 3000 :join? false}))))

(defn stop-server []
  (.stop @server))

(against-background [(before :contents (start-server))
                     (after  :contents (stop-server))]

  (fact "Gets root" :at
        (let [response (client/get "http://localhost:3000")]
          (:status response) => 200
          (:message (json/parse-string (:body response) true)) => "Hello, world!")))
