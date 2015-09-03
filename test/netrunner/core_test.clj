(ns netrunner.core-test
  (:use expectations)
  (:require [netrunner.init-test :refer [corp runner]]
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
