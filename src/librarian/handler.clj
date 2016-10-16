(ns librarian.handler
  (:require [librarian.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  
  (GET "/"
       []
       (views/home-page))
  
  (POST "/recommendation"
        []
       (views/recommendation-page))
  
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc site-defaults :security false)))
