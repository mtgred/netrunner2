(ns netrunner.core
  (:require [netrunner.utils :refer [set-zone remove-card merge-costs has? find-first to-keyword]]
            [netrunner.macros :refer [gfn afn effect]]))

(def cards (atom {}))

(defn defcard [name & r]
  (swap! cards assoc name (apply hash-map r)))

(defn card-def [card]
  (@cards (:title card)))

(defn trigger-event [state side event & args]
  state)

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
                     (v state side card options))]
           (system-msg state side (str (when-let [t (:title card)]
                                         (str "uses " t))
                                       " to " msg))))

(afn effect (v state side card options))

(defn can-pay? [state side costs]
  (every? #(or (>= (or (get-in state [side (first %)]) 0) (last %))
               (= (first %) :memory))
          (merge-costs costs)))

(defn update-values [state side deltas f]
  (loop [ds (partition 2 deltas)
         s state]
    (if (empty? ds)
      s
      (let [d (first ds)]
          (recur (rest ds)
                 (update-in s [side (first d)] #(f % (last d))))))))

(defn pay [state side costs]
  (update-values state side costs #(- (or %1 0) %2)))

(defn gain [state side & deltas]
  (update-values state side deltas #(+ (or %1 0) %2)))

(defn lose [state side & deltas]
  (update-values state side deltas #(if (= %2 :all)
                                      0
                                      (let [v (- (or %1 0) %2)]
                                        (if (= %1 :memory)
                                          v
                                          (max 0 v))))))

(defn card-init [state side card]
  (let [{:keys [in-play]} (card-def card)]
    (as-> state s
      (if in-play (apply gain s side in-play) state))))

(defn deactivate [state side card]
  (let [{:keys [in-play]} (card-def card)]
    (as-> state s
      (if in-play (apply lose s side in-play) state))))

(defn res
  ([state side ability] (res state side ability nil nil))
  ([state side ability card] (res state side ability card nil))
  ([state side {:keys [req costs additional-costs] :as ability} card options]
   (let [total-costs (concat costs additional-costs)]
     (if (and (can-pay? state side total-costs)
              (or (not req) (req state side card options)))
       (-> state
           (pay side total-costs)
           (resolve-msg side ability card options)
           (resolve-effect side ability card options))
       state))))

(defn move [state side card to]
  (let [zone (if (sequential? to)
               (concat [side] to)
               [side to])]
    (-> state
        (update-in zone #(conj (vec %) (assoc card :zone zone)))
        (update-in (:zone card) #(remove-card card %)))))

(defn- move-cards [state side from to n]
  (let [moved (set-zone to (take n (get-in state [side :deck])))]
     (-> state
         (update-in [side to] #(concat % moved))
         (update-in [side from] #(drop n %)))))

(gfn draw [n]
     (move-cards side :deck :hand n))

(gfn mill [n]
     (move-cards side :deck :discard n))

(gfn purge [])

(defn trash [state side card]
  (-> state
      (move side card :discard)
      (deactivate side card)))

(defn get-card [state card zone]
  (find-first #(= (:cid %) (:cid card)) (get-in state zone)))

(defn play-instant
  ([state side card] (play-instant state side card nil))
  ([state side card {:keys [extra-costs] :as options}]
   (let [cdef (@cards (:title card))
         ability (merge cdef {:costs (concat [:credit (:cost card)] extra-costs)})
         ability (update-in ability [:additional-costs]
                            concat (when (has? card :subtype "Double") [:click 1]))]
     (as-> state s
       (move s side card :play-area)
       (res s side ability card)
       (move s side (get-card s side card :play-area) :discard)))))

(gfn corp-install [card])

(gfn runner-install [card]
     (card-init side card)
     (move :runner card [:rig (if (:facedown options) :facedown (to-keyword (:type card)))]))

(gfn click-credit []
     (res side {:costs [:click 1] :effect (effect (gain :credit 1))}))

(gfn click-draw []
     (res side {:costs [:click 1] :effect (effect (draw 1))}))

(gfn click-purge []
     (res side {:costs [:click 3] :effect (effect (purge))}))

(gfn remove-tag []
     (res side {:costs [:click 1 :credit 2] :effect (effect (lose :tag 1))}))

(defn play
  ([state side card] (play state side card nil))
  ([state side card {:keys [server] :as options}]
   (let [cdef (@cards (:title card))]
     (case (:type card)
       ("Event" "Operation") (play-instant state side card {:extra-costs [:click 1]})
       ("Hardware" "Resource" "Program") (runner-install state side card {:extra-costs [:click 1]})
       ("ICE" "Upgrade" "Asset" "Agenda") (corp-install state side card {:extra-costs [:click 1] :server server})))))
