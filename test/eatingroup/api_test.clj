(ns eatingroup.api-test
  (:use clojure.test
        eatingroup.api))

(defonce InitState
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
    (let [group #eatingroup.api.Group{:id "id"
                                      :description "desc"
                                      :tim 18
                                      :members "me"}
          state-with-group (set-fld groups group InitState)
          not-found (get-fld groups group InitState)
          found (get-fld groups (:id group) state-with-group)]
      (is (= not-found nil))
      (is (= found group)))))
