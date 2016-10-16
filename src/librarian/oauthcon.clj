(ns librarian.oauthcon
  (:require [oauth.client :as oauth]
            [clj-http.client :as http]))

(def consumer-key "your-consumer-key")
(def consumer-secret "your-consumer-secret")

(def consumer (oauth/make-consumer consumer-key
                                   consumer-secret
                                   "https://www.goodreads.com/oauth/request_token"
                                   "https://www.goodreads.com/oauth/access_token"
                                   "https://www.goodreads.com/oauth/authorize"
                                   :hmac-sha1))

(def request-token (oauth/request-token consumer nil))

(oauth/user-approval-uri consumer
                         (:oauth_token request-token))

(defn get-user-id []
  (let [url "https://www.goodreads.com/api/auth_user"
        credentials (oauth/credentials consumer
                                       (:oauth_token {:oauth_token "your-oauth-token",
                                                      :oauth_token_secret
                                                      "your-oauth-token-secret"})
                                       (:oauth_token_secret {:oauth_token "your-oauth-token",
                                                      :oauth_token_secret
                                                      "your-oauth-token-secret"})
                                       :GET
                                       url)]
    (http/get url {:query-params credentials})))

(defn get-friends [user-id]
  (let [url (str "https://www.goodreads.com/friend/user/" user-id)
        params {:format "xml"}
        credentials (oauth/credentials consumer
                                       (:oauth_token {:oauth_token "your-oauth-token", 
                                                      :oauth_token_secret "your-oauth-secret"})
                                       (:oauth_token_secret {:oauth_token "your-oauth-token", 
                                                             :oauth_token_secret "your-oauth-secret"})
                                       :GET
                                       url
                                       params)]
    (http/get url {:query-params (merge credentials params)})))