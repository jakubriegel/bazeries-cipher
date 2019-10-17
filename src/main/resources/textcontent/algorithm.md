### how it works?
**alphabet:** `abcdefghiklmnopqrstuvwxyz`

The alphabet is filled into a 5x5 matrix column by column, a second matrix is filled line by line with a keyword (a number smaller than a million) followed by the remaining letters of the alphabet. 
Then the message is divide into blocks of arbitrary length and their characters' order is inverted. 
Finally, each letter is substituted by its counterpart in the second matrix.  

### additional features
* extended alphabet: 
```
aąbcćdeęfghijklłmnńoópqrsśtuvwxyzźż
AĄBCĆDEĘFHGIJKLŁMNŃOÓPQRSŚTUVWXYZŹŻ
!@#$%^&*()-=_+[]{}|\;:'",.<>/?`~
1234567890 space and new line
```
* keyword generated basing on key
* digit names keyword (i.e. for 123 -> onetwhr)
* full number name keyword (i.e. for 123 -> onehudreatwyf)
* non blocking and streaming based implementation

### examples
Using simple alphabet and key 900004 for word `university`:
* with digit names key

**keyword:** niefour

**matrix:**
```
n i e z r 
o f u a b 
c d g h k 
l m p q s 
t v w x y 
```

**result:** qmhatrmgxs

* with full number name key

**keyword:** niehudrtosaf

**matrix:**
```
n i e h u 
d r t o s 
a f b c g 
k l m p q 
v w x y z
```

**result:** plcovulbyq