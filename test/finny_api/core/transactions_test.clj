(ns finny-api.core.transactions-test
  (:require [midje.sweet :refer :all]
            [finny-api.core.transactions :as transactions]
            [finny-api.db.transactions :as db]))

(fact "Stores transaction in db with known fields"
      (against-background (db/create-transaction {:value 7 :comments "Blah" :category "Entertainment"}) => true)
      (let [stored-transaction (transactions/create-transaction {:value 7 :comments "Blah" :category "Entertainment" :useless-field true})]
        stored-transaction => {:value 7 :comments "Blah" :category "Entertainment"}))

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

(fact "Gets all transactions from db"
      (against-background (db/all-transactions) => [{:value 1 :comments "um"} {:value 2 :comments "dois"}])
      (let [all-transactions (transactions/all-transactions)]
        all-transactions => [{:value 1 :comments "um"} {:value 2 :comments "dois"}]))

(fact "Updates a transaction in the db"
      (against-background (db/update-transaction 8 {:value 80 :comments "8 * 10" :category "Entertainment"}) => 1)
      (let [updated-transaction (transactions/update-transaction 8 {:value 80 :comments "8 * 10" :category "Entertainment"})]
        updated-transaction => {:value 80 :comments "8 * 10" :category "Entertainment"}))

(fact "Deletes a transaction in the db"
      (against-background (db/delete-transaction 3) => 1)
      (let [deleted (transactions/delete-transaction 3)]
        deleted => :deleted))
