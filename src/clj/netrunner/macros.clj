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

(defmacro req [& expr]
  `(fn ~['state 'side 'card 'args]
     (let ~['runner '(:runner state)
            'corp '(:corp state)
            'run '(:run state)
            'corp-reg '(get-in state [:corp :register])
            'runner-reg '(get-in state [:runner :register])
            'target '(first (:targets args))
            'tagged '(or (> (:tagged runner) 0) (> (:tag runner) 0))]
        ~@expr)))

(defmacro gfn [name args & expr]
  (let [params (vec (concat ['state 'side] args))
        full-args (conj args 'options)]
    `(defn ~name
       (~params (~name ~@(conj params nil)))
       (~(conj params 'options)
        (if (~'allowed? ~'state ~'side ~(keyword name) ~@full-args)
          (-> ~'state
              ~@expr
              (~'trigger-event ~'side ~(keyword name) ~@full-args))
          ~'state)))))

(defmacro afn [name & expr]
  `(defn ~(symbol (str "resolve-" name)) ~['state 'side 'ability 'card 'options]
     (if-let [~'v (~(keyword name) ~'ability)]
       ~@expr
       ~'state)))
