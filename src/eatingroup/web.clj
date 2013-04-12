(ns eatingroup.web
  (:use
   aleph.http
   compojure.core)
  (:require
   [compojure.route :as route]
   [eatingroup.api :as api]
   [eatingroup.view :as view]))

(defroutes app-routes
  (GET "/" [] view/page)
  (GET "/api" [] (wrap-aleph-handler api/socket-handler))
  (route/resources "/static")
  (route/not-found "page not found"))

(defn start-server [_]
  (start-http-server
   (wrap-ring-handler app-routes)
   {:port 8008 :websocket true}))
