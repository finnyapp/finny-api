(ns finny-api.db.query-filters
  (:require [honeysql.core :as h]
            [honeysql.helpers :refer :all]
            [clj-time.coerce :as coercer]
            [clj-time.format :as date-formatter]))

(defn- parse-date [date]
  (coercer/to-sql-date (date-formatter/parse (date-formatter/formatter "yyyy-MM-dd") date)))

(defn- to-category-clause [filters]
  (when-let [category-filter (:category filters)]
    [:= :category category-filter]))

(defn- to-date-clause [filters]
  (when-let [date-filter (:date filters)]
    (cond
      (:is date-filter) [:= :date (parse-date (:is date-filter))]
      (and (:after date-filter) (:before date-filter)) [:between :date (parse-date (:after date-filter)) (parse-date (:before date-filter))]
      (:before date-filter) [:< :date (parse-date (:before date-filter))]
      (:after date-filter) [:> :date (parse-date (:after date-filter))]
      )))

(defn to-sql [filters]
  (let [category-clause (to-category-clause filters)
        date-clause (to-date-clause filters)]
    (cond
      (and (empty? category-clause) (empty? date-clause)) {}
      (and (not (empty? category-clause)) (not (empty? date-clause))) {:where [:and category-clause date-clause]}
      (not (empty? category-clause)) {:where category-clause}
      (not (empty? date-clause)) {:where date-clause}
      )))
