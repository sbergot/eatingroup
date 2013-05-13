(ns eatingroup.api
  (:use
   [lamina.core :only [enqueue channel receive-all]]
   [lamina.api :only [bridge]]
   [clojure.walk :only [keywordize-keys]]
   [clojure.core.incubator :only [dissoc-in]])
  (:require [clj-json.core :as json]))

;; state & primitive operations

(defonce GlobalState
  (atom
    {:groups {}
     :users {}}))

(defprotocol AStateFieldSet
  "Defines a list of in memory object"
  (get-fld [this id state]
    "retrieve the object by its id")
  (set-fld [this obj state]
    "creates the object or replace
     it and returns the new state")
  (rem-fld [this obj state]
    "remove the object and returns
     the new state"))

(deftype StateFieldSet [field-name]
  AStateFieldSet
  (get-fld [this id state]
    (get-in state [field-name id]))
  (set-fld [this obj state]
    (assoc-in state
      [field-name (:id obj)]
      obj))
  (rem-fld [this id state]
    (dissoc-in state [field-name id])))

;; user & group operations

(defrecord Group [id description time members])
(defrecord User [id name group])
(defonce groups (StateFieldSet. :groups))
(defonce users (StateFieldSet. :users))

(defn new-group [id description time]
  (Group. id description time #{}))

(defn update-members [group-id updater state]
  (let [group (get-fld groups group-id state)
        members (:members group)]
    (set-fld groups
         (assoc group :members
                (updater members))
         state)))

(defn add-user [user-id group-id state]
  (update-members
   group-id
   #(conj % user-id)
   state))

(defn remove-user [user-id group-id state]
  (let [new-state (update-members
                   group-id
                   #(disj % user-id)
                   state)
        group (get-fld groups group-id new-state)
        members (:members group)]
    (if (empty? members)
      (rem-fld groups group-id state)
      new-state)))

(defn join [user-id group-id state]
  (let [user (get-fld users user-id state)
        old-group-id (:group user)]
    (->> state
         (remove-user user-id old-group-id)
         (add-user user-id group-id)
         (set-fld users (assoc user :group group-id)))))

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
