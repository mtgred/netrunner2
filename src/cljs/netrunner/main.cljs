(ns netrunner.main
  (:require [reagent.core :refer [render render-component] :as r]))

(defn app []
  [:h1 "Reagent"])

(render app (js/document.getElementById "app"))
