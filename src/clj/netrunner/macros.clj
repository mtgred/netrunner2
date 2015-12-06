(ns netrunner.macros)

(defmacro effect [& expr]
  `(fn ~['state 'side 'card 'args]
     ~(let [actions (map #(if (#{:runner :corp} (second %))
                            (concat [(first %) (second %)] (drop 2 %))
                            (concat [(first %) 'side] (rest %)))
                         expr)]
        `(let ~['runner '(:runner state)
                'corp '(:corp state)
                'target '(first (:targets args))]
           (-> ~'state
               ~@actions)))))

(defmacro gfn [name args opt default & expr]
  (let [params (vec (concat ['state 'side] args))
        full-args (conj args opt)]
    `(defn ~name
       (~params (~name ~@(conj params default)))
       (~(conj params opt)
        (if (~'allowed? ~'state ~'side ~(keyword name) ~@full-args)
          (-> ~'state
              ~@expr
              (~'trigger-event ~'side ~(keyword name) ~@full-args))
          ~'state)))))

(defmacro afn [name & expr]
  `(defn ~(symbol (str "resolve-" name)) ~['state 'side 'card 'ability 'args]
     (if-let [~'v (~(keyword name) ~'ability)]
       (-> ~'state ~@expr)
       ~'state)))
