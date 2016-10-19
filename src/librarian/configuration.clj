(ns librarian.configuration
  (:require [librarian.formulas :as formulas]))

; Goodreads Configuration
; - - - - - - - - - -

;  - Goodreads API key:

(def api-key "your-api-key")

;  - Goodreads API address for user's book review list:

(def user-book-reviews "http://www.goodreads.com/review/list?v=2&key=%s&id=%s&sort=votes&per_page=1000&order=d")

;  - Goodreads API address for information about a book:

(def book-info "https://www.goodreads.com/book/show/%s?format=xml&key=%s")

; OAuth Configuration
; - - - - - - - - - -

;  - Consumer configuration:

(def consumer-key "your-consumer-key")
(def consumer-secret "your-consumer-secret")

;  - OAuth configuration:

(def oauth-token "your-oauth-token")
(def oauth-secret "your-oauth-secret")

; Formula Configuration:
; - - - - - - - - - -

(def formula 
  formulas/euclid
  ;formulas/pearson
  ;formulas/manhattan
  ;formulas/spearman
  ;formulas/chebyshev
  ;formulas/jaccard-index
  ;formulas/sorensen-dice
)