(ns netrunner.init-test
  (:use expectations)
  (:require [cheshire.core :refer [parse-string]]
            [netrunner.init :refer [trim-card parse-deck create-player create-game]]))

(def corp-deck (parse-string (slurp "data/corp-deck.json") true))
(def runner-deck (parse-string (slurp "data/runner-deck.json") true))

(def corp {:user {:username "mtgred" :emailhash "foo"}
           :deck corp-deck})

(def runner {:user {:username "Karen" :emailhash "bar"}
             :deck runner-deck})

(expect "Corp" (:side corp-deck))

(let [trimmed-card (trim-card (:identity corp-deck))]
  (expect "Haas-Bioroid: Engineering the Future" (:title trimmed-card))
  (expect nil (:number trimmed-card)))

(expect 49 (count (parse-deck corp-deck)))
(expect 45 (count (parse-deck runner-deck)))

(let [state (create-game corp runner)
      cards (:cards state)]
  (expect "mtgred" (get-in state [:corp :user :username]))
  (expect "Andromeda: Dispossessed Ristie" (get-in state [:runner :identity :title]))
  (expect 5 (count (get-in state [:corp :hand])))
  (expect 44 (count (get-in state [:corp :deck])))
  (expect [:corp :hand] (:zone (last (get-in state [:corp :hand]))))
  (expect [:runner :deck] (:zone (first (get-in state [:runner :deck])))))
