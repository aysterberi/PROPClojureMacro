;; Billy G. J. Beltran(bibe1744) & Joakim Berglund(jobe7147)
;; Contact details: billy@caudimordax.org, joakimberglund@live.se

(ns SQLMacro
  (:use clojure.test))

;; not operator according to specification
(defn <> [pre post] (not (= pre post)))

;; The maps we do our tests on
(def persons '({:id 1 :name "olle"}
                {:id 2 :name "anna"}
                {:id 3 :name "isak"}
                {:id 4 :name "beatrice"}))
(def empty-persons '())
(def duplicates '({:id 1 :name "olle"}
                   {:id 1 :name "olle"}
                   {:id 2 :name "anna"}
                   {:id 2 :name "anna"}))

(defmacro select [keys _ collection _ where-condition _ order-argument]
  "
  An sql select-like macro for searching in lists of maps. Takes a vector of [keys] to look up,
  a collection with key/value pairs, a condition to determine which key/value pairs are selected,
  and a single key to determine the sort order. Between each argument there is an ignored
  parameter to match the look of sql select statements:
  select [keys]
  from #{collection}
  where [where-condition]
  orderby order-argument
  "
  `(map #(select-keys % [~@keys])
        (filter #(~(second where-condition)
                   (get % ~(first where-condition))
                   ~(last where-condition))
                (sort-by #(get % ~order-argument)
                         (set ~@collection)))))

(deftest test-greater-than
  (is (= `({:id 4, :name "beatrice"} {:id 3, :name "isak"})
         (select [:id :name]
                 from #{persons}
                 where [:id > 2]
                 orderby :name))))

(deftest test-less-than
  (is (= `({:id 1, :name "olle"} {:id 2, :name "anna"})
         (select [:id :name]
                 from #{persons}
                 where [:id < 3]
                 orderby :id))))

(deftest test-not-equal-to
  (is (= `({:id 2, :name "anna"} {:id 3, :name "isak"} {:id 4, :name "beatrice"})
         (select [:id :name]
                 from #{persons}
                 where [:id <> 1]
                 orderby :id))))

(deftest test-equal-to
  (is (= `({:name "olle", :id 1})
         (select [:name :id]
                 from #{persons}
                 where [:id = 1]
                 orderby :id))))

(deftest test-empty-map
  (is (= `()
         (select [:name :id]
                 from #{empty-persons}
                 where [:id = 1]
                 orderby :id))))

(deftest test-with-duplicates
  (is (= `({:name "anna" :id 2} {:name "olle" :id 1})
         (select [:name :id]
                 from #{duplicates}
                 where [:id > 0]
                 orderby :name))))

(run-tests)

