(ns librarian.views
  (:require [hiccup.page :as hic-p]
            [librarian.xmlparser :as xmlparser]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "My Librarian: " title)]
   (hic-p/include-css "/css/style.css")
   ])

(defn home-page
  []
  (hic-p/html5
   (gen-page-head "Ask for a book recommendation")
   [:h1 {:id "maintitle"} "My Librarian"]
   [:div {:id "intro"}
    "Welcome to My Librarian book recommendation service"]
   [:form {:action "/recommendation" :method "POST"}
    [:div {:id "search"}
     [:input {:id "searchfield" :type "text" :name "goodreadsid"}]]
    [:div {:id "searchbutton"}
     [:input {:id "recommendbutton" :type "submit" :value "Recommend a book"}]]]))


(defn recommendation-page
  [{:keys [goodreadsid]}]
  (hic-p/html5
    (gen-page-head "Recommendation")
    [:h1 "Recommendation:"]
    [:p "Your goodreads id is:" goodreadsid]))