(defproject comkov "0.1.0-SNAPSHOT"
  :description "comment markov"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                [clj-http "2.0.0"]
                [org.clojure/data.codec "0.1.0"]
                [cheshire "5.5.0"]]
  :plugins [[lein-cljfmt "0.3.0"]]
  :main comkov.core)
