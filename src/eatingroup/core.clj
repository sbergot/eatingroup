(ns eatingroup.core
  (:require [eatingroup.web :as web]))
(defonce server (atom nil))

(defn initialize []
  (if @server
    (println "Warning: already initialized")
    (do (println "Starting server")
        (swap! server web/start-server))))

(defn shutdown []
  (when @server
    (do (println "Shutting down web server")
        (@server)
        (swap! server (fn [_] nil)))))

(defn reinitialize []
  "Run this on the REPL to reload web.clj and restart the web server"
  (shutdown)
  (require :reload-all 'eatingroup.core)
  (initialize))


(defn -main []
  (initialize))
