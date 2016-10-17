(ns librarian.recommendation
  (:require [librarian.ratings :as ratings]
            [librarian.configuration :as config]))

(defn euclid
  [p1 p2]
  (let [si (filter p1 (keys p2))]
    (if (empty? si)
      0
      (let [squares-sum (reduce #(+ %1 (Math/pow (- (p1 %2) (p2 %2)) 2)) 0 si)]
        (/ 1 (+ squares-sum 1))))))

(defn pearson
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      0
      (let [number-of-books (count books-both-read)
            ; sums
            sum-user-ratings (reduce #(+ %1 (user %2)) 0 books-both-read)
            sum-friend-ratings (reduce #(+ %1 (friend %2)) 0 books-both-read)
            ; sum of squared values
            sum-squared-user-ratings (reduce #(+ %1 (Math/pow (user %2) 2)) 0 books-both-read)
            sum-squared-friend-ratings (reduce #(+ %1 (Math/pow (friend %2) 2)) 0 books-both-read)
            ; sum of their products
            sum-products (reduce #(+ %1 (* (user %2) (friend %2))) 0 books-both-read)
            covariance (- sum-products (/ (* sum-user-ratings sum-friend-ratings) number-of-books))
            standard-deviation (Math/sqrt (* (- sum-squared-user-ratings (/ (Math/pow sum-user-ratings 2) number-of-books))
                                             (- sum-squared-friend-ratings (/ (Math/pow sum-friend-ratings 2) number-of-books))))]
        (if (= standard-deviation 0.0)
          0
          (/ covariance standard-deviation))))))

(defn manhattan-distance
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (reduce #(+ %1 (Math/abs (- (user %2) (friend %2)))) 0 books-both-read)))

(defn spearman
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (let [distance (reduce #(+ %1 (Math/pow (- (user %2) (friend %2)) 2)) 0 books-both-read)
          denominator (* (count books-both-read) (- (Math/pow (count books-both-read) 2) 1))]
      (if (= denominator 0.0)
        0
        (Math/abs (- 1 (/ (* 6 distance) denominator))))
      )))

(defn get-similarities
  [ratings user formula]
  (let [list-without-user (dissoc ratings user)]
      (reduce #(assoc %1 (key %2) (formula (ratings user) (val %2))) {} list-without-user)))

(defn similarity-scores
  [ratings user formula]
  (filter #(not (<= (second %) 0)) (get-similarities ratings user formula)))

(defn weighted-ratings
  [ratings scores user]
  (reduce 
   (fn [collection simil-scores]
     (let [friend-id (key simil-scores)
           sim (val simil-scores)
           not-read (filter #(not (contains? (ratings user) (key %))) (ratings friend-id))
           weighted-pref (zipmap (keys not-read) (map #(* % sim) (vals not-read)))]
       (assoc collection friend-id weighted-pref))) {} scores))

(defn sum-weighted-ratings
  [weighted-ratings]
  (reduce (fn [collection wratings] (merge-with #(+ %1 %2) collection wratings)) {} (vals weighted-ratings)))

(defn sum-similarity-scores
  [weighted-pref book-wprefs sim-users]
  (reduce (fn [collection bprefs]
            (let [book (first bprefs)
                  rated-users (reduce #(if (contains? (val %2) book) (conj %1 (key %2)) %1) [] weighted-pref)
                  similarities (reduce + (map #(sim-users %) rated-users))]
              (assoc collection book similarities))) {} book-wprefs))

(defn recommend-books
  [ratings user formula]
  (let [similar-friends (into {} (similarity-scores ratings user formula))
        weighted-rs (weighted-ratings ratings similar-friends user)
        sum-wratings (sum-weighted-ratings weighted-rs)
        sum-sim-scores (sum-similarity-scores weighted-rs sum-wratings similar-friends)]
    (zipmap (keys sum-wratings) (map #(/ (second %) (sum-sim-scores (first %))) sum-wratings))))

(defn sort-by-value
  [coll]
  (into (sorted-map-by (fn [key1 key2] (compare [(get coll key2) key2] [(get coll key1) key1]))) coll))

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
    (let [title "No recommendation"
         description "You need to have friends and some books rated. Also make sure you allow application access to your profile."
         link "http://www.goodreads.com"]
      (zipmap [:title :description :alink] [title description link]))
    (let [title (first (ratings/extract-tag :title parsed-book))
         description (first (ratings/extract-tag :description parsed-book))
         link (first (ratings/extract-tag :url parsed-book))]
      (zipmap [:title :description :alink] [title description link]))))