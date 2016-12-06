(ns SQLMacro
  (:use clojure.test))

;; not operator according to specification
(defn <> [pre post] (not (= pre post)))

;; The map we do our tests on
(def persons '({:id 1 :name "olle"}
                {:id 2 :name "anna"}
                {:id 3 :name "isak"}
                {:id 4 :name "beatrice"}))

(defmacro select [keys _ coll _ cond _ order-cond]
  "
  A macro for searching in lists of maps
  "

  `(map #(select-keys % [~@keys])
        ;; We could add another predicate if we wanted
        (filter #(~(second cond) (get % ~(first cond)) ~(last cond))
                (sort-by #(get % ~order-cond)
                         ~@coll)))

  )

;; Something
(deftest test-greater-than
  (is (= ({:id 4, :name "beatrice"} {:id 3, :name "isak"})
         (select [:id :name]
                 from #{persons}
                 where [:id > 2]
                 orderby :name))))

(run-all-tests)

