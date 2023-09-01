(defproject blog "0.1.0-SNAPSHOT"
  :description "Gabriel Marin's blog"
  :url "https://gbrlmarn.io"
  :license {:name "MIT License"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [stasis "2023.06.03"]
                 [markdown-clj "1.11.4"]
                 [hiccup "2.0.0-RC1"]
                 [ring "1.11.0-alpha1"]]
  :ring {:handler blog.core/server}
  :profiles {:dev {:plugins [[lein-ring "0.12.6"]]}}
  :repl-options {:init-ns blog.core}
  :aliases {"build-site" ["run" "-m" "blog.core/export!"]})
