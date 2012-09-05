(ns biscuit.core-test
  (:use clojure.test
        biscuit.core))


(deftest crc8
  (testing "CRC-8 implementation"
    (is (= (checksum "payload") 91))))
