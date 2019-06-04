(ns biscuit.core-test
  (:require [clojure.test :refer [deftest is]]
            [biscuit.core :as biscuit]))

(def biscuit-str
  "biscuit")
(def biscuit-iso-8859-1
  (byte-array [98 105 115 99 117 105 116]))
(def biscuit-utf-8
  (byte-array [98 105 115 99 117 105 116]))
(def biscuit-utf-16le
  (byte-array [98 0 105 0 115 0 99 0 117 0 105 0 116 0]))
(def aeiou-str
  "áéíóú")
(def aeiou-iso-8859-1
  (byte-array [225, 233, 237, 243, 250]))
(def aeiou-utf-8
  (byte-array [195, 161, 195, 169, 195, 173, 195, 179, 195, 186]))
(def aeiou-utf-16le
  (byte-array [225, 0, 233, 0, 237, 0. 243, 0, 250, 0]))

(deftest crc1-checksum
  (is (= (biscuit/crc1 biscuit-str) 243))
  (is (= (biscuit/crc1 biscuit-iso-8859-1) 243))
  (is (= (biscuit/crc1 biscuit-utf-8) 243))
  (is (= (biscuit/crc1 biscuit-utf-16le) 243))
  (is (= (biscuit/crc1 aeiou-str "ISO-8859-1") 164))
  (is (= (biscuit/crc1 aeiou-str "UTF-8") 51))
  (is (= (biscuit/crc1 aeiou-str "UTF-16LE") 164))
  (is (= (biscuit/crc1 aeiou-iso-8859-1) 164))
  (is (= (biscuit/crc1 aeiou-utf-8) 51))
  (is (= (biscuit/crc1 aeiou-utf-16le) 164)))

(deftest crc5-checksum
  (is (= (biscuit/crc5 biscuit-str) 79))
  (is (= (biscuit/crc5 biscuit-iso-8859-1) 79))
  (is (= (biscuit/crc5 biscuit-utf-8) 79))
  (is (= (biscuit/crc5 biscuit-utf-16le) 119))
  (is (= (biscuit/crc5 aeiou-str "ISO-8859-1") 7))
  (is (= (biscuit/crc5 aeiou-str "UTF-8") 183))
  (is (= (biscuit/crc5 aeiou-str "UTF-16LE") 191))
  (is (= (biscuit/crc5 aeiou-iso-8859-1) 7))
  (is (= (biscuit/crc5 aeiou-utf-8) 183))
  (is (= (biscuit/crc5 aeiou-utf-16le) 191)))

(deftest crc8-1wire-checksum
  (is (= (biscuit/crc8-1wire biscuit-str) 151))
  (is (= (biscuit/crc8-1wire biscuit-iso-8859-1) 151))
  (is (= (biscuit/crc8-1wire biscuit-utf-8) 151))
  (is (= (biscuit/crc8-1wire biscuit-utf-16le) 236))
  (is (= (biscuit/crc8-1wire aeiou-str "ISO-8859-1") 220))
  (is (= (biscuit/crc8-1wire aeiou-str "UTF-8") 163))
  (is (= (biscuit/crc8-1wire aeiou-str "UTF-16LE") 211))
  (is (= (biscuit/crc8-1wire aeiou-iso-8859-1) 220))
  (is (= (biscuit/crc8-1wire aeiou-utf-8) 163))
  (is (= (biscuit/crc8-1wire aeiou-utf-16le) 211)))

(deftest crc8-checksum
  (is (= (biscuit/crc8 biscuit-str) 133))
  (is (= (biscuit/crc8 biscuit-iso-8859-1) 133))
  (is (= (biscuit/crc8 biscuit-utf-8) 133))
  (is (= (biscuit/crc8 biscuit-utf-16le) 74))
  (is (= (biscuit/crc8 aeiou-str "ISO-8859-1") 7))
  (is (= (biscuit/crc8 aeiou-str "UTF-8") 166))
  (is (= (biscuit/crc8 aeiou-str "UTF-16LE") 203))
  (is (= (biscuit/crc8 aeiou-iso-8859-1) 7))
  (is (= (biscuit/crc8 aeiou-utf-8) 166))
  (is (= (biscuit/crc8 aeiou-utf-16le) 203)))

(deftest crc16-checksum
  (is (= (biscuit/crc16 biscuit-str) 56686))
  (is (= (biscuit/crc16 biscuit-iso-8859-1) 56686))
  (is (= (biscuit/crc16 biscuit-utf-8) 56686))
  (is (= (biscuit/crc16 biscuit-utf-16le) 56954))
  (is (= (biscuit/crc16 aeiou-str "ISO-8859-1") 52444))
  (is (= (biscuit/crc16 aeiou-str "UTF-8") 57557))
  (is (= (biscuit/crc16 aeiou-str "UTF-16LE") 10493))
  (is (= (biscuit/crc16 aeiou-iso-8859-1) 52444))
  (is (= (biscuit/crc16 aeiou-utf-8) 57557))
  (is (= (biscuit/crc16 aeiou-utf-16le) 10493)))

(deftest crc16-usb-checksum
  (is (= (biscuit/crc16-usb biscuit-str) 8842))
  (is (= (biscuit/crc16-usb biscuit-iso-8859-1) 8842))
  (is (= (biscuit/crc16-usb biscuit-utf-8) 8842))
  (is (= (biscuit/crc16-usb biscuit-utf-16le) 8238))
  (is (= (biscuit/crc16-usb aeiou-str "ISO-8859-1") 13063))
  (is (= (biscuit/crc16-usb aeiou-str "UTF-8") 6234))
  (is (= (biscuit/crc16-usb aeiou-str "UTF-16LE") 53362))
  (is (= (biscuit/crc16-usb aeiou-iso-8859-1) 13063))
  (is (= (biscuit/crc16-usb aeiou-utf-8) 6234))
  (is (= (biscuit/crc16-usb aeiou-utf-16le) 53362)))

(deftest crc16-ccitt-checksum
  (is (= (biscuit/crc16-ccitt biscuit-str) 47310))
  (is (= (biscuit/crc16-ccitt biscuit-iso-8859-1) 47310))
  (is (= (biscuit/crc16-ccitt biscuit-utf-8) 47310))
  (is (= (biscuit/crc16-ccitt biscuit-utf-16le) 451))
  (is (= (biscuit/crc16-ccitt aeiou-str "ISO-8859-1") 7531))
  (is (= (biscuit/crc16-ccitt aeiou-str "UTF-8") 43485))
  (is (= (biscuit/crc16-ccitt aeiou-str "UTF-16LE") 13711))
  (is (= (biscuit/crc16-ccitt aeiou-iso-8859-1) 7531))
  (is (= (biscuit/crc16-ccitt aeiou-utf-8) 43485))
  (is (= (biscuit/crc16-ccitt aeiou-utf-16le) 13711)))

(deftest crc16-dnp-checksum
  (is (= (biscuit/crc16-dnp biscuit-str) 31858))
  (is (= (biscuit/crc16-dnp biscuit-iso-8859-1) 31858))
  (is (= (biscuit/crc16-dnp biscuit-utf-8) 31858))
  (is (= (biscuit/crc16-dnp biscuit-utf-16le) 61911))
  (is (= (biscuit/crc16-dnp aeiou-str "ISO-8859-1") 3761))
  (is (= (biscuit/crc16-dnp aeiou-str "UTF-8") 50616))
  (is (= (biscuit/crc16-dnp aeiou-str "UTF-16LE") 23676))
  (is (= (biscuit/crc16-dnp aeiou-iso-8859-1) 3761))
  (is (= (biscuit/crc16-dnp aeiou-utf-8) 50616))
  (is (= (biscuit/crc16-dnp aeiou-utf-16le) 23676)))

(deftest crc16-modbus-checksum
  (is (= (biscuit/crc16-modbus biscuit-str) 56693))
  (is (= (biscuit/crc16-modbus biscuit-iso-8859-1) 56693))
  (is (= (biscuit/crc16-modbus biscuit-utf-8) 56693))
  (is (= (biscuit/crc16-modbus biscuit-utf-16le) 57297))
  (is (= (biscuit/crc16-modbus aeiou-str "ISO-8859-1") 52472))
  (is (= (biscuit/crc16-modbus aeiou-str "UTF-8") 59301))
  (is (= (biscuit/crc16-modbus aeiou-str "UTF-16LE") 12173))
  (is (= (biscuit/crc16-modbus aeiou-iso-8859-1) 52472))
  (is (= (biscuit/crc16-modbus aeiou-utf-8) 59301))
  (is (= (biscuit/crc16-modbus aeiou-utf-16le) 12173)))

(deftest crc16-xmodem--checksum
  (is (= (biscuit/crc16-xmodem biscuit-str) 18688))
  (is (= (biscuit/crc16-xmodem biscuit-iso-8859-1) 18688))
  (is (= (biscuit/crc16-xmodem biscuit-utf-8) 18688))
  (is (= (biscuit/crc16-xmodem biscuit-utf-16le) 43177))
  (is (= (biscuit/crc16-xmodem aeiou-str "ISO-8859-1") 3175))
  (is (= (biscuit/crc16-xmodem aeiou-str "UTF-8") 18660))
  (is (= (biscuit/crc16-xmodem aeiou-str "UTF-16LE") 54454))
  (is (= (biscuit/crc16-xmodem aeiou-iso-8859-1) 3175))
  (is (= (biscuit/crc16-xmodem aeiou-utf-8) 18660))
  (is (= (biscuit/crc16-xmodem aeiou-utf-16le) 54454)))

(deftest crc16-zmodem-checksum
  (is (= (biscuit/crc16-zmodem biscuit-str) 18688))
  (is (= (biscuit/crc16-zmodem biscuit-iso-8859-1) 18688))
  (is (= (biscuit/crc16-zmodem biscuit-utf-8) 18688))
  (is (= (biscuit/crc16-zmodem biscuit-utf-16le) 43177))
  (is (= (biscuit/crc16-zmodem aeiou-str "ISO-8859-1") 3175))
  (is (= (biscuit/crc16-zmodem aeiou-str "UTF-8") 18660))
  (is (= (biscuit/crc16-zmodem aeiou-str "UTF-16LE") 54454))
  (is (= (biscuit/crc16-zmodem aeiou-iso-8859-1) 3175))
  (is (= (biscuit/crc16-zmodem aeiou-utf-8) 18660))
  (is (= (biscuit/crc16-zmodem aeiou-utf-16le) 54454)))

(deftest crc24-checksum
  (is (= (biscuit/crc24 biscuit-str) 10922056))
  (is (= (biscuit/crc24 biscuit-iso-8859-1) 10922056))
  (is (= (biscuit/crc24 biscuit-utf-8) 10922056))
  (is (= (biscuit/crc24 biscuit-utf-16le) 6060748))
  (is (= (biscuit/crc24 aeiou-str "ISO-8859-1") 1411850))
  (is (= (biscuit/crc24 aeiou-str "UTF-8") 6752129))
  (is (= (biscuit/crc24 aeiou-str "UTF-16LE") 15428536))
  (is (= (biscuit/crc24 aeiou-iso-8859-1) 1411850))
  (is (= (biscuit/crc24 aeiou-utf-8) 6752129))
  (is (= (biscuit/crc24 aeiou-utf-16le) 15428536)))

(deftest crc32-checksum
  (is (= (biscuit/crc32 biscuit-str) 2285031842))
  (is (= (biscuit/crc32 biscuit-iso-8859-1) 2285031842))
  (is (= (biscuit/crc32 biscuit-utf-8) 2285031842))
  (is (= (biscuit/crc32 biscuit-utf-16le) 477848848))
  (is (= (biscuit/crc32 aeiou-str "ISO-8859-1") 3139632463))
  (is (= (biscuit/crc32 aeiou-str "UTF-8") 3532617105))
  (is (= (biscuit/crc32 aeiou-str "UTF-16LE") 2003798097))
  (is (= (biscuit/crc32 aeiou-iso-8859-1) 3139632463))
  (is (= (biscuit/crc32 aeiou-utf-8) 3532617105))
  (is (= (biscuit/crc32 aeiou-utf-16le) 2003798097)))

(deftest crc32c-checksum
  (is (= (biscuit/crc32c biscuit-str) 141543465))
  (is (= (biscuit/crc32c biscuit-iso-8859-1) 141543465))
  (is (= (biscuit/crc32c biscuit-utf-8) 141543465))
  (is (= (biscuit/crc32c biscuit-utf-16le) 1419465629))
  (is (= (biscuit/crc32c aeiou-str "ISO-8859-1") 2082708916))
  (is (= (biscuit/crc32c aeiou-str "UTF-8") 2470632328))
  (is (= (biscuit/crc32c aeiou-str "UTF-16LE") 2827949885))
  (is (= (biscuit/crc32c aeiou-iso-8859-1) 2082708916))
  (is (= (biscuit/crc32c aeiou-utf-8) 2470632328))
  (is (= (biscuit/crc32c aeiou-utf-16le) 2827949885)))

(deftest crc32-mpeg-checksum
  (is (= (biscuit/crc32-mpeg biscuit-str) 2739206853))
  (is (= (biscuit/crc32-mpeg biscuit-iso-8859-1) 2739206853))
  (is (= (biscuit/crc32-mpeg biscuit-utf-8) 2739206853))
  (is (= (biscuit/crc32-mpeg biscuit-utf-16le) 3612678782))
  (is (= (biscuit/crc32-mpeg aeiou-str "ISO-8859-1") 810276022))
  (is (= (biscuit/crc32-mpeg aeiou-str "UTF-8") 2753494197))
  (is (= (biscuit/crc32-mpeg aeiou-str "UTF-16LE") 4163228820))
  (is (= (biscuit/crc32-mpeg aeiou-iso-8859-1) 810276022))
  (is (= (biscuit/crc32-mpeg aeiou-utf-8) 2753494197))
  (is (= (biscuit/crc32-mpeg aeiou-utf-16le) 4163228820)))

(deftest crc64-checksum
  (is (= (biscuit/crc64 biscuit-str) 5158440339845310816))
  (is (= (biscuit/crc64 biscuit-iso-8859-1) 5158440339845310816))
  (is (= (biscuit/crc64 biscuit-utf-8) 5158440339845310816))
  (is (= (biscuit/crc64 biscuit-utf-16le) 12621829595197151832))
  (is (= (biscuit/crc64 aeiou-str "ISO-8859-1") 10915415423066308608))
  (is (= (biscuit/crc64 aeiou-str "UTF-8") 16123775510345298566))
  (is (= (biscuit/crc64 aeiou-str "UTF-16LE") 3264482577922715790))
  (is (= (biscuit/crc64 aeiou-iso-8859-1) 10915415423066308608))
  (is (= (biscuit/crc64 aeiou-utf-8) 16123775510345298566))
  (is (= (biscuit/crc64 aeiou-utf-16le) 3264482577922715790)))
