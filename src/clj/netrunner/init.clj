(ns netrunner.init
  (:require [clj-uuid :as uuid]
            [cheshire.core :refer [parse-string]]
            [netrunner.utils :refer [to-keyword set-zone key-map]]))

(defn trim-card [card]
  (-> card
      (dissoc :_id :setname :influence :number :uniqueness :influencelimit :faction :factioncost
              :minimumdecksize :text)
      (assoc :side (to-keyword (:side card)))))

(def card-data (key-map :title trim-card (parse-string (slurp "data/cards.json") true)))

(defn parse-deck [deck]
  (let [side (to-keyword (:side deck))]
    (mapcat #(map (fn [card]
                    (assoc (card-data card) :cid (uuid/v1) :zone [side :deck]))
                  (repeat (:qty %) (:card %)))
            (:cards deck))))

(defn create-player [side player]
  (let [cards (shuffle (parse-deck (:deck player)))
        data {:user (:user player)
               :identity (if-let [card (get-in player [:deck :identity])]
                           (trim-card card)
                           {:side side :type "Identity"})
               :deck (set-zone [side :deck] (drop 5 cards))
               :hand (set-zone [side :hand] (take 5 cards))
               :discard []
               :scored []
               :rfg []
               :play-area []
               :click 0
               :credit 5
               :agenda-point 0
               :max-hand-size 5
               :keep false}]
    (if (= side :corp)
      (assoc data
             :servers {:hq {} :rd{} :archives {} :remote []}
             :bad-publicity 0
             :click-per-turn 3)
      (assoc data
             :rig {:program [] :resource [] :hardware []}
             :run-credit 0
             :memory 4
             :link 0
             :hq-access 1
             :rd-access 1
             :tag 0
             :tagged 0
             :brain-damage 0
             :click-per-turn 4))))

(defn create-game [corp runner]
  {:gameid (uuid/v1)
   :log []
   :active-player :runner
   :end-turn true
   :corp (create-player :corp corp)
   :runner (create-player :runner runner)})
