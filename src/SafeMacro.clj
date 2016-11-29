(ns SafeMacro)

(defmacro safe-macro [vec form]
  "
  takes a vector containting a variable name and its value as its first argument,
  and an expression as its second argument.
  The macro applies the expression using the variable, and returns the result.
  If an exception is thrown, the exception is returned instead
  "
  )