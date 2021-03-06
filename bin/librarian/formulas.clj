(ns librarian.formulas)

(use 'criterium.core)
(set! *warn-on-reflection* true)

; - - - - - - - - - -
; The formulas namespace contains mathematical formulas used for similarity
; calculations between two users. Each function takes the user's id and one
; of it's friend's ids to calculate a similarity score between each of those
; two people based on which a book will be recommended for the user to read.
; - - - - - - - - - -

;********** Euclidean distance **********

(defn euclid
  "The Euclidean distance between points p and q is the length of the line segment connecting them."
  [user friend]
  (let [books-both-read (filter user (keys friend))]
    (if (empty? books-both-read)
      (int 0)
      (let [distance (float (reduce #(+ %1 (float (Math/pow (- (int (user %2)) (int (friend %2))) (int 2)))) (int 0) books-both-read))]
        (/ (int 1) (+ distance (int 1)))))))

;********** Pearson correlation **********

(defn pearson
  "Pearson correlation measures the similarity in shape between two profiles.
   The formula for the Pearson Correlation distance is:
   d = 1 - r where r = Z(x)·Z(y)/n is the dot product of the z-scores of the vectors x and y.
   The z-score of x is constructed by subtracting from x its mean and dividing by its standard deviation."
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      (int 0)
      (let [number-of-books (int (count books-both-read))
            ; sums
            sum-user-ratings (reduce #(+ %1 (int (user %2))) (int 0) books-both-read)
            sum-friend-ratings (reduce #(+ %1 (int (friend %2))) (int 0) books-both-read)
            ; sum of squared values
            sum-squared-user-ratings (reduce #(+ %1 (Math/pow (int (user %2)) (int 2))) (int 0) books-both-read)
            sum-squared-friend-ratings (reduce #(+ %1 (Math/pow (int (friend %2)) (int 2))) (int 0) books-both-read)
            ; sum of their products
            sum-products (reduce #(+ %1 (* (int (user %2)) (int (friend %2)))) (int 0) books-both-read)
            covariance (float (- sum-products (/ (* sum-user-ratings sum-friend-ratings) number-of-books)))
            standard-deviation (float (Math/sqrt (* (- sum-squared-user-ratings (/ (Math/pow sum-user-ratings (int 2)) number-of-books))
                                                    (- sum-squared-friend-ratings (/ (Math/pow sum-friend-ratings (int 2)) number-of-books)))))]
        (if (= standard-deviation 0.0)
          (int 0)
          (float (/ covariance standard-deviation)))))))

;********** Manhattan distance **********

(defn manhattan-distance
  "The distance between two points measured along axes at right angles.
   In a plane with p1 at (x1, y1) and p2 at (x2, y2), it is |x1 - x2| + |y1 - y2|."
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      (int 0)
      (reduce #(+ %1 (Math/abs (- (int (user %2)) (int (friend %2))))) (int 0) books-both-read))))

;********** Spearman rank correlation **********

(defn spearman
  "Spearman Rank Correlation measures the correlation between two sequences of values.
   The two sequences are ranked separately and the differences in rank are calculated at each position, i."
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      (int 0)
      (let [distance (reduce #(+ %1 (Math/pow (- (int (user %2)) (int (friend %2))) (int 2))) (int 0) books-both-read)
            n (int (count books-both-read))
            denominator (* n (- (Math/pow n (int 2)) (int 1)))]
        (if (= denominator (float 0.0))
          (int 0)
          (Math/abs (- (int 1) (/ (* (int 6) distance) denominator))))
        ))))

;********** Chebyshev distance **********

(defn chebyshev
  "The Chebyshev distance is a metric defined on a vector space where the distance
   between two vectors is the greatest difference along any coordinate dimension."
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      (int 0)
      (let [vector-list (reduce #(conj %1 (Math/abs (- (int (user %2)) (int (friend %2))))) [] books-both-read)]
        (apply max vector-list))
        )))

;********** Jaccard coefficient and distance **********

(defn jaccard-index
  "The Jaccard coefficient measures similarity between finite sample sets,
   and is defined as the size of the intersection divided by the size of the union of the sample sets."
  [user friend]
  (let [first-person (reduce #(into %1 %2) #{} user)
        second-person (reduce #(into %1 %2) #{} friend)]
    (if (empty? (clojure.set/union first-person second-person))
           (int 0)
           (/ (int (count (clojure.set/intersection first-person second-person)))
              (int (count (clojure.set/union first-person second-person)))))))

;********** Sorensen-Dice coefficient **********

(defn sorensen-dice
  "The Sorensen-dice coefficient is a statistic used for comparing the similarity of two samples."
  [user friend]
  (let [first-person (reduce #(into %1 %2) #{} user)
        second-person (reduce #(into %1 %2) #{} friend)
        n-first-person (int (count first-person))
        n-second-person (int (count second-person))]
    (if (= n-first-person n-second-person)
      (int 0)
      (/ (* (int 2) (int (count (clojure.set/intersection first-person second-person)))) (+ n-first-person n-second-person)))))