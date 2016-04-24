(ns finny-api.transactions-fixtures)

(def small-expense
  {:value 5.0 :comments "A small expense" :type "expense"})

(def heavy-expense
  {:value 999.0 :comments "Gosh!" :type "expense"})

(def small-income
  {:value 7.0 :comments "A small income" :type "income"})

(def large-income
  {:value 7654.0 :comments "Wow! income" :type "income"})
