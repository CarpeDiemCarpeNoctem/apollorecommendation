(ns librarian.ratings
  (:require [clojure.xml :only ['parse]]
            [librarian.oauthcon :as ocon]
            [librarian.configuration :as config]
            ))

(defn get-friends-xml
  [id]
  (:body (ocon/get-friends id)))

(defn parse-xml
  [s]
  (clojure.xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn get-friends
  [parsedxml]
  (let [friends (-> parsedxml :content second)]
    (:content friends)))

(defn list-friends
  [friends-vec]
  (for [user friends-vec]
    (first (:content (first (:content user))))))

(defn user-book-reviews
  [userid]
  (format config/user-book-reviews config/api-key userid))

(defn extract-tag
  [tag coll]
  (let [n (first coll)]
    (if (= tag (:tag n))
        (:content n)
        (extract-tag tag (rest coll)))))

(defn book-ratings
  [url]
  (let [reviews (extract-tag :reviews (:content (clojure.xml/parse url)))]
    (into {} (map (fn [review]
                    (let [book-review (:content review)
                          book-id (first (extract-tag :id (extract-tag :book book-review)))
                          book-rating (int (. Integer parseInt (first (extract-tag :rating book-review))))]
                      [(keyword book-id) book-rating]))
                  reviews))))

(defn make-keyword-list
  [collection]
  (map keyword collection))

(defn users-book-ratings
  [list-of-users]
  (for [user list-of-users] (book-ratings (user-book-reviews user))))

(defn create-ratings
  [list-of-users]
  (let [keyword-ids (make-keyword-list list-of-users)]
    (zipmap keyword-ids (users-book-ratings list-of-users))))