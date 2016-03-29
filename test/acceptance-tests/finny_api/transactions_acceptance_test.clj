(ns finny-api.transactions-acceptance-test
  (:require [midje.sweet :refer :all]))

(fact "This is an acceptance test" :at
      (prn "This is an acceptance test")
      true => true)
