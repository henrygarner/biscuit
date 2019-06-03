(ns biscuit.core
  (:require [biscuit.tables :as lookup]))

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

(defn- digest-byte-biginteger
  "Returns an updated checksum given a previous checksum and a byte.
  Uses BigInteger arithmetic for checksum calculations"
  [lookup-table lookup-shift xor-shift and-mask checksum byte]
  (-> checksum
      lookup-shift
      (.xor (.toBigInteger (bigint byte)))
      (.and (.toBigInteger 0xffN))
      lookup-table
      (-> bigint .toBigInteger)
      (.xor (-> checksum
                xor-shift
                (.and and-mask)))))

(defn- digest-message
  "Digests the message bytes into a checksum"
  [lookup-table lookup-shift xor-shift and-mask xor-mask checksum message]
  (-> digest-byte
      (partial lookup-table lookup-shift xor-shift and-mask)
      (reduce checksum message)
      (bit-xor xor-mask)))

(defn- digest-message-biginteger
  "Digests the message bytes into a checksum.
  Uses BigInteger arithmetic for checksum calculations"
  [lookup-table lookup-shift xor-shift and-mask xor-mask checksum message]
  (-> digest-byte-biginteger
      (partial lookup-table lookup-shift xor-shift and-mask)
      (reduce checksum message)
      (.xor xor-mask)))

(defprotocol ICrc
  (crc1 [message] [message charset]
    "Calculates the CRC1 checksum")
  (crc5 [message] [message charset]
    "Calculates the CRC5 checksum")
  (crc8-1wire [message] [message charset]
    "Calculates the Dallas 1-wire CRC8 checksum")
  (crc8 [message] [message charset]
    "Calculates the CRC8 checksum")
  (crc16 [message] [message charset]
    "Calculates the CRC16 checksum")
  (crc16-usb [message] [message charset]
    "Calculates the CRC16 USB checksum")
  (crc16-ccitt [message] [message charset]
    "Calculates the CRC16 CCITT checksum")
  (crc16-dnp [message] [message charset]
    "Calculates the CRC16 DNP checksum")
  (crc16-modbus [message] [message charset]
    "Calculates the CRC16 Modbus checksum")
  (crc16-xmodem [message] [message charset]
    "Calculates the CRC16 XModem checksum")
  (crc16-zmodem [message] [message charset]
    "Calculates the CRC16 ZModem checksum")
  (crc24 [message] [message charset]
    "Calculates the CRC24 checksum")
  (crc32 [message] [message charset]
    "Calculates the CRC32 checksum")
  (crc32c [message] [message charset]
    "Calculates the CRC32c checksum")
  (crc32-mpeg [message] [message charset]
    "Calculates the CRC32MPEG checksum")
  (crc64 [message] [message charset]
    "Calculates the CRC64 checksum"))

(extend-protocol ICrc
  (Class/forName "[B")
  (crc1 [message]
    (-> (reduce + message)
        (mod 256)))
  (crc5 [message]
    (digest-message lookup/crc5
                    #(bit-shift-right % 3)
                    #(bit-shift-right % 8)
                    (bit-shift-left 0x1f 3)
                    0x1f
                    0x1f
                    message))
  (crc8-1wire [message]
    (digest-message lookup/crc8-1wire
                    identity
                    #(bit-shift-left % 8)
                    0xff
                    0x00
                    0x00
                    message))
  (crc8 [message]
    (digest-message lookup/crc8
                    identity
                    #(bit-shift-left % 8)
                    0xff
                    0x00
                    0x00
                    message))
  (crc16 [message]
    (digest-message lookup/crc16
                    identity
                    #(bit-shift-right % 8)
                    0x00ffff
                    0x00
                    0x00
                    message))
  (crc16-usb [message]
    (digest-message lookup/crc16
                    identity
                    #(bit-shift-right % 8)
                    0x00ffff
                    0xffff
                    0xffff
                    message))
  (crc16-ccitt [message]
    (digest-message lookup/crc16-ccitt
                    #(bit-shift-right % 8)
                    #(bit-shift-left % 8)
                    0x00ffff
                    0x00
                    0xffff
                    message))
  (crc16-dnp [message]
    (digest-message lookup/crc16-dnp
                    identity
                    #(bit-shift-right % 8)
                    0x00ffff
                    0x00
                    0x00
                    message))
  (crc16-modbus [message]
    (digest-message lookup/crc16-modbus
                    identity
                    #(bit-shift-right % 8)
                    0xffff
                    0x00
                    0xffff
                    message))
  (crc16-xmodem [message]
    (digest-message lookup/crc16-xmodem
                    #(bit-shift-right % 8)
                    #(bit-shift-left % 8)
                    0xffff
                    0x00
                    0x00
                    message))
  (crc16-zmodem [message]
    (digest-message lookup/crc16-zmodem
                    #(bit-shift-right % 8)
                    #(bit-shift-left % 8)
                    0xffff
                    0x00
                    0x00
                    message))
  (crc24 [message]
    (digest-message lookup/crc24
                    #(bit-shift-right % 16)
                    #(bit-shift-left % 8)
                    0xffffff
                    0x00
                    0xb704ce
                    message))
  (crc32 [message]
    (digest-message lookup/crc32
                    identity
                    #(bit-shift-right % 8)
                    0x00ffffff
                    0xffffffff
                    0xffffffff
                    message))
  (crc32c [message]
    (digest-message lookup/crc32c
                    identity
                    #(bit-shift-right % 8)
                    0x00ffffff
                    0xffffffff
                    0xffffffff
                    message))
  (crc32-mpeg [message]
    (digest-message lookup/crc32-mpeg
                    #(bit-shift-right % 24)
                    #(bit-shift-left % 8)
                    0xffffffff
                    0x00
                    0xffffffff
                    message))
  (crc64 [message]
    (digest-message-biginteger lookup/crc64
                               identity
                               #(.shiftRight % 8)
                               (.toBigInteger 0xffffffffffffffffN)
                               (.toBigInteger 0x00N)
                               (.toBigInteger 0x00N)
                               message))
  String
  (crc1
    ([message]
     (crc1 message "UTF-8"))
    ([message charset]
     (crc1 (.getBytes message charset))))
  (crc5
    ([message]
     (crc5 message "UTF-8"))
    ([message charset]
     (crc5 (.getBytes message charset))))
  (crc8-1wire
    ([message]
     (crc8-1wire message "UTF-8"))
    ([message charset]
     (crc8-1wire (.getBytes message charset))))
  (crc8
    ([message]
     (crc8 message "UTF-8"))
    ([message charset]
     (crc8 (.getBytes message charset))))
  (crc16
    ([message]
     (crc16 message "UTF-8"))
    ([message charset]
     (crc16 (.getBytes message charset))))
  (crc16-usb
    ([message]
     (crc16-usb message "UTF-8"))
    ([message charset]
     (crc16-usb (.getBytes message charset))))
  (crc16-ccitt
    ([message]
     (crc16-ccitt message "UTF-8"))
    ([message charset]
     (crc16-ccitt (.getBytes message charset))))
  (crc16-dnp
    ([message]
     (crc16-dnp message "UTF-8"))
    ([message charset]
     (crc16-dnp (.getBytes message charset))))
  (crc16-modbus
    ([message]
     (crc16-modbus message "UTF-8"))
    ([message charset]
     (crc16-modbus (.getBytes message charset))))
  (crc16-xmodem
    ([message]
     (crc16-xmodem message "UTF-8"))
    ([message charset]
     (crc16-xmodem (.getBytes message charset))))
  (crc16-zmodem
    ([message]
     (crc16-zmodem message "UTF-8"))
    ([message charset]
     (crc16-zmodem (.getBytes message charset))))
  (crc24
    ([message]
     (crc24 message "UTF-8"))
    ([message charset]
     (crc24 (.getBytes message charset))))
  (crc32
    ([message]
     (crc32 message "UTF-8"))
    ([message charset]
     (crc32 (.getBytes message charset))))
  (crc32c
    ([message]
     (crc32c message "UTF-8"))
    ([message charset]
     (crc32c (.getBytes message charset))))
  (crc32-mpeg
    ([message]
     (crc32-mpeg message "UTF-8"))
    ([message charset]
     (crc32-mpeg (.getBytes message charset))))
  (crc64
    ([message]
     (crc64 message "UTF-8"))
    ([message charset]
     (crc64 (.getBytes message charset)))))
