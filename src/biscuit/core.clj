(ns biscuit.core
  (:require [biscuit.lookup-tables :as lookups]))

(defn- digest-byte
  "Returns a new checksum given a byte lookup, a bit shift operation, a mask, the previous checksum and the byte to digest"
  [lookup-table lookup-shift xor-shift mask checksum byte]
  (-> checksum
      lookup-shift
      (bit-xor byte)
      (bit-and 0xff)
      lookup-table
      (bit-xor (-> checksum
                   xor-shift
                   (bit-and mask)))))

(defn- digest-message
  "Returns a message digest given a byte lookup, a bit shift operation, a bitwise and mask, a bitwise or mask, the initial checksum and a message to digest"
  [lookup-table lookup-shift xor-shift and-mask or-mask checksum message]
  (let [bytes (.getBytes message)]
    (bit-xor
     (reduce
      (partial digest-byte lookup-table lookup-shift xor-shift and-mask)
      checksum bytes)
     or-mask)))

(defn crc1
  "Calculates the CRC1 checksum"
  [message]
  (-> (reduce + (.getBytes message))
      (mod 256)))

(defn crc8-1wire
  "Calculates the Dallas 1-wire CRC8 checksum"
  [message]
  (digest-message lookups/crc8-1wire
                  identity
                  #(bit-shift-left % 8)
                  0xff
                  0x00
                  0x00
                  message))

(defn crc8
  "Calculates the CRC8 checksum"
  [message]
  (digest-message lookups/crc8
                  identity
                  #(bit-shift-left % 8)
                  0xff
                  0x00
                  0x00
                  message))

(defn crc16
  "Calculates the CRC16 checksum"
  [message]
  (digest-message lookups/crc16
                  identity
                  #(bit-shift-right % 8)
                  0x00ffff
                  0x0
                  0x00
                  message))

(defn crc24
  "Calculates the CRC24 checksum"
  [message]
  (digest-message lookups/crc24
                  #(bit-shift-right % 16)
                  #(bit-shift-left % 8)
                  0xffffff
                  0x00
                  0xb704ce
                  message))

(defn crc32
  "Calculates the CRC32 checksum"
  [message]
  (digest-message lookups/crc32
                  identity
                  #(bit-shift-right % 8)
                  0x00ffffff
                  0xffffffff
                  0xffffffff
                  message))