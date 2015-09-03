(ns netrunner.utils)

(defn to-keyword [s]
  (when (string? s)
    (keyword (.toLowerCase s))
    s))

(defn set-zone [z coll]
  (map #(assoc % :zone z) coll))

(defn key-map
  ([key coll]
   (into {} (for [item coll] [(key item) item])))
  ([key f coll]
   (into {} (for [item coll] [(key item) (f item)]))))

(defn remove-card [card coll]
  (let [[head tail] (split-with #(not= (:cid %) (:cid card)) coll)]
    (vec (concat head (rest tail)))))
