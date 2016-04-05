(ns netrunner.cardbrowser
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]))

(defn image-url [card]
  (str "/img/cards/" (:code card) ".png"))

(defn cardbrowser []
  (let [cards (subscribe [:cards])]
    (fn []
      [:div.cardbrowser
       (for [card @cards]
         ^{:key (:code card)}
         [:div.card
          [:img {:src (image-url card)}]])])))

(dispatch [:fetch :cards "/data/cards"])
