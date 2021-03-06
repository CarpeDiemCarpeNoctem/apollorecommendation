(ns librarian.midje-test
  (:use midje.sweet)
  (:require [librarian.test-data :refer :all]
            [librarian.ratings :refer :all]
            [librarian.recommendation :refer :all]
            [librarian.formulas :refer :all]))

(use 'criterium.core)
(set! *warn-on-reflection* true)

; - - - - - - - - - -
; The midje-test namespace is used to do unit testing using Midje unit testing framework.
; Sets of functions from three namespaces are unit-tested: Ratings Tests, Recommendation
; Tests and Formulas Tests. The test-data namespace is used to help unit test certain functions.
; - - - - - - - - - -

; Ratings Tests:
; - - - - - - - - - -

(facts "ns:ratings 'asynchronized'"
  (fact "returns a processed list from a given function and collection"
    (asynchronized inc [1 2 3 4 5]) => '(2 3 4 5 6))
  
  (fact "when collection is empty it returns an empty list"
    (asynchronized inc []) => '())
  
  (fact "when no collection is present it returns an empty list"
    (asynchronized inc nil) => '())
  
  (fact "when only nil is present in collection it returns an empty list"
    (asynchronized inc [nil]) => '())
  
  (fact "when nil is present in collection it processes only non nil elements of the original collection"
    (asynchronized inc [nil 3 4 nil 8 nil nil 9]) => '(4 5 9 10)))

; get-friends-xml goes through integration testing in live environment as, due to its nature,
; it relies on I/O operation external to the system.

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
    (make-keyword-list '()) => '())
 
  (fact "Returns empty list if no collection is supplied"
    (make-keyword-list nil) => '()))

; create-ratings, vector-of-extended-friends, lits-user-and-extended-friends, users-book-ratings,
; user-book-reviews and book-ratings go through integration testing in live environment as,
; due to their nature, they rely on I/O operations external to the system.
; - - - - - - - - - -

; Recommendation Tests:
; - - - - - - - - - -

(facts "ns:recommendations 'recommend-books'"
  (fact "Returns map of key-value pairs of book ids and their ratings"
    (recommend-books {:11111111 {:123 3 :234 5 :345 4} :22222222 {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}} :11111111 euclid) => {:456 2.0, :567 3.0, :678 5.0})
 
  (fact "Returns empty list if supplied collection is empty"
    (recommend-books {} :11111111 euclid) => {}))

(facts "ns:recommendations 'sort-by-value'"
  (fact "Sorts supplied collection by value in ascending order"
    (sort-by-value {:456 2.0, :567 3.0, :678 5.0}) => {:456 2.0, :567 3.0, :678 5.0})
  
  (fact "Sorts supplied collection by value is ascending order"
    (sort-by-value {:678 5.0, :567 3.0, :456 2.0}) => {:456 2.0, :567 3.0, :678 5.0})
  
  (fact "Sorts supplied collection by value is ascending order"
    (sort-by-value {:678 5.0, :456 2.0, :567 3.0}) => {:456 2.0, :567 3.0, :678 5.0})
  
  (fact "Returns empty list if supplied collection is empty"
    (sort-by-value {}) => {}))

(facts "ns:recommendations 'get-highest-rated-book'"
  (fact "Returns id of highest rated book"
    (get-highest-rated-book {:456 2.0, :567 3.0, :678 5.0}) => "678")
  
  (fact "Returns empty list if supplied collection is empty"
    (get-highest-rated-book {}) => nil))

; recommended-book-xml goes through integration testing in live environment as, due to its nature,
; it relies on I/O operation external to the system. Only nil input value is unit tested.

(facts "ns:recommendations 'recommended-book-xml'"
  (fact "Returns nil if no book is supplied"
    (get-highest-rated-book nil) => nil))

; parse-book-xml and recommended-book-info go through integration testing in live environment as,
; due to their nature, they rely on I/O operations external to the system. Only nil input value is
; unit tested.

(facts "ns:recommendations 'parse-book-xml'"
  (fact "Returns nil if no input is supplied"
    (parse-book-xml nil) => nil))

(facts "ns:recommendations 'recommended-book-info'"
  (fact "Returns nil if no input is supplied"
    (recommended-book-info nil) => {:alink "http://www.goodreads.com", :description "You need to have friends on Goodreads and some books rated. Also please make sure you allow the application to access your profile.", :title "Sorry no recommendation"}))
; - - - - - - - - - -

; Formulas Tests:
; - - - - - - - - - -

; All mathematical formulas tests are conducted when there are two identical maps, when there is
; partial matching, when there are no ratings in one of the maps and when both maps are empty.

(facts "ns:formulas 'euclid'"
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

(facts "ns:formulas 'manhattan-distance'"
  (fact "Returns Manhattan distance score"
    (manhattan-distance {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 0)
  
  (fact "Returns Manhattan distance score"
    (manhattan-distance {:123 3 :234 5 :345 4} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 2)
  
  (fact "Returns Manhattan distance score"
    (manhattan-distance {} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0)
  
  (fact "Returns Manhattan distance score"
    (manhattan-distance {} {}) => 0))

(facts "ns:formulas 'spearman'"
  (fact "Returns Spearman correlation score"
    (spearman {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 1.0)
  
  (fact "Returns Spearman correlation score"
    (spearman {:123 3 :234 5 :345 4} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 1.0)
  
  (fact "Returns Spearman correlation score"
    (spearman {} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0)
  
  (fact "Returns Spearman correlation score"
    (spearman {} {}) => 0))

(facts "ns:formulas 'chebyshev'"
  (fact "Returns Chebyshev correlation score"
    (chebyshev {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 0)
  
  (fact "Returns Chebyshev correlation score"
    (chebyshev {:123 3 :234 5 :345 4} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 1)
  
  (fact "Returns Chebyshev correlation score"
    (chebyshev {} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0)
  
  (fact "Returns Chebyshev correlation score"
    (chebyshev {} {}) => 0))

(facts "ns:formulas 'jaccard-index'"
  (fact "Returns Jaccard index score"
    (jaccard-index {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 1)
  
  (fact "Returns Jaccard index score"
    (jaccard-index {:123 3 :234 5 :345 4} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 1/3)
  
  (fact "Returns Jaccard index score"
    (jaccard-index {} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0)
  
  (fact "Returns Jaccard index score"
    (jaccard-index {} {}) => 0))

(facts "ns:formulas 'sorensen-dice'"
  (fact "Returns Sorensen-Dice correlation score"
    (sorensen-dice {:123 3 :234 5 :345 4} {:123 3 :234 5 :345 4}) => 0)
  
  (fact "Returns Sorensen-Dice correlation score"
    (sorensen-dice {:123 3 :234 5 :345 4} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 1/2)
  
  (fact "Returns Sorensen-Dice correlation score"
    (sorensen-dice {} {:123 4 :345 5 :456 2 :567 3.0, :678 5.0}) => 0)
  
  (fact "Returns Sorensen-Dice correlation score"
    (sorensen-dice {} {}) => 0))