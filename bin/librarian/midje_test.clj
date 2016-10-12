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

(facts "ns:ratings 'get-friends'"
  (fact "Returns only the friends portion of parsed XML file"
    (get-friends sample-parsed-xml) => [{:attrs nil, :content [{:attrs nil, :content ["11111111"], :tag :id} {:attrs nil, :content ["4"], :tag :friends_count} {:attrs nil, :content ["78"], :tag :reviews_count}], :tag :user} {:attrs nil, :content [{:attrs nil, :content ["22222222"], :tag :id} {:attrs nil, :content ["4"], :tag :friends_count} {:attrs nil, :content ["50"], :tag :reviews_count}], :tag :user}]))

(facts "ns:ratings 'list-friends'"
  (fact "Returns a list of all of friend's ids"
    (list-friends sample-get-friends) => '("11111111" "22222222")))

(facts "ns:ratings 'make-keyword-list'"
 (fact "Returns list of keywords from the collection of elements supplied"
   (make-keyword-list '("11111111" "22222222" "33333333" "44444444" "55555555")) => '(:11111111 :22222222 :33333333 :44444444 :55555555))
 
 (fact "Returns empty list if supplied collection is empty"
    (make-keyword-list '()) => '()))