(ns blog.core
  (:require [blog.layout :as layout]
            [blog.resources :as resources]
            [stasis.core :as stasis]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.export]
            [optimus.strategies :refer [serve-live-assets]]))

;; Rendering
(def optimize optimizations/all)

(def server
  (-> (stasis/serve-pages (resources/get-pages))
      (optimus/wrap resources/get-assets optimize serve-live-assets)))

;; Exporting
(def out-dir "docs")


(defn export! []
  (let [assets (optimize (resources/get-assets) {})]
    (stasis/empty-directory! out-dir)
    (optimus.export/save-assets assets out-dir)
    (stasis/export-pages
     (resources/get-pages) out-dir
     {:optimus-assets assets})
    (println "Website is ready!")))
