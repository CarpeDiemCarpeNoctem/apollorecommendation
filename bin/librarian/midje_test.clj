(ns librarian.midje-test
  (:use midje.sweet)
  (:require [librarian.test-data :refer :all]
            [librarian.algorithms :refer :all]
            [librarian.xmlparser :refer :all]))

(facts "ns:algorithms 'euclid'"
  (fact "Returns Euclidean distance score"
    (euclid {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 1.0)
  
  (fact "Returns Euclidean distance score"
    (euclid {:123 3 :234 5 :345 4} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0.3333333333333333)
  
  (fact "Returns Euclidean distance score"
    (euclid {} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0)
  
  (fact "Returns Euclidean distance score"
    (euclid {} {}) => 0))

(facts "ns:formulas 'pearson'"
  (fact "Returns Pearson distance score"
    (pearson {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 1.0)
  
  (fact "Returns Pearson distance score"
    (pearson {:123 3 :234 5 :345 4} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 1.0)
  
  (fact "Returns Pearson distance score"
    (pearson {} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0)
  
  (fact "Returns Pearson distance score"
    (pearson {} {}) => 0))

(facts "ns:ratings 'parse-xml'"
  (fact "Returns a map from supplied XML"
    (parse-xml "<hello>world</hello>") => {:tag :hello, :attrs nil, :content ["world"]}))