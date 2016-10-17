(ns librarian.views
  (:require [hiccup.page :as hic-p]
            [librarian.ratings :as ratings]
            [librarian.recommendation :as recommendation]))

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
  (let [ratings (-> (ratings/get-friends-xml (str goodreadsid))
               ratings/parse-xml
               ratings/get-friends
               ratings/list-friends
               (#(conj % (str goodreadsid)))
               ratings/create-ratings)
        result (-> (recommendation/recommend-books ratings (keyword goodreadsid) recommendation/euclid)
                 recommendation/sort-by-value 
                 recommendation/get-highest-rated-book 
                 recommendation/recommended-book-xml
                 recommendation/parse-book-xml
                 recommendation/recommended-book-info)]
  (hic-p/html5
    (gen-page-head "Recommendation")
    [:h1 "Recommendation:"]
    [:p (format (str (:title result)))]
    [:p (format (str (:description result)))]
    )))