(ns SQLMacro
  (:use clojure.test))

(defn <> [pre post] (not (= pre post)))

(defmacro select [keys _ coll _ cond _ order-cond]
  "
  A macro for searching in lists of maps
  "
  ;`(map (fn [~keys] ~coll)
  ;     (filter (fn [a#] (~@(infix cond))))
  ;            (sort-by (fn [~keys] ~order-cond)
  ;                    ~coll)))

  `(map #(select-keys % [~@keys])
        (filter #(~(second cond) (get % ~(first cond)) ~(last cond))
                (sort-by #(get % ~order-cond)
                         ~@coll)))
  )

(def persons '({:id 1 :name "olle"}
                {:id 2 :name "anna"}
                {:id 3 :name "isak"}
                {:id 4 :name "beatrice"}))



(deftest test-greater-than
  (is (= ({:id 4, :name "beatrice"} {:id 3, :name "isak"})
         (select [:id :name]
                 from #{persons}
                 where (:id > 2)
                 orderby :name))))

;(deftest test-equals
;  (is (= ({:id 1, :name "olle"})
;         (select [:id :name]
;                 from #{persons}
;                 where (:id < 3)
;                 orderby :name))))

(run-all-tests)

