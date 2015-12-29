(ns comkov.core
  (:gen-class)
  (:require [clojure.data.codec.base64 :as base64]))

(:require [clojure.data.codec.base64])

(defn get-basic-auth
  []
  (let [username (System/getenv "USERNAME")
        password (System/getenv "PASSWORD")]
        (str username ":" password))
)

(defn base64-encode-string
  [input]
  (String (base64/encode (.getBytes input))))

(defn -main []
  (base64-encode "test"))
