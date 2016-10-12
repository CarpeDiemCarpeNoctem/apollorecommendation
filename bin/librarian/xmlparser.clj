(ns librarian.xmlparser
  (:require [oauth.client :as oauth]
            [clojure.xml :only ['parse]]
            [librarian.oauthcon :as ocon]
            ))

(defn get-friends-xml
  [id]
  (:body (ocon/get-friends id)))

(defn parse-xml
  [s]
  (clojure.xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn get-friends
  [parsedxml]
  (:content (-> parsedxml :content second)))

(defn list-friends
  [friends-vec]
  (for [user friends-vec]
    (first (:content (first (:content user))))))

(defn user-book-reviews
  [userid]
  (format "http://www.goodreads.com/review/list?v=2&key=%s&id=%s&sort=votes&per_page=1000&order=d" "your-api-key" userid))

(defn extract-tag
  [tag coll]
  (let [n (first coll)]
    (if (= tag (:tag n))
        (:content n)
        (extract-tag tag (rest coll)))))

(defn book-ratings
  [url]
  (let [reviews (extract-tag :reviews (:content (clojure.xml/parse url)))]
    (map (fn [review]
           (let [book-review (:content review)
                 book-id (first (extract-tag :id (extract-tag :book book-review)))
                 book-rating (int (. Integer parseInt (first (extract-tag :rating book-review))))]
             [(keyword book-id) book-rating]))
         reviews)))

(defn users-book-ratings
  [list-of-users]
  (for [user list-of-users] (book-ratings (user-book-reviews user))))

(defn create-ratings
  [list-of-users]
  (let [keyword-ids (map keyword list-of-users)]
    (zipmap keyword-ids (users-book-ratings list-of-users))))