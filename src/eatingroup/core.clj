(ns eatingroup.core
  (:use
   lamina.core
   aleph.http
   compojure.core)
  (:require
   [compojure.route :as route]
   [clj-json.core :as json]
   eatingroup.view))

(defn hello-world [channel request]
  (let [params (:route-params request)
        id (:id params)]
    (enqueue channel
             {:status 200
              :headers {"content-type" "text/html"}
              :body (str "Hello World! " id)})))

(defn group-handler [ch handshake]
  (do
   (enqueue
    ch
    (json/generate-string
     {"name" "message"
      "data" {"msg" "hello you"}}))
   (receive
    ch
    (fn [_]
      (enqueue
       ch
       (json/generate-string
        {"name" "message"
         "data" {"msg" "hello again"}}))))
   (receive
    ch
    (fn [_]
      (enqueue
       ch
       (json/generate-string
        {"name" "message"
         "data" {"msg" "hello the third"}}))))))

(defroutes app-routes
  (GET "/" [] eatingroup.view/page)
  (GET "/toto/:id" [id] (wrap-aleph-handler hello-world))
  (GET "/api" [] (wrap-aleph-handler group-handler))
  (route/resources "/static")
  (route/not-found "page not found"))

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
