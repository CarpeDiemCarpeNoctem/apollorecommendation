(ns librarian.oauthcon
  (:require [oauth.client :as oauth]
            [librarian.configuration :as config]
            [clj-http.client :as http]))

(def consumer (oauth/make-consumer config/consumer-key
                                   config/consumer-secret
                                   "https://www.goodreads.com/oauth/request_token"
                                   "https://www.goodreads.com/oauth/access_token"
                                   "https://www.goodreads.com/oauth/authorize"
                                   :hmac-sha1))

(def request-token (oauth/request-token consumer nil))

(oauth/user-approval-uri consumer
                         (:oauth_token request-token))

; Returns an XML file of all user's friends through OAuth connection

(defn get-friends [user-id]
  (let [url (str "https://www.goodreads.com/friend/user/" user-id)
        params {:format "xml"}
        credentials (oauth/credentials consumer
                                       (:oauth_token {:oauth_token config/oauth-token, 
                                                      :oauth_token_secret config/oauth-secret})
                                       (:oauth_token_secret {:oauth_token config/oauth-token, 
                                                             :oauth_token_secret config/oauth-secret})
                                       :GET
                                       url
                                       params)]
    (http/get url {:query-params (merge credentials params)})))