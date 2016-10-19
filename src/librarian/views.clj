(ns librarian.views
  (:require [hiccup.page :as hic-p]
            [hiccup.element :as hic-e :refer [link-to]]
            [librarian.ratings :as ratings]
            [librarian.recommendation :as recommendation]
            [librarian.configuration :as config]))

(use 'criterium.core)
(set! *warn-on-reflection* true)

(defn gen-page-head
  [title]
  [:head
   [:title (str "Apollo book recommendation: " title)]
   (hic-p/include-css "/css/style.css")
   ])

(def header-links
  [:div#header-links
   [:a {:href "/"} "Recommend again"]
   ])

(defn home-page
  []
  (hic-p/html5
   (gen-page-head "Ask for a book recommendation")
   [:h1 {:id "maintitle"} "Apollo"]
   [:div {:id "intro"}
    "Welcome to Apollo book recommendation service"]
   [:div {:id "quote"}
    "“I find television very educating. Every time somebody turns on the set, I go into the other room and read a book.” - Groucho Marx"]
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
  (try
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
      header-links
      [:h1 "Here's your recommended book:"]
      [:p {:class "description-title"} (format (str (:title result)))]
      [:p {:class "description"} (format (str (:description result)))]
      [:p {:class "description"} (hic-e/link-to (format (str (:alink result))) "More details")]
      ))
   (catch Exception e
     (hic-p/html5
       (gen-page-head "Recommendation")
       header-links
       [:h1 "Oops, something went wrong"]
       [:p {:class "description"} "There was an error with your request. Please make sure you are using a valid Goodreads id."]
       [:p {:class "description"} (hic-e/link-to "http://www.goodreads.com" "Create a Goodreads ID or log in")]))))