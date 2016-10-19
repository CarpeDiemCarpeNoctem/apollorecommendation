(ns librarian.configuration
  (:require [librarian.formulas :as formulas]))

; - - - - - - - - - -
; The configuration namespace contains configuration settings for the application.
; It is composed of three major sections:
; Goodreads Configuration, OAuth Configuration and Formula Configuration
; Each of them has its own sub-configuration sections for configuring specific aspects
; of each of the three major sections.
; - - - - - - - - - -

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

;  - Similarity rating calculation formula:
;    - Choose from the following list of mathematical formulas:
;    -- Uncoment the one that you would like to use and comment
;    -- out all the other ones.

(def formula 
  formulas/euclid
  ;formulas/pearson
  ;formulas/manhattan
  ;formulas/spearman
  ;formulas/chebyshev
  ;formulas/jaccard-index
  ;formulas/sorensen-dice
)