(ns eatingroup.core
  (:use
   lamina.core
   aleph.http
   compojure.core)
  (:require
   [compojure.route :as route]
   [clj-json.core :as json]
   eatingroup.view))

;; api
(defn publish-group [data]
  {"name" "addGroup"
   "data" {"group" {"description" "yaya"
                    "time" "never"
                    "members" "me"}}})

(defonce api-map
  {"publish_group" publish-group})

(defn dispatch-message [raw_message]
  (do
    (println raw_message)
    (let [{name "name" data "data"} (json/parse-string raw_message)]
      (json/generate-string ((api-map name) data)))))

;; web app
(defn group-handler [ch handshake]
  (receive-all ch #(enqueue ch (dispatch-message %))))

(defroutes app-routes
  (GET "/" [] eatingroup.view/page)
  (GET "/api" [] (wrap-aleph-handler group-handler))
  (route/resources "/static")
  (route/not-found "page not found"))

; server management
(defonce server (atom nil))

(defn initialize []
  (if @server
    (println "Warning: already initialized")
    (let [port 8008]
      (println (format "Starting http://localhost:%s/" port))
      (swap! server (fn [_] (start-http-server
                             (wrap-ring-handler app-routes)
                             {:port port :websocket true}))))))

(defn shutdown []
  (when @server
    (do
      (println "Shutting down web server")
      (@server)
      (swap! server (fn [_] nil)))))

(defn reinitialize []
  "Run this on the REPL to reload web.clj and restart the web server"
  (shutdown)
  (use :reload-all 'eatingroup.core)
  (use :reload-all 'eatingroup.view)
  (initialize))


(defn -main []
  (initialize))
