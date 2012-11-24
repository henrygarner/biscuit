(ns biscuit.core-test
  (:use clojure.test
        biscuit.core))

(deftest checksum
  (is (= (crc1 "biscuit") 243))
  (is (= (crc5 "biscuit") 79))
  (is (= (crc8-1wire "biscuit") 151))
  (is (= (crc8 "biscuit") 133))
  (is (= (crc16 "biscuit") 56686))
  (is (= (crc16-usb "biscuit") 8842))
  (is (= (crc16-ccitt "biscuit") 47310))
  (is (= (crc16-dnp "biscuit") 31858))
  (is (= (crc16-modbus "biscuit") 56693))
  (is (= (crc16-xmodem "biscuit") 18688))
  (is (= (crc16-zmodem "biscuit") 18688))
  (is (= (crc24 "biscuit") 10922056))
  (is (= (crc32 "biscuit") 2285031842))
  (is (= (crc32c "biscuit") 141543465))
  (is (= (crc32-mpeg "biscuit") 2739206853))
  (comment
    "Bitwise and does not support clojure.lang.BigInt"
    (is (= (crc64 "biscuit") 5158440339845310816))))
