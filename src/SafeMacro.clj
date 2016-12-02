(ns SafeMacro
  (:use clojure.test))

(defmacro safe
  "
  takes a vector containting a variable name and its value as its first argument,
  and an expression as its second argument.
  The macro applies the expression using the variable, and returns the result.
  If an exception is thrown, the exception is returned instead
  "
  ([expression]
   ((try
      (if (vector? (first list))
        (safe (first list) (rest list))

        )
      (catch Exception e
        )))
   )([vec expression]
        
        ))

(deftest testDivideByZero
  (is (= "msg" (safe (/ 1 0)))))

(run-all-tests)