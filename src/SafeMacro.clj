;; Billy G. J. Beltran(bibe1744) & Joakim Berglund(jobe7147)
;; Contact details: billy@caudimordax.org, joakimberglund@live.se

(ns SafeMacro
  (:use clojure.test)
  (:require clojure.core)
  (:import (java.io FileReader File)))

(defmacro safe
  "
  takes a vector containting a variable name and its value as its first argument,
  and an expression as its second argument, or a single expression as its full argument.
  The macro applies the expression using the variable, and returns the result.
  If an exception is thrown, the exception message is returned instead
  "
  ([expression]
   `(try (~@expression)
         (catch Exception exception# (str (.toString exception#)))))
  ([vec & expression]
    ;; From https://clojuredocs.org/clojure.core/with-open:
    ;; Evaluates body in a try expression with names bound to the values
    ;; of the inits, and a finally clause that calls (.close name) on each
    ;; name in reverse order
   (if (not (vector? vec))
     "Must be a vector"
     `(try (with-open ~vec ~@expression)
           (catch Exception exception# (str (.toString exception#)))))))

(deftest test-arithmetic-operation
  (is (= 4 (safe (+ 2 2)))))

(deftest test-divide-by-zero
  (is (= "java.lang.ArithmeticException: Divide by zero" (safe (/ 2 0)))))

(deftest test-closeable ;; First byte of file.txt: 76 = 'L'
  (is (= 76 (safe [s (FileReader. (File. "file.txt"))] (.read s)))))

(deftest test-missing-file
  (is (= "java.io.FileNotFoundException: non-existant (Det går inte att hitta filen)" (safe [s (FileReader. (File. "non-existant"))] (. s read)))))

(deftest test-wrong-input
  (is (= "Must be a vector" (safe list (s (FileReader. (File. "file.txt"))) (.read s)))))

(run-tests)