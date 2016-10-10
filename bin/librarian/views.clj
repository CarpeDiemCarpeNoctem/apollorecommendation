(ns librarian.views
  (:require [hiccup.page :as hic-p]))

(defn home-page
  []
  (hic-p/html5
   [:h1 {:id "maintitle"} "My Librarian"]
   [:div {:id "intro"}
    "Welcome to My Librarian book recommendation service"]))