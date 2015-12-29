(ns netrunner.core
  (:require [reagent.core :refer [render]]
            [re-frame.core :refer [dispatch-sync]]
            [netrunner.subscriptions]
            [netrunner.handlers]
            [netrunner.auth :refer [auth-forms]]
            [netrunner.topbar :refer [topbar]]
            [netrunner.chat :refer [chat]]
            [netrunner.announcements :refer [announcements]]
            [netrunner.cardbrowser :refer [cardbrowser]]
            [netrunner.deckbuilder :refer [deckbuilder]]
            [netrunner.gamelobby :refer [gamelobby]]
            [netrunner.gameboard :refer [gameboard]]
            [netrunner.about :refer [about]]))

(enable-console-print!)

(defn app []
  [:div
   [topbar]
   [auth-forms]
   [:div.carousel.slide {:data-interval "false"}
    [:div.carousel-inner
     [:div.item.active
      [:div.home-bg]
      [announcements]
      [chat]]

     [:div.item
      [:div.deckbuilder-bg]
      [deckbuilder]]

     [:div.item
      [:div.lobby-bg]
      [:div.container
       [gamelobby]]
      [gameboard]]

     [:div.item
      [:div.cardbrowser-bg]
      [cardbrowser]]

     [:div.item
      [:div.about-bg]
      [about]]]]])

(dispatch-sync [:initialize])
(render [app] (.getElementById js/document "app"))
