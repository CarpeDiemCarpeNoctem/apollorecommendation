(ns librarian.views
  (:require [hiccup.page :as hic-p]))

(defn gen-page-head
  []
  [:head
   [:title (str "My Librarian")]
   (hic-p/include-css "/css/style.css")
   ])

(defn home-page
  []
  (hic-p/html5
   (gen-page-head)
   [:h1 {:id "maintitle"} "My Librarian"]
   [:div {:id "intro"}
    "Welcome to My Librarian book recommendation service"]
   [:form {:action "/" :method "POST"}
    [:div {:id "search"}
     [:input {:id "searchfield" :type "text" :name "goodreadsid"}]]
    [:div {:id "searchbutton"}
     [:input {:id "recommendbutton" :type "submit" :value "Recommend a book"}]]]))