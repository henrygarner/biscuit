# Biscuit

A Clojure library for calculating message digest(ive)s. The digest algorithms implemented in biscuit are all variations of [CRCs](http://en.wikipedia.org/wiki/Cyclic_redundancy_check) and are designed to verify the integrity of messages sent over noisy channels.

## Installation

Add the following dependency to your `project.clj` file:

```clojure
[biscuit "1.0.0"]
```

## Usage

```clojure
(require '[biscuit.core :as digest])
(digest/crc8 "hobnob")
; 17
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
Copyright © 2019 Magnet S. Coop.

Distributed under the Eclipse Public License, the same as Clojure.
