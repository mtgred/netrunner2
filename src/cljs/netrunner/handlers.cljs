(ns netrunner.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :refer [register-handler dispatch]]
            [netrunner.ajax :refer [GET]]
            [cljs.core.async :refer [<!]]))

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

(register-handler :set-data
                  (fn [db [id key value]]
                    (assoc db key value)))

(register-handler :fetch
                  (fn [db [id key url]]
                    (go (dispatch [:set-data key (:json (<! (GET url)))]))
                    db))

