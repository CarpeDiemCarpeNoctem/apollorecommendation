(ns librarian.algorithms)

(defn si [p1 p2] (filter p1 (keys p2)))

(defn euclid
  [p1 p2]
  (if (empty? (si p1 p2))
    (int 0)
    (let [squares-sum (float (reduce #(+ %1 (float (Math/pow (- (int (p1 %2)) (int (p2 %2))) (int 2)))) (int 0) (si p1 p2)))]
      (/ (int 1) (+ squares-sum (int 1))))))