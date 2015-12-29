(defproject manabase "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [com.stuartsierra/component "0.2.3"]
                 [http-kit "2.1.19"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [danlentz/clj-uuid "0.1.6"]
                 [re-frame "0.6.0"]
                 [secretary "1.2.3"]
                 ;; [cheshire "5.5.0"]
                 [reagent "0.5.1"]]

  :source-paths ["src/clj" "src/cljs"]

  :test-paths ["test"]

  :clean-targets ^{:protect false} ["target"]

  :main manabase.system

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler manabase.web/app}

  :profiles {:dev {:plugins [[lein-cljsbuild "1.1.1"]
                             [lein-figwheel "0.5.0-2"]
                             [lein-expectations "0.0.8"]
                             [lein-autoexpect "1.7.0"]]
                   :dependencies [[expectations "2.1.2"]
                                  [reloaded.repl "0.1.0"]]
                   :source-paths ["dev"]
                   :repl-options {:init-ns user
                                  :init (go)}
                   :cljsbuild {:builds [{:source-paths ["src/cljs"]
                                         :figwheel true
                                         :compiler {:output-to "resources/public/js/app.js"
                                                    :output-dir "resources/public/cljs"
                                                    :main netrunner.core
                                                    :asset-path "cljs"
                                                    :optimizations :none
                                                    :recompile-dependents true
                                                    :source-map true}}]}}})
