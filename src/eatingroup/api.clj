(ns eatingroup.api
  (:use
   [lamina.core :only [enqueue channel receive-all]]
   [lamina.api :only [bridge]])
  (:require [clj-json.core :as json]))

(defonce broadcast-ch (channel))

(defn register-connection [ch]
  (bridge ch broadcast-ch))

(defn publish-group [data]
  {"name" "addGroup"
   "data"
   {"group"
    {"description" (data "description")
     "time" (data "time")
     "members" "me"}}})

(defonce api-map
  {"publish_group" publish-group})

(defn dispatch-message [raw_message]
  (do
    (println raw_message)
    (let [{name "name" data "data"} (json/parse-string raw_message)]
      (json/generate-string ((api-map name) data)))))

(defn socket-handler [ch handshake]
  (receive-all ch #(enqueue ch (dispatch-message %))))
