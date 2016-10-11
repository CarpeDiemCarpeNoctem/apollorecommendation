(ns librarian.xmlparser
  (:require [oauth.client :as oauth]
            [clojure.xml :only ['parse]]
            [librarian.oauthcon :as ocon]
            ))

(defn get-friends-xml
  [id]
  (ocon/get-friends id))

(defn parse-xml
  [s]
  (clojure.xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))