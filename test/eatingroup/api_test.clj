(ns eatingroup.api-test
  (:use clojure.test
        eatingroup.api))

(defn create-user [id]
  (eatingroup.api.User. id
                        id
                        nil))

(defn create-group [id]
  (eatingroup.api.Group. id
                         id
                         12
                         #{}))

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
          not-found (get-fld groups (:id group) InitState)
          found (get-fld groups (:id group) state-with-group)]
      (is (= not-found nil))
      (is (= found group)))))

(deftest user-tests
  (testing "user add/fetch"
    (let [user (create-user "Tom")
          state-with-user (set-fld users user InitState)
          not-found (get-fld users (:id user) InitState)
          found (get-fld users (:id user) state-with-user)]
      (is (= not-found nil))
      (is (= found user)))))

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
    (let [user-added (add-user "Tom" "id1" PopulatedState)
          populated-group (get-fld groups "id1" user-added)]
      (is (= #{"Tom"} (:members populated-group))))))
