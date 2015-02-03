(defproject finny-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-json "0.3.1"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [cheshire "5.4.0"]
                 [postgresql "9.1-901.jdbc4"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.7"]
                 [log4j/log4j "1.2.17" :exclusions  [javax.mail/mail
                                                     javax.jms/jms
                                                     com.sun.jmdk/jmxtools
                                                     com.sun.jmx/jmxri]]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler finny-api.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
