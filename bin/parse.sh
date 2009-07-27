#!/usr/bin/bash

head -n 400 arabic2.txt > in.txt
perl '-F/\t/' -nae 'for($i=0; $i<5; $i++){print join("",map("&#x".sprintf("%x",0x560 + ord $_),split(//,$F[$i]))), "\t";} for(;$i<@F; $i++){ print($F[$i],"\t") }; print "\n";' < in.txt | csv2om
