(ns librarian.core
  (:require [goog.events :as events]
            [goog.dom :as dom]))

(defn loader
  []
  (let [image (atom "<img src='images/spinner.gif'>")
        button  (dom/getElement "recommendbutton")
        display (dom/getElement "load")]
    (events/listen button "click"
                   (fn [event]
                       (set! (.-innerHTML display) @image)))))

(loader)