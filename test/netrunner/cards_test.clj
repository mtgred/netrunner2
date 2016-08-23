(ns netrunner.cards-test
  (:use expectations)
  (:require [netrunner.init-test :refer [corp runner]]
            [netrunner.core-test :refer [state create-card add-card]]
            [netrunner.macros :refer [effect]]
            [netrunner.core :as c]))

(def state2 (assoc-in state [:corp :click] 4))

(let [card (create-card state2 :corp "Blue Level Clearance" :hand)
      new-state (-> state2
                    (add-card :corp card)
                    (c/play :corp {:card card}))]
  (expect 8 (get-in new-state [:corp :credit]))
  (expect 2 (get-in new-state [:corp :click]))
  (expect 0 (count (get-in new-state [:corp :play-area])))
  (expect 7 (count (get-in new-state [:corp :hand])))
  (expect 1 (count (get-in new-state [:corp :discard]))))

(let [card (create-card state2 :corp "Hedge Fund" :hand)
      new-state (-> state2
                    (add-card :corp card)
                    (c/play :corp {:card card}))]
  (expect 9 (get-in new-state [:corp :credit]))
  (expect 3 (get-in new-state [:corp :click]))
  (expect 5 (count (get-in new-state [:corp :hand])))
  (expect 1 (count (get-in new-state [:corp :discard]))))

(let [card (create-card state2 :corp "Closed Accounts" :hand)
      s (add-card state2 :corp card)
      new-state (-> s
                    (assoc-in [:runner :tag] 1)
                    (c/play :corp {:card card}))
      new-state2 (-> s
                     (c/play :corp {:card card}))]
  (expect 0 (get-in new-state [:runner :credit]))
  (expect 4 (get-in new-state [:corp :credit]))
  (expect 5 (get-in new-state2 [:runner :credit]))
  (expect 5 (get-in new-state2 [:corp :credit])))
