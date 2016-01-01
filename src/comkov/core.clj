(ns comkov.core
  (:gen-class)
  (:require [tentacles.pulls :as github-pulls])
  (:require [tentacles.repos :as github-repos]))

(def current-user (System/getenv "USERNAME"))
(def current-user-password (System/getenv "PASSWORD"))
(def auth-map {:auth (str current-user ":" current-user-password)})

(defn -main
  []
  (let [repos (github-repos/repos (merge auth-map {:all-pages true}))]
    (println (str (count repos) " repos"))
    (let [owner (get-in repo-repr [:owner :login])
          repo (:name repo-repr)
          comments (map #(github-pulls/repo-comments owner repo {:all-pages true})
                        repos)]
      (println (str (count comments) "comments for " repo))
      (println (comments)))))
