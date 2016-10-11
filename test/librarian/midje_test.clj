(ns librarian.midje-test
  (:use midje.sweet)
  (:require [librarian.algorithms :refer :all]))

(facts "ns:algorithms 'euclid'"
  (fact "Returns Euclidean distance score"
    (euclid {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 1.0))