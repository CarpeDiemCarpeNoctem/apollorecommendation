(ns librarian.recommendation
  (:require [librarian.ratings :as ratings]
            [librarian.configuration :as config]))

(use 'criterium.core)
(set! *warn-on-reflection* true)

; - - - - - - - - - -
; The recommendation namespace contains functions that take a map of user ids
; mapped to maps of book ids and the ratings they gave them and makes a
; book recommendation to the user based on the calculation of similarity
; scores between the user and each of their friends.
; - - - - - - - - - -

; The following 5 functions are helper functions used by recommend-books:
; - get-similarities, similarity-scores, weighted-ratings, sum-weighted-ratings, sum-similarity-scores

(defn get-similarities
  "Returns a list of similarity scores between the user and each of their friends"
  [ratings user formula]
  (let [list-without-user (dissoc ratings user)]
      (reduce #(assoc %1 (key %2) (formula (ratings user) (val %2))) {} list-without-user)))

(defn similarity-scores
  "Returns a list of similarity scores for each friend a user has and filters out 0 or lower scores"
  [ratings user formula]
  (filter #(not (<= (second %) 0)) (get-similarities ratings user formula)))

(defn weighted-ratings
  "Returns weighted book ratings (for books the user hasn't read) based on similarity of each friend to user"
  [ratings scores user]
  (reduce 
   (fn [collection simil-scores]
     (let [friend-id (key simil-scores)
           sim (val simil-scores)
           not-read (filter #(not (contains? (ratings user) (key %))) (ratings friend-id))
           weighted-pref (zipmap (keys not-read) (map #(* % sim) (vals not-read)))]
       (assoc collection friend-id weighted-pref))) {} scores))

(defn sum-weighted-ratings
  "Returns a sum of all weighted book ratings"
  [weighted-ratings]
  (reduce (fn [collection wratings] (merge-with #(+ %1 %2) collection wratings)) {} (vals weighted-ratings)))

(defn sum-similarity-scores
  "Returns the sum of all similarity scores relating to each book"
  [weighted-pref book-wprefs sim-users]
  (reduce (fn [collection bprefs]
            (let [book (first bprefs)
                  rated-users (reduce #(if (contains? (val %2) book) (conj %1 (key %2)) %1) [] weighted-pref)
                  similarities (reduce + (map #(sim-users %) rated-users))]
              (assoc collection book similarities))) {} book-wprefs))

; - - - - - - - - - -

(defn recommend-books
  "Returns a map of book ids recommended for the user to read"
  [ratings user formula]
  (let [similar-friends (into {} (similarity-scores ratings user formula))
        weighted-rs (weighted-ratings ratings similar-friends user)
        sum-wratings (sum-weighted-ratings weighted-rs)
        sum-sim-scores (sum-similarity-scores weighted-rs sum-wratings similar-friends)]
    (zipmap (keys sum-wratings) (map #(/ (second %) (sum-sim-scores (first %))) sum-wratings))))

(defn sort-by-value
  "Sorts a given collection of key-value pairs by its value in descending order"
  [coll]
  (into (sorted-map-by (fn [key1 key2]
                         (compare [(get coll key2) key2]
                                  [(get coll key1) key1])))
        coll))

(defn get-highest-rated-book
  "Returns the highest rated book id"
  [books]
  (if (empty? books)
    nil
    (subs (str (key (first books))) 1)))

(defn recommended-book-xml
  "Creates the URL for the XML file of the recommended book from which to parse the book information"
  [book]
  (if (nil? book)
    nil
    (format config/book-info book config/api-key)))

(defn parse-book-xml
  "Parses the recommended book"
  [book-xml]
  (if (nil? book-xml)
    nil
    (:content (second (:content (clojure.xml/parse book-xml))))))

(defn recommended-book-info
  "Returns the recommended book title, description and link"
  [parsed-book]
  (if (nil? parsed-book)
    (let [title "Sorry no recommendation"
         description "You need to have friends on Goodreads and some books rated. Also please make sure you allow the application to access your profile."
         link "http://www.goodreads.com"]
      (zipmap [:title :description :alink] [title description link]))
    (let [title (first (ratings/extract-tag :title parsed-book))
         description (first (ratings/extract-tag :description parsed-book))
         link (first (ratings/extract-tag :url parsed-book))]
      (zipmap [:title :description :alink] [title description link]))))