(ns netrunner.macros)

(defmacro effect [& expr]
  `(fn ~['state 'side 'card 'targets]
     ~(let [actions (map #(if (#{:runner :corp} (second %))
                            (concat [(first %) (second %)] (drop 2 %))
                            (concat [(first %) 'side] (rest %)))
                         expr)]
        `(let ~['runner '(:runner state)
                'corp '(:corp state)
                'target '(first targets)]
           (-> ~'state ~@actions)))))
