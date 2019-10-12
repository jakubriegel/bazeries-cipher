# Bazeries Cipher

## about
Implementation of Bazeries Cipher using asynchronous flows and coroutines. 
Application provides command and graphical interfaces.

Features:
* encryption of given text and files
* decryption of given text and files
* random key generation
* using digit or full names key
* _in progress_ concurrent encoding/decoding
* _in progress_ full Polish alphabet with special characters

## build
```
./gradlew shadowJar
```
Resulting jar is written to `build/libs/bazeries.jar`

## run
### cli
#### command
```
java -jar cli bazeries.jar ARGS
```
#### options for ARGS
* `WORD <KEY>` - encrypt word encrypt, key is optional (by default random key is used)
* `-f FILENAME <KEY>` - encrypt file, result is written to `FILENAME.enc`, key is optional (by default random key is used)
* `-d ARGS` - decrypt word or file; file result is written to `FILENAME.dec`

### gui
tba

## algorithm
tba

## credits
tba
