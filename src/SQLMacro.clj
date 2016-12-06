(ns SQLMacro
  (:use clojure.test))

;; not operator according to specification
(defn <> [pre post] (not (= pre post)))

;; The map we do our tests on
(def persons '({:id 1 :name "olle"}
                {:id 2 :name "anna"}
                {:id 3 :name "isak"}
                {:id 4 :name "beatrice"}))

(defmacro select [keys _ collection _ where-condition _ order-condition]
  "
  A macro for searching in lists of maps
  "

  `(map #(select-keys % [~@keys])
        (filter #(~(second where-condition) (get % ~(first where-condition)) ~(last where-condition))
                (sort-by #(get % ~order-condition)
                         ~@collection)))

  )

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

(run-all-tests)

