(ns user
  (:require [reloaded.repl :refer [system reset stop go set-init!]]
            [manabase.system]))

(set-init! #'manabase.system/create-system)

