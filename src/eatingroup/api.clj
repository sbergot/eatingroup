(ns eatingroup.api
  (:use
   [lamina.core :only [enqueue channel receive-all]]
   [lamina.api :only [bridge]]
   [clojure.walk :only [keywordize-keys]])
  (:require [clj-json.core :as json]
            [eatingroup.groups :as grp]))

;; state & primitive operations

(defonce GlobalState
  (atom (grp/new-state)))

;; channel management

(defonce broadcast-ch (channel))

(defn register-connection [ch]
  (bridge ch broadcast-ch))

;; glue with the socket server
(defn publish-group-mock [group user]
  "fake response to demonstrate ui"
  {"name" "addGroup"
   "data"
   {"group"
    {"description" (group :description)
     "time" (group :time)
     "members" "me"}}})

(defonce api-map
  {:publish_group publish-group-mock})

(defn dispatch-message [raw-message]
  (do
    (println raw-message)
    (let [{name :name
           data :data
           user :user}
          (-> raw-message json/parse-string keywordize-keys)]
      (json/generate-string ((api-map name) data user)))))

(defn socket-handler [ch handshake]
  (receive-all ch #(enqueue ch (dispatch-message %))))
