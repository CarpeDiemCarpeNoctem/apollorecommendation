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

