(ns librarian.core
  (:require [goog.events :as events]
            [goog.dom :as dom]))

(defn validation-loader
  []
  (let [image (atom "<img src='images/spinner.gif'>")
        warning (atom "Please check your input")
        button  (dom/getElement "recommendbutton")
        display (dom/getElement "load")]
    (events/listen button "click"
                   (fn [event]
                     (if (.checkValidity (dom/getElement "searchfield"))
                       (set! (.-innerHTML display) @image)
                       (set! (.-innerHTML display) @warning))))))

(validation-loader)