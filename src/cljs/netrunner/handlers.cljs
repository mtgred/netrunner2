(ns netrunner.handlers
  (:require [re-frame.core :refer [register-handler]]))

(def init-db
  {:active-page "/"
   :active-game nil})

(register-handler :initialize
                  (fn [db _]
                    (merge db init-db)))

(register-handler :go-to-page
                  (fn [db [id token]]
                    (let [tokens #js ["/" "/deckbuilder" "/play" "/cards" "/about"]]
                      (.carousel (js/$ ".carousel") (.indexOf tokens token)))
                    ;; (try (js/ga "send" "pageview" token)
                    ;;      (catch js/Error e))
                    (assoc db :active-page token)))
