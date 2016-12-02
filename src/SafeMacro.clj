(ns SafeMacro
  (:use clojure.test)
  (:require clojure.core))

(defmacro safe
  "
  takes a vector containting a variable name and its value as its first argument,
  and an expression as its second argument.
  The macro applies the expression using the variable, and returns the result.
  If an exception is thrown, the exception message is returned instead
  "
  ([expression]

   (try (eval expression)
         (catch Exception e# (str "Exception: " (.getMessage e#)))
         )
    )
  )

(deftest test-arithmetic-operation
  (is (= 4 (safe (+ 2 2)))))

(deftest test-divide-by-zero
  (is (= "Exception: Divide by zero" (safe (/ 2 0)))))

(run-all-tests)