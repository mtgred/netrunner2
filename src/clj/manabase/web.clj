(ns manabase.web
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources not-found]]))

(defn page404 []
  (html5
   [:head
    [:title "Not found"]]
   [:body
    [:h1 "Not found"]]))

(defn index [req]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Jinteki"]
    (include-css "css/style.css")]
   [:body
    [:div#app]

    (include-js "js/app.js")]))

(defroutes app
  (GET "/" [] index)
  (resources "/")
  (not-found (page404)))
