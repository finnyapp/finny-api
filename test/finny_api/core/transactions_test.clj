(ns finny-api.core.transactions-test
  (:require [midje.sweet :refer :all]
            [clj-time.core :as date]
            [finny-api.core.transactions :as transactions]
            [finny-api.db.transactions :as db]))

(def today (date/date-time 2016 01 01))

(fact "Stores transaction in db with known fields"
      (against-background (db/create-transaction {:value 7 :comments "Blah" :category "Entertainment" :date today}) => true)
      (let [stored-transaction (transactions/create-transaction {:value 7 :comments "Blah" :category "Entertainment" :date "2016-01-01" :useless-field true})]
        stored-transaction => {:value 7 :comments "Blah" :category "Entertainment" :date today}))

(fact "Gets the total value of transactions from db"
      (against-background (db/total-value-of-transactions) => 27)
      (let [total (transactions/total-value-of-transactions)]
        total => 27))

(fact "Gets a transaction from db or nothing"
      (against-background (db/get-transaction 9) => {:value 99 :comments "99 problems"})
      (against-background (db/get-transaction 0) => nil)
      (let [found-transaction (transactions/get-transaction 9)
            missing-transaction (transactions/get-transaction 0)]
        found-transaction => {:value 99 :comments "99 problems"}
        missing-transaction => {}))

(fact "Updates a transaction in the db"
      (against-background (db/update-transaction 8 {:value 80 :comments "8 * 10" :category "Entertainment" :date today}) => 1)
      (let [updated-transaction (transactions/update-transaction 8 {:value 80 :comments "8 * 10" :category "Entertainment" :date "2016-01-01"})]
        updated-transaction => {:value 80 :comments "8 * 10" :category "Entertainment" :date today}))

(fact "Deletes a transaction in the db"
      (against-background (db/delete-transaction 3) => 1)
      (let [deleted (transactions/delete-transaction 3)]
        deleted => :deleted))

(fact "Gets all transactions from db"
      (against-background (db/get-transactions {}) => [{:value 1 :comments "um"} {:value 2 :comments "dois"}])
      (let [all-transactions (transactions/get-transactions {})]
        all-transactions => [{:value 1 :comments "um"} {:value 2 :comments "dois"}]))

(fact "Gets transactions filtered by category"
      (against-background (db/get-transactions {:category "Entertainment"}) => [{:value 3 :category "Entertainment"}])
      (transactions/get-transactions {:category "Entertainment"}) => [{:value 3 :category "Entertainment"}])
