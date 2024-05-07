(ns blog.core
  (:require [blog.layout :as layout]
            [blog.resources :as resources]
            [blog.database :as database]
            [datomic.api :as d]
            [stasis.core :as stasis]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.export]
            [optimus.strategies :refer [serve-live-assets]]))

;; Rendering
(def optimize optimizations/all)

(def server
  (let [conn (database/db-conn)]
    (database/add-posts conn)
    (-> (stasis/serve-pages (resources/get-pages (d/db conn)))
        (optimus/wrap resources/get-assets optimize serve-live-assets))))

;; Exporting
(def out-dir "docs")

(defn export! []
  (let [assets (optimize (resources/get-assets) {})
        conn (database/db-conn)]
    (stasis/empty-directory! out-dir)
    (optimus.export/save-assets assets out-dir)
    (stasis/export-pages
     (resources/get-pages (d/db conn)) out-dir
     {:optimus-assets assets})
    (println "Website is ready!")))
