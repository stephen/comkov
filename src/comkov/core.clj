(ns comkov.core
  (:gen-class)
  (:require [clj-http.client :as client])
  (:require [uritemplate-clj.core :as templ]))

(def github-headers
  {:basic-auth [(System/getenv "USERNAME") (System/getenv "PASSWORD")]
   :accept "application/vnd.github.v3+json"
   :as :json})

(defn fetch-user-repos-url
  []
  (get-in
   (client/get "https://api.github.com/user" github-headers)
   [:body :repos_url]))

(defn fetch-urls-for-repos
  []
  (let [response (client/get (fetch-user-repos-url) github-headers)]
    {:next_page_url (get-in response [:links :next :href])
     :comments_urls (map
                     #(templ/uritemplate (get % :pulls_url) {"number" "comments"})
                     (:body response))}))

; (map
;       (get-in response [:body])
;       #(templ/uritemplate (get % :pulls_url)
;                           "number" "comments"))

(defn -main []
  (println (fetch-urls-for-repos)))
