(ns netrunner.core
  (:require [netrunner.utils :refer [set-zone remove-card]]))

(defn card-init [state side card]
  state)

(defn init-game [state]
  (-> state
      (card-init :corp (get-in state [:corp :identity]))
      (card-init :runner (get-in state [:runner :identity]))))

(defn update-values [state side deltas f]
  (loop [ds (partition 2 deltas)
         s state]
    (if (empty? ds)
      s
      (let [d (first ds)]
          (recur (rest ds)
                 (update-in s [side (first d)] #(f % (last d))))))))

(defn gain [state side & deltas]
  (update-values state side deltas #(+ (or %1 0) %2)))

(defn lose [state side & deltas]
  (update-values state side deltas #(if (= %2 :all)
                                      0
                                      (- (or %1 0) %2))))

(defn move
  ([state side card to] (move state side card to nil))
  ([state side {:keys [zone] :as card} to args]
   (-> state
       (update-in [side to] #(conj (vec %) (assoc card :zone to)))
       (update-in zone #(remove-card card %)))))

(defn- move-cards [state side from to n]
  (let [moved (set-zone to (take n (get-in state [side :deck])))]
     (-> state
         (update-in [side to] #(concat % moved))
         (update-in [side from] #(drop n %)))))

(defn draw
  ([state side] (draw state side 1))
  ([state side n]
   (move-cards state side :deck :hand n)))

(defn mill
  ([state side] (mill state side 1))
  ([state side n]
   (move-cards state side :deck :discard n)))
