(ns librarian.ratings
  (:require [clojure.xml :only ['parse]]
            [librarian.oauthcon :as ocon]
            [librarian.configuration :as config]
            [clojure.core.async :refer [>! <!! go chan]]))

(use 'criterium.core)
(set! *warn-on-reflection* true)

; - - - - - - - - - -
; The ratings namespace contains functions that create a map of user ids
; mapped to maps of book ids and the ratings they gave them.
; This map is later used to calculate similarity scores between the user and
; each of their friends and make a book recommendation based on those scores.
; - - - - - - - - - -

; asynchronized is a helper function that creates asynchronous computation

(defn asynchronized
  "Asynchronously processes elements of a collection through a given function"
  [function coll]
  (if (nil? coll)
    '()
    (let [elements (filter #(not (nil? %)) coll)
          channels (repeatedly (count elements) chan)]
      (doseq [[channel element] (map vector channels elements)]
        (go (>! channel (function element))))
      (map <!! channels))))

; Entry point for algorithm: get-friends-xml

(defn get-friends-xml
  "Establishes a connection with the API through OAuth and gets a list of user's friends"
  [id]
  (if-let [con (:body (ocon/get-friends id))]
    con
    nil))

; - - - - - - - - - -

(defn parse-xml
  "Parses XML string"
  [^String s]
  (clojure.xml/parse
    (java.io.ByteArrayInputStream. (.getBytes s))))

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

; Enhanced algorithm that creates a list of all user's friends and firends of user's friends
; to give better recommendation results based on more people against which similarity scores
; can be calculated.
; The enhanced algorithm replaces the list-friends function and instead includes two functions:
; - list-user-and-extended-friends, vector-of-extended-friends
; -- vector-of-extended-friends is a helper function used in list-user-and-extended-friends

(defn vector-of-extended-friends
  "Returns a list of vectors with friends ids and their friends ids in them"
  [friends-list]
  (flatten (asynchronized #(-> (parse-xml (get-friends-xml (str %)))
                            get-friends
                            list-friends) friends-list)))

(defn list-user-and-extended-friends
  "Returns a list with user, user's friends and friends of user's friends"
  [friends-vec]
  (let [first-degree-friends (list-friends friends-vec)
        extended-friends (vector-of-extended-friends first-degree-friends)]
    (into (set first-degree-friends) extended-friends)))

; The following 3 functions are helper functions used by users-book-ratings function:
; - user-book-reviews, extract-tag, book-ratings

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
    (into {} (asynchronized (fn [review]
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
  (asynchronized #(-> (user-book-reviews %) book-ratings) list-of-users))

(defn create-ratings
  "Creates a map of ratings for each user mapped to maps of their book ids and the ratings they gave each book"
  [list-of-users]
  (let [keyword-ids (make-keyword-list list-of-users)]
    (zipmap keyword-ids (users-book-ratings list-of-users))))