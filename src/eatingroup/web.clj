(ns eatingroup.web
  (:use
   lamina.core
   aleph.http
   compojure.core)
  (:require
   [compojure.route :as route]
   [eatingroup.api :as api]
   [eatingroup.view :as view]))

(defn socket-handler [ch handshake]
  (receive-all ch #(enqueue ch (api/dispatch-message %))))

(defroutes app-routes
  (GET "/" [] view/page)
  (GET "/api" [] (wrap-aleph-handler socket-handler))
  (route/resources "/static")
  (route/not-found "page not found"))

(defn start-server [_]
  (start-http-server
   (wrap-ring-handler app-routes)
   {:port 8008 :websocket true}))
