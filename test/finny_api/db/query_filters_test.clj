(ns finny-api.db.query-filters-test
  (:require [finny-api.db.query-filters :as query-filters]
            [midje.sweet :refer :all]
            [clj-time.coerce :as coercer]
            [clj-time.format :as date-formatter]
            [honeysql.core :as h]
            [honeysql.helpers :refer :all]))

(defn- sqlfy-date [date]
  (coercer/to-sql-date (date-formatter/parse (date-formatter/formatter "yyyy-MM-dd") date)))

(background (around :facts (let [today         "2016-01-01"
                                 next-year     "2017-01-01"
                                 last-year     "2015-01-01"
                                 today-sql     (sqlfy-date "2016-01-01")
                                 next-year-sql (sqlfy-date "2017-01-01")
                                 last-year-sql (sqlfy-date "2015-01-01")] ?form)))

(fact "Converts empty filters to empty map"
      (query-filters/to-sql {}) => {})

(fact "Converts filter with category into korma query arguments"
      (query-filters/to-sql {:category "Education"}) => (h/build :where [:= :category "Education"]))

(fact "Converts filter with specficic date into korma query arguments"
      (query-filters/to-sql {:date {:is today}}) => (h/build :where [:= :date today-sql]))

(fact "Converts filter for dates before given date into korma query arguments"
      (query-filters/to-sql {:date {:before today}}) => (h/build :where [:< :date today-sql]))

(fact "Converts filter for dates after given date into korma query arguments"
      (query-filters/to-sql {:date {:after today}}) => (h/build :where [:> :date today-sql]))

(fact "Converts filter for dates between given dates into korma query arguments"
      (query-filters/to-sql {:date {:after last-year :before next-year}}) => (h/build :where [:between :date last-year-sql next-year-sql]))

(fact "Converts filter with category and dates into korma query arguments"
      (query-filters/to-sql {:category "Education" :date {:is today}}) => (h/build :where [:and [:= :category "Education"] [:= :date today-sql]]))
