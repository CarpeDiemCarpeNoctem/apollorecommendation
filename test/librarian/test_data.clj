(ns librarian.test-data)

(def sample-parsed-xml {:tag :GoodreadsResponse,
 :attrs nil,
 :content
 [{:tag :Request,
   :attrs nil,
   :content
   [{:tag :authentication,
     :attrs nil,
     :content ["true"]}
    {:tag :key,
     :attrs nil,
     :content
     ["aaaaaaaaaaaaaaaaaaaaaaaaa"]}
    {:tag :method,
     :attrs nil,
     :content ["friend_user"]}]}
  {:tag :friends,
   :attrs
   {:start "1", :end "2", :total "2"},
   :content
   [{:tag :user,
     :attrs nil,
     :content
     [{:tag :id,
       :attrs nil,
       :content ["11111111"]}
      {:tag :friends_count,
       :attrs nil,
       :content ["4"]}
      {:tag :reviews_count,
       :attrs nil,
       :content ["78"]}]}
    {:tag :user,
     :attrs nil,
     :content
     [{:tag :id,
       :attrs nil,
       :content ["22222222"]}
      {:tag :friends_count,
       :attrs nil,
       :content ["4"]}
      {:tag :reviews_count,
       :attrs nil,
       :content ["50"]}]}]}]})

(def sample-get-friends [{:attrs nil, :content [{:attrs nil, :content ["11111111"], :tag :id} {:attrs nil, :content ["4"], :tag :friends_count} {:attrs nil, :content ["78"], :tag :reviews_count}], :tag :user} {:attrs nil, :content [{:attrs nil, :content ["22222222"], :tag :id} {:attrs nil, :content ["4"], :tag :friends_count} {:attrs nil, :content ["50"], :tag :reviews_count}], :tag :user}])