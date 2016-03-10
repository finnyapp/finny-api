(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use  (finny-apy.db.config)
         (lobos [migration :only [defmigration]] core schema
               config helpers)))

(defmigration add-transactions-table
  (up [] (create
          (tbl :transactions
            (double :value)
            (varchar :comments 255)
            (check :value (> :value 0)))))
  (down [] (drop (table :transactions))))

(defmigration add-category-to-transactions
  (up [] (alter :add (table :transactions (varchar :category 255))))
  (down [] (alter :drop (table :transactions (column :category)))))
