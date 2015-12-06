(ns netrunner.core
  (:require [netrunner.utils :refer [set-zone remove-card]]
            [netrunner.macros :refer [gfn afn]]))

(defn card-init [state side card]
  state)

(defn trigger-event [state side event & args]
  state)

(defn init-game [state]
  (-> state
      (card-init :corp (get-in state [:corp :identity]))
      (card-init :runner (get-in state [:runner :identity]))))
(defn allowed? [state side action & args]
  true)

(defn say [state side {:keys [user text]}]
  (let [author (or user (get-in state [side :user]))]
    (update-in state [:log] #(conj % {:user author :text text}))))

(defn system-msg [state side text]
  (let [username (get-in state [side :user :username])]
    (say state side {:user "__system__" :text (str username " " text ".")})))

(afn msg (let [msg (if (string? v)
                     v
                     (v state side card args))]
           (system-msg state side (str "uses " (:title card) " to " msg))))

(afn effect (v state side card args))


(gfn res [card ability] args nil
     (resolve-msg side card ability args)
     (resolve-effect side card ability args))

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


(gfn move [{:keys [zone] :as card} to] args nil
     (update-in [side to] #(conj (vec %) (assoc card :zone to)))
     (update-in zone #(remove-card card %)))

(defn- move-cards [state side from to n]
  (let [moved (set-zone to (take n (get-in state [side :deck])))]
     (-> state
         (update-in [side to] #(concat % moved))
         (update-in [side from] #(drop n %)))))

(gfn card-init [card] args nil)

(gfn draw [] n 1
     (move-cards side :deck :hand n))

(gfn mill [] n 1
     (move-cards side :deck :discard n))

(gfn play-instant [card] args nil)

(gfn corp-install [card] args nil)

(gfn runner-install [card] args nil)
