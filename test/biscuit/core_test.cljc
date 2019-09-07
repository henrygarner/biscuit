(ns biscuit.core-test
  (:require [clojure.test :refer [deftest is]]
            [biscuit.core :as b]))

(deftest checksum-str
  (is (= (b/crc1 "biscuit") 243))
  (is (= (b/crc5 "biscuit") 79))
  (is (= (b/crc8-1wire "biscuit") 151))
  (is (= (b/crc8 "biscuit") 133))
  (is (= (b/crc16 "biscuit") 56686))
  (is (= (b/crc16-usb "biscuit") 8842))
  (is (= (b/crc16-ccitt "biscuit") 47310))
  (is (= (b/crc16-dnp "biscuit") 31858))
  (is (= (b/crc16-modbus "biscuit") 56693))
  (is (= (b/crc16-xmodem "biscuit") 18688))
  (is (= (b/crc16-zmodem "biscuit") 18688))
  (is (= (b/crc24 "biscuit") 10922056))
  (is (= (b/crc32 "biscuit") 2285031842))
  (is (= (b/crc32c "biscuit") 141543465))
  (is (= (b/crc32-mpeg "biscuit") 2739206853))
  (comment
    "Bitwise and does not support clojure.lang.BigInt"
    (is (= (b/crc64 "biscuit") 5158440339845310816))))

(deftest checksum-array-from-str
  (is (= (b/crc8 (b/seqable-bytes "biscuit")) 133))
  (is (= (b/crc16 (b/seqable-bytes "biscuit")) 56686))
  (is (= (b/crc32 (b/seqable-bytes "biscuit")) 2285031842)))

#?(:cljs
(deftest checksum-js-typed-array
  (is (= (b/crc8 (js/Int8Array. (clj->js [0 1 2]))) 27))
  (is (= (b/crc16 (js/Int8Array. (clj->js [0 1 2]))) 20864))
  (is (= (b/crc32 (js/Int8Array. (clj->js [0 1 2]))) 139757951))))