(ns librarian.views
  (:require [hiccup.page :as hic-p]
            [hiccup.element :as hic-e :refer [link-to]]
            [librarian.ratings :as ratings]
            [librarian.recommendation :as recommendation]
            [librarian.configuration :as config]))

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
    [:div {:id "enhanced"}
     [:label {:for "enhance-algorithm"} "Use enhanced algorithm: "] [:input {:type "checkbox", :name "enhance", :id "enhance-algorithm", :value "yes", :checked false}]]
    [:div {:id "searchbutton"}
     [:input {:id "recommendbutton" :type "submit" :value "Recommend a book"}]]]
   [:div {:id "load"}]
   (hic-p/include-js "/js/main.js")))


(defn recommendation-page
  [{:keys [goodreadsid enhance]}]
  (let [math-formula config/formula
        ratings (if (= "yes" enhance)
                    (-> (ratings/get-friends-xml (str goodreadsid))
                      ratings/parse-xml
                      ratings/get-friends
                      ratings/list-user-and-extended-friends
                      ratings/create-ratings)
                    (-> (ratings/get-friends-xml (str goodreadsid))
                      ratings/parse-xml
                      ratings/get-friends
                      ratings/list-friends
                      (#(conj % (str goodreadsid)))
                      ratings/create-ratings))
        result (-> (recommendation/recommend-books ratings (keyword goodreadsid) math-formula)
                 recommendation/sort-by-value 
                 recommendation/get-highest-rated-book 
                 recommendation/recommended-book-xml
                 recommendation/parse-book-xml
                 recommendation/recommended-book-info)]
  (hic-p/html5
    (gen-page-head "Recommendation")
    [:h1 "Here's your recommended book:"]
    [:p (format (str (:title result)))]
    [:p (format (str (:description result)))]
    [:p (hic-e/link-to (format (str (:alink result))) "More details")]
    )))