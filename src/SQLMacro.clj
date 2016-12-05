(ns SQLMacro)

(defmacro select [keys _ coll _ cond _ order-cond]
  "
  A macro for searching in lists of maps
  "
  `(map (fn [~keys] ~coll)
        (filter (fn [~keys] ~cond)
                (sort-by (fn [~keys] ~order-cond)
                         ~coll))))

(def persons '({:id 1 :name "olle"}
                {:id 2 :name "anna"}
                {:id 3 :name "isak"}
                {:id 4 :name "beatrice"}))

(prn (select [:id :name]
              from #{persons}
              where [:id > 1]
              orderby :name
              ))

