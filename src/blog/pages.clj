(ns blog.pages
  (:require [blog.database :as database]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [datomic.api :as d]))

(comment

  (let [conn (database/db-conn)
        db (d/db conn)]
    (map #(hiccup/html [:li (flatten %)]) (d/q database/all-titles-q db)))

  
  (let [conn (database/db-conn)
        db (d/db conn)]
    (flatten
     (first (d/q database/all-titles-q db))))


  )
