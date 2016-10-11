(ns librarian.views
  (:require [hiccup.page :as hic-p]))

(defn gen-page-head
  []
  [:head
   [:title (str "My Librarian")]
   ])

(defn home-page
  []
  (hic-p/html5
   (gen-page-head)
   [:h1 {:id "maintitle"} "My Librarian"]
   [:div {:id "intro"}
    "Welcome to My Librarian book recommendation service"]))