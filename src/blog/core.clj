(ns blog.core
  (:require [clojure.string :as str]
            [stasis.core :as stasis]
            [markdown.core :as md]
            [hiccup.page :as hiccup]
            [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.export]
            [optimus.strategies :refer [serve-live-assets]]))

(def source-dir "resources")

;; Functions
(defn get-assets []
  (assets/load-assets "public" ["/marincv.pdf"]))

(defn key-to-html [s]
  (str/replace s #".md" ".html"))

(defn read-and-convert! [src]
  (let [data  (stasis/slurp-directory src #".*\.md$")
        html-paths (map key-to-html (keys data))
        html-content (map md/md-to-html-string (vals data))]
    (zipmap html-paths html-content)))

(defn apply-header-footer [page]
  (hiccup/html5 {:lang "en"}
    [:head
     [:title "Gabriel Marin"]
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     [:link {:type "text/css"
             :href "/css/style.css"
             :rel "stylesheet"}] ;; css
     [:body
      [:div {:class "header"}
       [:div {:class "name"}
        [:a {:href "/"} "(Gabriel Marin)"]
        [:div {:class "header-right"}
         [:a {:href "/posts"} "Posts"]]]]
      page]
     [:footer
      [:p "Copyright © 2023-2024 Gabriel Marin"]]]))

(defn format-pages [m]
  (let [html-keys (keys m)
        page-data (map apply-header-footer (vals m))]
    (zipmap html-keys page-data)))

(apply-header-footer
 (read-and-convert! source-dir))

(defn get-css [src]
  (stasis/slurp-directory src #".*\.css$"))

(defn merge-website-assets! [root-dir]
  (let [page-map (format-pages (read-and-convert! source-dir))
        css-map (get-css source-dir)]
    (stasis/merge-page-sources {:css css-map
                                :pages page-map})))

(defn get-pages []
  (merge-website-assets! source-dir))

;; Rendering
(def optimize optimizations/all)

(def server
  (-> (stasis/serve-pages (get-pages))
      (optimus/wrap get-assets optimize serve-live-assets)))

;; Exporting
(def out-dir "docs")

(defn export! []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! out-dir)
    (optimus.export/save-assets assets out-dir)
    (stasis/export-pages
     (get-pages) out-dir
     {:optimus-assets assets})
    (println "Website is ready!")))
