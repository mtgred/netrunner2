(ns netrunner.utils)

(defn find-first [f coll]
  (->> coll
       (filter f)
       first))

(defn to-keyword [s]
  (when (string? s)
    (keyword (.toLowerCase s))
    s))

(defn set-zone [z coll]
  (map #(assoc % :zone z) coll))

(defn merge-costs [costs]
  (vec (reduce #(let [key (first %2) value (last %2)]
              (assoc %1 key (+ (or (key %1) 0) value)))
           {} (partition 2 (flatten costs)))))

(defn has? [card property value]
  (when-let [p (property card)]
    (> (.indexOf p value) -1)))

(defn key-map
  ([key coll]
   (into {} (for [item coll] [(key item) item])))
  ([key f coll]
   (into {} (for [item coll] [(key item) (f item)]))))

(defn remove-card [card coll]
  (let [[head tail] (split-with #(not= (:cid %) (:cid card)) coll)]
    (vec (concat head (rest tail)))))
