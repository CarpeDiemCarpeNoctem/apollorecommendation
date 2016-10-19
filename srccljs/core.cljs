(ns librarian.core
  (:require [goog.events :as events]
            [goog.dom :as dom]))

; - - - - - - - - - -
; The core namespace contains clojurescript functions
; that compile to javascript to help with user interaction
; on the front-end of the book recommendation application
; - - - - - - - - - -

(defn validation-loader
  "Checks if the input field on the front-end has valid input and loads a spinner image to show work
   in progress. Otherwise it shows a warning message if the input is not valid."
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