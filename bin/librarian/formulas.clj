(ns librarian.formulas)

(use 'criterium.core)
(set! *warn-on-reflection* true)

(defn euclid
  [p1 p2]
  (let [si (filter p1 (keys p2))]
    (if (empty? si)
      (int 0)
      (let [squares-sum (float (reduce #(+ %1 (float (Math/pow (- (int (p1 %2)) (int (p2 %2))) (int 2)))) (int 0) si))]
        (/ (int 1) (+ squares-sum (int 1)))))))

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
    (if (empty? books-both-read)
      0
      (reduce #(+ %1 (Math/abs (- (user %2) (friend %2)))) 0 books-both-read))))

(defn spearman
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      0
      (let [distance (reduce #(+ %1 (Math/pow (- (user %2) (friend %2)) 2)) 0 books-both-read)
            n (count books-both-read)
            denominator (* n (- (Math/pow n 2) 1))]
        (if (= denominator 0.0)
          0
          (Math/abs (- 1 (/ (* 6 distance) denominator))))
        ))))

(defn chebyshev
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      0
      (let [vector-list (reduce #(conj %1 (Math/abs (- (user %2) (friend %2)))) [] books-both-read)]
        (apply max vector-list))
        )))

(defn jaccard-index
  [user friend]
  (let [first-person (reduce #(into %1 %2) #{} user)
        second-person (reduce #(into %1 %2) #{} friend)]
    (if (empty? (clojure.set/union first-person second-person))
           0
           (/ (count (clojure.set/intersection first-person second-person))
              (count (clojure.set/union first-person second-person))))))

(defn sorensen-dice
  [user friend]
  (let [first-person (reduce #(into %1 %2) #{} user)
        second-person (reduce #(into %1 %2) #{} friend)
        n-first-person (count first-person)
        n-second-person (count second-person)]
    (if (= n-first-person n-second-person)
      0
      (/ (* 2 (count (clojure.set/intersection first-person second-person))) (+ n-first-person n-second-person)))))