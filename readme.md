# Bazeries Cipher

## about
Implementation of Bazeries Cipher using asynchronous flows and coroutines. 
Application provides command and graphical interfaces.

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
