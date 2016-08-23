(ns netrunner.common)

(defn event-handler [f]
  (fn [e]
    (.preventDefault e)
    (apply f [e])))

(defn user-view [user size]
  [:div.user-view
   [:img.avatar {:src (str "http://www.gravatar.com/avatar/" (:emailhash @user) "?d=retro&s=" size)}]
   [:span (:username @user)]])
