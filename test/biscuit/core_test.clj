(ns biscuit.core-test
  (:use clojure.test
        biscuit.core))

(deftest checksum
  (testing "CRC-1 implementation"
    (is (= (crc1 "biscuit") 243)))
  (testing "CRC-5 implementation"
    (is (= (crc5 "biscuit") 79)))
  (testing "CRC-8 1-Wire implementation"
    (is (= (crc8-1wire "biscuit") 151)))
  (testing "CRC-8 implementation"
    (is (= (crc8 "biscuit") 133)))
  (testing "CRC-16 implementation"
    (is (= (crc16 "biscuit") 56686)))
  (testing "CRC-24 implementation"
    (is (= (crc24 "biscuit") 10922056)))
  (testing "CRC-32 implementation"
    (is (= (crc32 "biscuit") 2285031842)))
  (testing "CRC-32c implementation"
    (is (= (crc32c "biscuit") 141543465))))
