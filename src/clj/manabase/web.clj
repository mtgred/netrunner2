(ns manabase.web
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [clojure.data.json :as json]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources]]))


(defn index [req]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Jinteki"]
    (include-css "css/carousel.css")
    (include-css "css/manabase.css")]
   [:body
    [:div#app]
    (include-js "lib/jquery/jquery.min.js")
    (include-js "lib/bootstrap/dist/js/bootstrap.min.js")
    (include-js "js/app.js")
    [:audio#ting
     [:source {:src "/sound/ting.mp3" :type "audio/mp3"}]
     [:srouce {:src "/sound/ting.mp3" :type "audio/ogg"}]]]))

(def cards (json/read-str (slurp "data/cards.json")))

(defroutes app
  (resources "/")
  (GET "/data/cards" [] (json/write-str cards))
  (GET "*" [] index))

