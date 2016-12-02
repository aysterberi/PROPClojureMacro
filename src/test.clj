(ns test
  (:use clojure.test))

(defn adder [x y]
  (+ x y))

(deftest test-adder
  (is (= 2 (adder 1 1))))



(run-all-tests)