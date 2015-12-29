(ns netrunner.handlers
  (:require [re-frame.core :refer [register-handler]]))

(def init-db
  {:active-page "/"
   :active-game nil})

(register-handler :initialize
                  (fn [db _]
                    (merge db init-db)))
