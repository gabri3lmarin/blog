(ns blog.posts
  (:require [blog.database :as database]
            [datomic.api :as d]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [markdown.core :as md]
   )
  (:import (java.time LocalDateTime)
           (java.time.format DateTimeFormatter)))

(defn get-posts-map [db]
  (map #(into {} %)
       (d/q database/posts-map-q db)))

(defn key-to-html [s]
  (str/replace s #".md" ".html"))

(defn format-posts-titles [paths titles]
  (hiccup/html
   [:h4
    [:a {:href
         (key-to-html paths)}
    titles]]))

(defn posts-titles-page [db]
  (let [posts-map (get-posts-map db)]
    (hiccup/html
        [:ul
         (map format-posts-titles
              (map #(:post/path %) posts-map)
              (map :post/title posts-map))])))

#_(defn get-posts-content-map []
  (let [conn (database/db-conn)
        db (d/db conn)
        html-content
        (map #(map md/md-to-html-string
                   (flatten %))
             (flatten (d/q database/all-posts-body-q db)))
        html-paths (map flatten
                        (d/q database/all-paths-q db))]
    (zipmap html-paths html-content)))

(comment

  (let [conn (database/db-conn)
        db (d/db conn)
        posts-map (map #(into {} %) (d/q database/posts-map-q db))]
    (map #(key-to-html (:post/path %)) posts-map)
    )

  (let [conn (database/db-conn)
        db (d/db conn)
        posts-map (map #(into {} %) (d/q database/posts-map-q db))]
    posts-map
    
    )

  )
