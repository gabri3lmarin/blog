(defproject blog "0.1.0-SNAPSHOT"
  :description "Gabriel Marin's blog"
  :url "https://gbrlmarn.io"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [stasis "2023.06.03"]
                 [markdown-clj "1.11.4"]
                 [hiccup "2.0.0-RC1"]
                 [ring "1.11.0-alpha1"]]
  :ring {:handler blog.core/server}
  :profiles {:dev {:plugins [[lein-ring "0.12.6"]]}}
  :repl-options {:init-ns blog.core}
  :aliases {"build-site" ["run" "-m" "blog.core/export!"]})
