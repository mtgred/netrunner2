(ns manabase.system
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]
            [manabase.web :refer [app]]))

(defn- start-server [handler port]
  (println (str "Server running on localhost:" port))
  (run-server handler {:port port}))

(defn- stop-server [server]
  (when server
    (server)))

(defrecord Manabase []
  component/Lifecycle
  (start [this]
    (assoc this :server (start-server #'app 1043)))
  (stop [this]
    (stop-server (:server this))
    (dissoc this :server)))

(defn create-system []
  (Manabase.))

(defn -main [& args]
  (.start (create-system)))
