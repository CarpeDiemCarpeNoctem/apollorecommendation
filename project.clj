(defproject librarian "0.1.0-SNAPSHOT"
  :description "Book recommendation application"
  :url "TBA"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-oauth "1.5.2"]
                 [clj-http "1.1.2"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [org.clojure/clojurescript "0.0-3190"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.1"]
                 [criterium "0.4.3"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-cljsbuild "1.1.0"]
            [lein-midje "3.2-RC4"]]
  :cljsbuild {
    :builds [{
        :source-paths ["srccljs"]
        :compiler {
          :output-to "resources/public/js/main.js"
          :optimizations :whitespace
          :pretty-print true}}]}
  :ring {:handler librarian.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [hiccup "1.0.2"]
                        [midje "1.7.0"]
                        ]}})