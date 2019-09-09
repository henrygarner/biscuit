(ns biscuit.core
  (:require [biscuit.tables :as lookup]
            #?(:cljs [goog.crypt :as c])))

(defn- digest-byte
  "Returns an updated checksum given a previous checksum and a byte"
  [lookup-table lookup-shift xor-shift and-mask checksum byte]
  (-> checksum
      lookup-shift
      (bit-xor byte)
      (bit-and 0xff)
      lookup-table
      (bit-xor (-> checksum
                   xor-shift
                   (bit-and and-mask)))))

#?(:clj
   (defn seqable-bytes
     "Return seqable bytes"
     [message]
     (if (string? message)
       (.getBytes message "UTF-8")
       message)))

#?(:cljs
   (defn seqable-bytes
     "Return seqable bytes"
     [message]
     (cond
       (string? message) (c/stringToUtf8ByteArray message)
       (= (type message) js/Int8Array) (array-seq message)
       :else message)))

(defn- digest-message
  "Digests the message bytes into a checksum"
  [lookup-table lookup-shift xor-shift and-mask xor-mask checksum message]
  (let [bytes (seqable-bytes message)]
    (-> digest-byte
        (partial lookup-table lookup-shift xor-shift and-mask)
        (reduce checksum bytes)
        (bit-xor xor-mask))))

(defn crc1
  "Calculates the CRC1 checksum"
  [message]
  (let [bytes (seqable-bytes message)]
    (-> (reduce + bytes)
        (mod 256))))

(defn crc5
  "Calculates the CRC5 checksum"
  [message]
  (digest-message lookup/crc5
                  #(bit-shift-right % 3)
                  #(bit-shift-right % 8)
                  (bit-shift-left 0x1f 3)
                  0x1f
                  0x1f
                  message))

(defn crc8-1wire
  "Calculates the Dallas 1-wire CRC8 checksum"
  [message]
  (digest-message lookup/crc8-1wire
                  identity
                  #(bit-shift-left % 8)
                  0xff
                  0x00
                  0x00
                  message))

(defn crc8
  "Calculates the CRC8 checksum"
  [message]
  (digest-message lookup/crc8
                  identity
                  #(bit-shift-left % 8)
                  0xff
                  0x00
                  0x00
                  message))

(defn crc16
  "Calculates the CRC16 checksum"
  [message]
  (digest-message lookup/crc16
                  identity
                  #(bit-shift-right % 8)
                  0x00ffff
                  0x00
                  0x00
                  message))

(defn crc16-usb
  "Calculates the CRC16 USB checksum"
  [message]
  (digest-message lookup/crc16
                  identity
                  #(bit-shift-right % 8)
                  0x00ffff
                  0xffff
                  0xffff
                  message))

(defn crc16-ccitt
  "Calculates the CRC16 CCITT checksum"
  [message]
  (digest-message lookup/crc16-ccitt
                  #(bit-shift-right % 8)
                  #(bit-shift-left % 8)
                  0x00ffff
                  0x00
                  0xffff
                  message))

(defn crc16-dnp
  "Calculates the CRC16 DNP checksum"
  [message]
  (digest-message lookup/crc16-dnp
                  identity
                  #(bit-shift-right % 8)
                  0x00ffff
                  0x00
                  0x00
                  message))

(defn crc16-modbus
  "Calculates the CRC16 Modbus checksum"
  [message]
  (digest-message lookup/crc16-modbus
                  identity
                  #(bit-shift-right % 8)
                  0xffff
                  0x00
                  0xffff
                  message))

(defn crc16-xmodem
  "Calculates the CRC16 XModem checksum"
  [message]
  (digest-message lookup/crc16-xmodem
                  #(bit-shift-right % 8)
                  #(bit-shift-left % 8)
                  0xffff
                  0x00
                  0x00
                  message))

(defn crc16-zmodem
  "Calculates the CRC16 ZModem checksum"
  [message]
  (digest-message lookup/crc16-zmodem
                  #(bit-shift-right % 8)
                  #(bit-shift-left % 8)
                  0xffff
                  0x00
                  0x00
                  message))

(defn crc24
  "Calculates the CRC24 checksum"
  [message]
  (digest-message lookup/crc24
                  #(bit-shift-right % 16)
                  #(bit-shift-left % 8)
                  0xffffff
                  0x00
                  0xb704ce
                  message))

(defn crc32
  "Calculates the CRC32 checksum"
  [message]
  (digest-message lookup/crc32
                  identity
                  #(bit-shift-right % 8)
                  0x00ffffff
                  0xffffffff
                  0xffffffff
                  message))

(defn crc32c
  "Calculates the CRC32c checksum"
  [message]
  (digest-message lookup/crc32c
                  identity
                  #(bit-shift-right % 8)
                  0x00ffffff
                  0xffffffff
                  0xffffffff
                  message))

(defn crc32-mpeg
  "Calculates the CRC32MPEG checksum"
  [message]
  (digest-message lookup/crc32-mpeg
                  #(bit-shift-right % 24)
                  #(bit-shift-left % 8)
                  0xffffffff
                  0x00
                  0xffffffff
                  message))

(comment
  "Bitwise and does not support clojure.lang.BigInt"
  (defn crc64
    "Calculates the CRC64 checksum"
    [message]
    (digest-message lookup/crc64
                    identity
                    #(bit-shift-right % 8)
                    0xffffffffffffffff
                    0x00
                    0x00
                    message)))
