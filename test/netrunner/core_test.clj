(ns netrunner.core-test
  (:use expectations)
  (:require [netrunner.init-test :refer [corp runner]]
            [netrunner.macros :refer [effect]]
            [netrunner.init :refer [create-game card-data]]
            [netrunner.core :as c]))

(def state (create-game corp runner))

(defn create-card [state side cardname zone]
  (assoc (card-data cardname) :zone [:corp :hand] :cid 99))

(defn add-card [state side card]
  (update-in state [side :hand] conj card))

(let [new-state (c/draw state :corp)]
  (expect 6 (count (get-in new-state [:corp :hand])))
  (expect 43 (count (get-in new-state [:corp :deck])))
  (expect (:cid (last (get-in new-state [:corp :hand])))
          (:cid (first (get-in state [:corp :deck])))))

(let [new-state (c/mill state :corp 2)]
  (expect 2 (count (get-in new-state [:corp :discard])))
  (expect 42 (count (get-in new-state [:corp :deck])))
  (expect (:cid (last (get-in new-state [:corp :discard])))
          (:cid (second (get-in state [:corp :deck])))))

(let [new-state (c/gain state :runner :memory 1 :credit 3 :tag 2)]
  (expect 5 (get-in new-state [:runner :memory]))
  (expect 8 (get-in new-state [:runner :credit]))
  (expect 2 (get-in new-state [:runner :tag])))

(let [new-state (c/lose state :runner :credit :all :memory 2)]
  (expect 0 (get-in new-state [:runner :credit]))
  (expect 2 (get-in new-state [:runner :memory])))

(let [new-state ((effect (c/draw 3) (c/gain :credit 1)) state :corp nil nil)]
  (expect 8 (count (get-in new-state [:corp :hand])))
  (expect 6 (get-in new-state [:corp :credit])))

(let [card (create-card state :corp "Blue Level Clearance" :hand)
      s (add-card state :corp card)
      new-state (c/move state :corp card :discard)]
  (expect 6 (count (get-in s [:corp :hand])))
  (expect 5 (count (get-in new-state [:corp :hand])))
  (expect 1 (count (get-in new-state [:corp :discard])))
  (expect (:cid card) (:cid (first (get-in new-state [:corp :discard])))))

(expect false (c/can-pay? state :runner [:click 1]))
(expect false (c/can-pay? state :runner [:credit 2 :credit 4]))
(expect false (c/can-pay? state :corp [:credit 1 :tag 2 :credit 3]))
(expect true (c/can-pay? state :runner [:credit 1 :memory 5 :credit 3]))

(let [ability {:costs [:credit 2 :memory 1]
               :msg "draw 3 cards and gain 4 [Credits]"
               :effect (effect (c/draw 3) (c/gain :credit 4))}
      new-state (c/res state :runner {:title "foobar"} ability)]
  (expect [{:user "__system__", :text "Karen uses foobar to draw 3 cards and gain 4 [Credits]."}]
          (get-in new-state [:log]))
  (expect 3 (get-in new-state [:runner :memory]))
  (expect 8 (count (get-in new-state [:runner :hand])))
  (expect 7 (get-in new-state [:runner :credit])))
