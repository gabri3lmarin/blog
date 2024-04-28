(ns blog.resources
  (:require [blog.layout :as layout]
            [clojure.string :as str]
            [markdown.core :as md]
            [stasis.core :as stasis]
            [optimus.assets :as assets]))

(def source-dir "resources")

(defn get-assets []
  (assets/load-assets "public" ["/marincv.pdf"]))

(defn key-to-html [s]
  (str/replace s #".md" ".html"))

(defn read-and-convert! [src]
  (let [data  (stasis/slurp-directory src #".*\.md$")
        html-paths (map key-to-html (keys data))
        html-content (map md/md-to-html-string (vals data))]
    (zipmap html-paths html-content)))

(defn get-css [src]
  (stasis/slurp-directory src #".*\.css$"))

(defn format-pages [m]
  (let [html-keys (keys m)
        page-data (map layout/apply-header-footer (vals m))]
    (zipmap html-keys page-data)))

(defn merge-website-assets! [root-dir]
  (let [page-map (format-pages (read-and-convert! source-dir))
        css-map (get-css source-dir)]
    (stasis/merge-page-sources {:css css-map
                                :pages page-map})))

(defn get-pages []
  (merge-website-assets! source-dir))
