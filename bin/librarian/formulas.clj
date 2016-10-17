(ns librarian.formulas)

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