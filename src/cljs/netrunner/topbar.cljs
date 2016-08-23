(ns netrunner.topbar
  (:require-macros [secretary.core :refer [defroute]])
  (:require [re-frame.core :refer [subscribe dispatch]]
            [secretary.core :refer [dispatch!] :as secretary]
            [goog.events :as events]
            [netrunner.common :refer [user-view event-handler]])
  (:import [goog.history EventType]
           [goog.history Html5History]))

(def history (doto (Html5History.)
               (.setUseFragment false)
               (.setPathPrefix "")
               (events/listen EventType.NAVIGATE #(dispatch! (.-token %)))
               (.setEnabled true)))

(defroute "/" [token]
  (dispatch [:go-to-page "/"]))

(defroute "/:token" [token]
  (dispatch [:go-to-page (str "/" token)]))

(defn nav-link [text href]
  (let [active-page (subscribe [:active-page])]
    (fn []
      ^{:key href}
      [:a {:href href
           :class (when (= @active-page href) "active")
           :on-click (event-handler #(.setToken history href))}
       text])))

(defn logged-menu [user]
  [:div.dropdown.usermenu
   [:a.dropdown-toggle {:href "" :data-toggle "dropdown"}
    [user-view user 22]
    [:b.caret]]
   [:div.dropdown-menu.blue-shade.float-right
    [:a.block-link {:href "/logout"} "Logout"]]])

(defn game-menu [games]
  (let [spectators (:spectators @games)
        c (count spectators)]
    (fn []
      [:div
       (when (pos? c)
         [:div (str c " Spectators" (when (> c 1) "s"))
          [:div.blue-shade.spectators
           (for [s spectators]
             [user-view s 22])]])
       [:a {:on-click #(dispatch [:leave-game])} "Leave game"]])))

(defn topbar []
  (let [user (subscribe [:user])
        games (subscribe [:games])
        active-game (subscribe [:active-game])]
    (fn []
      [:div.topbar.blue-shade
       [:div.main-menu
        [nav-link "Jinteki" "/"]
        [nav-link "Deck Builder" "/deckbuilder"]
        [nav-link "Play" "/play"]
        [nav-link "Cards" "/cards"]
        [nav-link "About" "/about"]]
       [:div.side-menu.float-right
        (when @active-game
          [game-menu active-game])
        (let [c (count @games)]
          [:div (str c " Game" (when (> c 1) "s"))])
        (if @user
          [logged-menu user]
          [:div
           [:a {:href "" :data-target "#register-form" :data-toggle "modal"} "Sign up"]
           [:a {:href "" :data-target "#login-form" :data-toggle "modal"} "Login"]])]])))
