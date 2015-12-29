(ns netrunner.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub :active-page (fn [db _] (reaction (:active-page @db))))
(register-sub :user (fn [db _] (reaction (:user @db))))
(register-sub :games (fn [db _] (reaction (:games @db))))
(register-sub :active-game (fn [db _] (reaction (:active-game @db))))
