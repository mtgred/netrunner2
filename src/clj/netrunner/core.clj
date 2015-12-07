(ns netrunner.core
  (:require [netrunner.utils :refer [set-zone remove-card merge-costs]]
            [netrunner.macros :refer [gfn afn effect]]))

(defn card-init [state side card]
  state)

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
                     (v state side card args))]
           (system-msg state side (str (when-let [t (:title card)]
                                         (str "uses " t))
                                       " to " msg))))

(afn effect (v state side card args))


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

(defn res
  ([state side card ability] (res state side card ability nil))
  ([state side card {:keys [req costs] :as ability} args]
   (if (and (can-pay? state side costs)
            (or (not req) (req state side card args)))
     (-> state
         (pay side costs)
         (resolve-msg side card ability args)
         (resolve-effect side card ability args))
     state)))

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
