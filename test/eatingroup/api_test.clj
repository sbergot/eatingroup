(ns eatingroup.api-test
  (:use clojure.test
        eatingroup.api))

(defn create-user [id]
  (eatingroup.api.User. id id nil))

(defn create-group [id]
  (eatingroup.api.Group. id id 12 #{}))

(def InitState
  {:groups {}
   :users {}})

(deftest a-test
  (testing "publish group should return the same structure"
    (is (=
         (publish-group-mock
          {:description "toto desc"
           :time 15}
          "toto")
         {"name" "addGroup"
          "data" {"group" {"description" "toto desc"
                           "time" 15
                           "members" "me"}}}))))

(deftest group-tests
  (testing "group add/fetch"
    (let [group (create-group "kebab")
          state-with-group (set-fld groups group InitState)
          state-without-group (rem-fld groups (:id group) state-with-group)
          not-found (get-fld groups (:id group) InitState)
          found (get-fld groups (:id group) state-with-group)
          not-found-bis (get-fld groups (:id group) state-without-group)]
      (is (= not-found nil))
      (is (= found group))
      (is (= not-found-bis nil)))))

(deftest user-tests
  (testing "user add/fetch"
    (let [user (create-user "Tom")
          state-with-user (set-fld users user InitState)
          state-without-user (rem-fld users (:id user) state-with-user)
          not-found (get-fld users (:id user) InitState)
          found (get-fld users (:id user) state-with-user)
          not-found-bis (get-fld users (:id user) state-without-user)]
      (is (= not-found nil))
      (is (= found user))
      (is (= not-found-bis nil)))))

(def PopulatedState
  (->> InitState
       (set-fld groups (create-group "id1"))
       (set-fld groups (create-group "id2"))
       (set-fld users (create-user "Tom"))
       (set-fld users (create-user "John"))
       (set-fld users (create-user "David"))))

(deftest test-state
  (testing "check populated state"
    (is (= "John" (:name (get-fld users "John" PopulatedState))))
    (is (= "David" (:name (get-fld users "David" PopulatedState))))
    (is (= "Tom" (:name (get-fld users "Tom" PopulatedState))))))

(deftest user-manipulation-tests
  (testing "add/remove user to a group"
    (let [group (->> PopulatedState
                     (add-user "Tom" "id1")
                     (get-fld groups "id1"))]
      (is (= #{"Tom"} (:members group))))
    (let [group (->> PopulatedState
                     (add-user "Tom" "id1")
                     (add-user "John" "id1")
                     (get-fld groups "id1"))]
      (is (= #{"Tom" "John"} (:members group))))
    (let [group (->> PopulatedState
                     (add-user "Tom" "id1")
                     (add-user "John" "id1")
                     (add-user "Tom" "id1")
                     (get-fld groups "id1"))]
      (is (= #{"Tom" "John"} (:members group))))
    (let [group (->> PopulatedState
                     (add-user "Tom" "id1")
                     (remove-user "Tom" "id1")
                     (get-fld groups "id1"))]
      (is (= nil group)))))

(deftest user-join-tests
  (testing "let user join a group"
    (let [new-state (->> PopulatedState
                     (join "Tom" "id1"))
          group (get-fld groups "id1" new-state)
          user (get-fld users "Tom" new-state)]
      (is (= #{"Tom"} (:members group)))
      (is (= "id1" (:group user))))
    (let [new-state (->> PopulatedState
                     (join "Tom" "id1")
                     (join "Tom" "id2"))
          not-found (get-fld groups "id1" new-state)
          group (get-fld groups "id2" new-state)
          user (get-fld users "Tom" new-state)]
      (is (= nil not-found))
      (is (= #{"Tom"} (:members group)))
      (is (= "id2" (:group user))))

    ))
