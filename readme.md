# Bazeries Cipher

## about
Implementation of Bazeries Cipher using asynchronous flows and coroutines. 
Application provides command line and graphical interfaces.

Features:
* encryption of given text and files
* decryption of given text and files
* random key generation
* using digit or full name key
* basic English alphabet or full Polish alphabet with digits and special characters

## build
```
./gradlew shadowJar
```
Resulting jar is written to `build/libs/bazeries.jar`

## run
### gui
Features:
* encrypt/decrypt text
* encrypt/decrypt file
* use given or random key
* use digit or full name key
* use basic or extended alphabet

### cli
> CLI was developed only for development purposes and has very limites features

#### command
```
java -jar cli bazeries.jar ARGS
```
#### options for ARGS
* `WORD <KEY>` - encrypt word encrypt
* `-f FILENAME <KEY>` - encrypt file, result is written to `FILENAME.enc`
* `-d ARGS` - decrypt word or file; file result is written to `FILENAME.dec`

## algorithm
tba

## credits
This is a project for Data Protection Basics course at Poznan University of Technology.
