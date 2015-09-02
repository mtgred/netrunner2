(defproject manabase "0.1.0-SNAPSHOT"
  :main manabase.system
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [com.stuartsierra/component "0.2.3"]
                 [http-kit "2.1.19"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [danlentz/clj-uuid "0.1.6"]
                 [cheshire "5.5.0"]
                 [reagent "0.5.0"]]

  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test"]

  :profiles {:dev {:plugins [[lein-cljsbuild "1.0.6"]
                             [lein-figwheel "0.3.7"]
                             [lein-expectations "0.0.7"]
                             [lein-autoexpect "1.6.0"]]
                   :dependencies [[expectations "2.1.2"]
                                  [reloaded.repl "0.1.0"]]
                   :source-paths ["dev"]
                   :repl-options {:init-ns user
                                  :init (go)}
                   :cljsbuild {:builds [{:source-paths ["src/cljs" "dev"]
                                         :figwheel true
                                         :compiler {:output-to "target/classes/public/js/app.js"
                                                    :output-dir "target/classes/public/out"
                                                    :main manabase.main
                                                    :asset-path "out"
                                                    :optimizations :none
                                                    :recompile-dependents true
                                                    :source-map true}}]}}})
