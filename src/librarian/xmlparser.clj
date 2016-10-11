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