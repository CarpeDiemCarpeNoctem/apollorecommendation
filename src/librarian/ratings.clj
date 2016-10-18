(ns librarian.ratings
  (:require [clojure.xml :only ['parse]]
            [librarian.oauthcon :as ocon]
            [librarian.configuration :as config]
            ))

(defn get-friends-xml
  "Establishes a connection with the API through OAuth and gets a list of user's friends"
  [id]
  (if-let [con (:body (ocon/get-friends id))]
    con
    nil))

(defn parse-xml
  [s]
  (clojure.xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn get-friends
  "Returns a vector of maps with information of all friends"
  [parsedxml]
  (if-let [friends (-> parsedxml :content second)]
    (if (< (int 0) (int (. Integer parseInt (-> friends :attrs :total))))
      (:content friends)
      nil)
    nil))

(defn list-friends
  "Returns a list of friends' ids as keywords"
  [friends-vec]
  (for [user friends-vec]
    (first (:content (first (:content user))))))

(defn user-book-reviews
  "Creates a url for an API call to user's book reviews XML file"
  [userid]
  (format config/user-book-reviews config/api-key userid))

(defn extract-tag
  "Returns contents of the tag from the provided collection"
  [tag coll]
  (let [n (first coll)]
    (if (= tag (:tag n))
        (:content n)
        (extract-tag tag (rest coll)))))

(defn book-ratings
  "Returns a list of vectors of book ids and ratings"
  [url]
  (let [reviews (extract-tag :reviews (:content (clojure.xml/parse url)))]
    (into {} (map (fn [review]
                    (let [book-review (:content review)
                          book-id (first (extract-tag :id (extract-tag :book book-review)))
                          book-rating (int (. Integer parseInt (first (extract-tag :rating book-review))))]
                      [(keyword book-id) book-rating]))
                  reviews))))

; make-keyword-list is a helper function used by create-ratings
; to make code more readable

(defn make-keyword-list
  "Returns a list of keywords from a given collection"
  [collection]
  (map keyword collection))

; - - - - - - - - - -

(defn users-book-ratings
  "Returns maps of key-value pairs of book ids and ratings that each user gave them"
  [list-of-users]
  (for [user list-of-users] (-> (user-book-reviews user) book-ratings)))

(defn create-ratings
  "Creates a map of ratings for each user mapped to maps of their book ids and the ratings they gave each book"
  [list-of-users]
  (let [keyword-ids (make-keyword-list list-of-users)]
    (zipmap keyword-ids (users-book-ratings list-of-users))))