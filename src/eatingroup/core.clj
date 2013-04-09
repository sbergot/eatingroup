(ns eatingroup.core
  (:use
   lamina.core
   aleph.http
   compojure.core)
  (:require
   [compojure.route :as route]
   eatingroup.view))

(defn hello-world [channel request]
  (let [params (:route-params request)
        id (:id params)]
    (enqueue channel
             {:status 200
              :headers {"content-type" "text/html"}
              :body (str "Hello World! " id)})))

(defroutes app-routes
  (GET "/" [] eatingroup.view/page)
  (GET "/toto/:id" [id] (wrap-aleph-handler hello-world))
  (route/resources "/static")
  (route/not-found "page not found"))

(defn -main []
  (start-http-server (wrap-ring-handler app-routes) {:port 8008}))
