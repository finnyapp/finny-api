(defproject finny-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.4.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [cheshire "5.5.0"]
                 [postgresql "9.3-1102.jdbc41"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.14"]
                 [korma "0.4.2"]
                 [lobos "1.0.0-beta3"]
                 [log4j/log4j "1.2.17" :exclusions  [javax.mail/mail
                                                     javax.jms/jms
                                                     com.sun.jmdk/jmxtools
                                                     com.sun.jmx/jmxri]]]
  :plugins [[lein-ring "0.9.7"]
            [lein-midje "3.2"]
            [lein-cloverage "1.0.6"]
            [org.clojars.edtsech/lein-lobos "1.0.0-beta1"]]
  :ring {:handler finny-api.core.handler/app}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.0"]
                                  [midje "1.8.3" :exclusions [org.clojure/clojure]]] }})
