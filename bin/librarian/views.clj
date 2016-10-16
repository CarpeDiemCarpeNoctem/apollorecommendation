(ns librarian.views
  (:require [hiccup.page :as hic-p]
            [librarian.xmlparser :as xmlparser]
            [librarian.algorithms :as algorithms]))

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
  (let [ratings (-> (xmlparser/get-friends-xml (str goodreadsid))
               xmlparser/parse-xml
               xmlparser/get-friends
               xmlparser/list-friends
               (#(conj % (str goodreadsid)))
               xmlparser/create-ratings)
        result (-> (algorithms/recommend-books ratings (keyword goodreadsid) algorithms/euclid)
                 algorithms/sort-by-value 
                 algorithms/get-highest-rated-book 
                 algorithms/parse-book-xml
                 algorithms/recommended-book-info)]
  (hic-p/html5
    (gen-page-head "Recommendation")
    [:h1 "Recommendation:"]
    [:p (format (str (:title result)))]
    [:p (format (str (:description result)))]
    )))