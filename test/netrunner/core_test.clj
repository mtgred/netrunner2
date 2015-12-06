(ns netrunner.core-test
  (:use expectations)
  (:require [netrunner.init-test :refer [corp runner]]
            [netrunner.macros :refer [effect]]
            [netrunner.init :refer [create-game]]
            [netrunner.core :as c]))

(def state (c/init-game (create-game corp runner)))

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

(let [ability {:msg "draw 3 cards and gain 2 [Credits]"
               :effect (effect (c/draw 3) (c/gain :credit 2))}
      new-state (c/res state :corp {:title "foobar"} ability)]
  (expect [{:user "__system__", :text "mtgred uses foobar to draw 3 cards and gain 2 [Credits]."}]
          (get-in new-state [:log]))
  (expect 8 (count (get-in new-state [:corp :hand])))
  (expect 7 (get-in new-state [:corp :credit])))
