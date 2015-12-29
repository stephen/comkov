(ns comkov.core
  (:gen-class)
  (:require [clj-http.client :as client]))

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
  (get
    (client/get (fetch-user-repos-url) github-headers)
    :body))

(defn -main []
  (println (get-repos)))
