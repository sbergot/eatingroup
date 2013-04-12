(ns eatingroup.api-test
  (:use clojure.test
        eatingroup.api))

(deftest a-test
  (testing "publish group should return the same structure"
    (is (=
         (publish-group
          {"description" "toto desc"
           "time" 15})
         {"name" "addGroup"
          "data" {"group" {"description" "toto desc"
                           "time" 15
                           "members" "me"}}}))))
