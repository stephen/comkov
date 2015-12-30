(ns comkov.core
  (:gen-class)
  (:require [clj-http.client :as client])
  (:require [uritemplate-clj.core :as templ]))

(def current-user (System/getenv "USERNAME"))
(def current-user-password (System/getenv "PASSWORD"))

(def github-options
  {:basic-auth [current-user current-user-password]
   :accept "application/vnd.github.v3+json"
   :as :json})

(defn fetch-user-repos-url
  "Fetch the URL that returns a list of the current user's repositories"
  []
  (get-in
   (client/get "https://api.github.com/user" github-options)
   [:body :repos_url]))

(def default-per-page 100)

(defn build-pagination-options
  "build http client options for paginating with per_page and page"
  ([page] (build-pagination-options page default-per-page))
  ([page per-page]
    {:query-params
      {"per_page" per-page
       "page" page}}))

(defn fetch-pr-comment-urls-for-repos
  ([] (fetch-pr-comment-urls-for-repos nil))
  ([page] (fetch-pr-comment-urls-for-repos (fetch-user-repos-url) nil))
  ([repos-url page]
    (let
      [response
        (client/get repos-url (merge github-options (build-pagination-options page)))]
      {:next_page_url (get-in response [:links :next :href])
       :comments_urls (map
                           #(templ/uritemplate (get % :pulls_url) {"number" "comments"})
                           (:body response))})))

(defn fetch-pr-comments
  ([pr-comments-url] (fetch-pr-comments pr-comments-url nil))
  ([pr-comments-url, page]
    (let
      [response
        (client/get pr-comments-url (merge github-options (build-pagination-options page)))]
      {:next_page_url (get-in response [:links :next :href])
       :comments (map #(:body %) (filter #(= (get-in % [:user :login]) current-user)
                                         (:body response)))})))

(defn -main
  []
  (let [urls (:comments_urls (fetch-pr-comment-urls-for-repos))]
    (let [comments (map #(fetch-pr-comments %) urls)]
      (println (count comments))
      (println comments))))
