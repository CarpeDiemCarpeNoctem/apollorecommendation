(defproject librarian "0.1.0-SNAPSHOT"
  :description "Book recommendation application"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-oauth "1.5.2"]
                 [clj-http "1.1.2"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler librarian.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [hiccup "1.0.2"]
                        [midje "1.7.0"]]}})
