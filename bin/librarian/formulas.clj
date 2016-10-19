(ns librarian.formulas)

(use 'criterium.core)
(set! *warn-on-reflection* true)

(defn euclid
  "The Euclidean distance between points p and q is the length of the line segment connecting them."
  [user friend]
  (let [books-both-read (filter user (keys friend))]
    (if (empty? books-both-read)
      (int 0)
      (let [distance (float (reduce #(+ %1 (float (Math/pow (- (int (user %2)) (int (friend %2))) (int 2)))) (int 0) books-both-read))]
        (/ (int 1) (+ distance (int 1)))))))

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

(defn manhattan-distance
  "The distance between two points measured along axes at right angles.
   In a plane with p1 at (x1, y1) and p2 at (x2, y2), it is |x1 - x2| + |y1 - y2|."
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      (int 0)
      (reduce #(+ %1 (Math/abs (- (int (user %2)) (int (friend %2))))) (int 0) books-both-read))))

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

(defn chebyshev
  [user friend]
  (let [books-both-read (filter user (keys friend))] 
    (if (empty? books-both-read)
      (int 0)
      (let [vector-list (reduce #(conj %1 (Math/abs (- (int (user %2)) (int (friend %2))))) [] books-both-read)]
        (apply max vector-list))
        )))

(defn jaccard-index
  [user friend]
  (let [first-person (reduce #(into %1 %2) #{} user)
        second-person (reduce #(into %1 %2) #{} friend)]
    (if (empty? (clojure.set/union first-person second-person))
           (int 0)
           (/ (int (count (clojure.set/intersection first-person second-person)))
              (int (count (clojure.set/union first-person second-person)))))))

(defn sorensen-dice
  [user friend]
  (let [first-person (reduce #(into %1 %2) #{} user)
        second-person (reduce #(into %1 %2) #{} friend)
        n-first-person (int (count first-person))
        n-second-person (int (count second-person))]
    (if (= n-first-person n-second-person)
      (int 0)
      (/ (* (int 2) (int (count (clojure.set/intersection first-person second-person)))) (+ n-first-person n-second-person)))))