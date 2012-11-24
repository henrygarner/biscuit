# Biscuit

A Clojure library for calculating message digest(-ive)s.

## Usage

```clojure
(require '[biscuit.core :as digest])
(digest/crc8 "Message")
```

The following algorithms are supported:

* CRC1
* CRC5
* CRC8
* CRC8 1-wire
* CRC16
* CRC16 CCITT
* CRC16 DNP
* CRC16 Modbus
* CRC16 USB
* CRC16 XModem
* CTC16 ZModem
* CRC24
* CRC32
* CRC32c
* CRC32 MPEG

## Credit

This library is strongly influenced by the [digest-crc](https://github.com/postmodern/digest-crc) ruby gem.

## License

Copyright © 2012 Henry Garner

Distributed under the Eclipse Public License, the same as Clojure.
