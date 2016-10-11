(ns librarian.algorithms)

(defn euclid
  [p1 p2]
  (let [si (filter p1 (keys p2))]
    (if (empty? si)
      0
      (let [squares-sum (reduce #(+ %1 (Math/pow (- (p1 %2) (p2 %2)) 2)) 0 si)]
        (/ 1 (+ squares-sum 1))))))
