<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html><head><title>Nabu ${vocName}</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script language="JavaScript">
var vocabulary = [
					${vocString}
				];			
var modes = [
	${modesString}
];
var size = vocabulary.length;
var numofchoices = 5;
var permutation=[];
var repetitions=[];
var repetitioninit = 3;
var repetitionincrement = 1;
var status = 0; // 0=start; 1=init; 2=question; 3=correction; 5=end;
var stat_attempts = 0;
var stat_firstsuccesses = 0; 
var stat_repetitions = 0;

var mode = 0;

function el(id){
	return document.getElementById(id);
}

function getQuestion(i){
	var arr = modes[mode][1];
	var str = vocabulary[i][arr[0]];
	for(var j=1; j<arr.length; j++){
		str += ', ' + vocabulary[i][arr[j]];
	}
	return str;
}

function getAnswer(i){
	var arr = modes[mode][2];
	var str = vocabulary[i][arr[0]];
	for(var j=1; j<arr.length; j++){
		str += vocabulary[i][arr[j]];
	}
	return str;
}

function arrayshuffle(arr){
	var n = arr.length;
	for(var i=0; i<n; i++) {
        var k = random(n);  
        var temp = arr[i];
        arr[i] = arr[k];
        arr[k] = temp;
    }
   	return;
}
function random(n){
	var p = Math.random();
	var r = p*n;
	return Math.round(r-0.5);
}

var vocSize=-1;
function startSession(){
	var startVocSize = vocabulary.length;
	for(var i=0; i<startVocSize; i++){
		permutation[i]=i;
		repetitions[i]=0;
	}
	arrayshuffle(permutation);
	//numofchoices=Math.round(document.radiointerface.elements.length/3.0);
	status=1;	// init -->
	document.summary.statistics.value="";
	presentQuestion();
	return;
}

var vocNum=-1;
var rightChoice=-1;
function presentQuestion(){
	var actualSize = permutation.length;
	if(actualSize<1){
		status=5;  //   end   -->
		getSummary();
		document.interface.actualstatus.value=status;
		return;    //   showstatistics()   -->
	}
	status=2;	// question  -->
	vocSize = vocabulary.length;
	vocNum = permutation.pop();
	var vocItem = vocabulary[vocNum];
	setContent("questiontext",getQuestion(vocNum));
	var choices =[];
	for(var i=0; i<numofchoices; i++){
		var rpos = random(vocSize);
		while(rpos==vocNum){
			rpos = random(vocSize);
		}
		choices[i]=rpos;
	}
	rightChoice = random(numofchoices);
	choices[rightChoice]=vocNum;
	for(var i=0; i<numofchoices; i++){
		var pos = choices[i];
		var actVocItem = getAnswer(pos);
		setAnswerText(i,actVocItem);
		el("answer"+i).style.background="#DDDDBB";
	}
	document.questioninterface.answertext.value="";
	document.questioninterface.atext.value="";
	return;
}

function answer(answernum){
	
	var actualSize = permutation.length;
	if(status==5){	// end  -->
		return;
	}else if(status==3){ // correction  -->
		presentQuestion();
		return;
	}
	status=3; 
	stat_attempts++;
	if(answernum >= 0 && (answernum == rightChoice || getAnswer(answernum)==getAnswer(rightChoice)) ){
		if(repetitions[vocNum]>0){
			repetitions[vocNum]--;
			stat_repetitions++;
		}else{
			stat_firstsuccesses++; 
		}
		status = 3;
		presentQuestion();
		return;
	}else{
		if(repetitions[vocNum]>0){
			repetitions[vocNum]+=repetitionincrement;
			for(var i=0; i<=repetitionincrement; i++){
				var rpos = random(actualSize);
				permutation.splice(rpos,0,vocNum);
			}
		}else{
			repetitions[vocNum]=repetitioninit;
			for(var i=0; i<=repetitioninit; i++){
				var rpos = random(actualSize);
				permutation.splice(rpos,0,vocNum);
			}
		}
		
	}
	actualSize = permutation.length;
	for(var i=0; i<numofchoices; i++){
		if(i==rightChoice){
			el("answer"+i).style.background="green";
		}else if(i==answernum){
			el("answer"+i).style.background="red";
		}else{
			el("answer"+i).style.background="#DDDDBB";
		}
	}
	return;
}
function textanswer(){
	var actualSize = permutation.length;
	if(status==5){	// end  -->
		return  false;
	}else if(status==3){ // correction  -->
		presentQuestion();
		document.questioninterface.answertext.focus();
		return  false;
	}
	status=3; 
	document.interface.actualstatus.value=status;
	stat_attempts++;
	var vocItem = vocabulary[vocNum];
	var answerstring=document.questioninterface.answertext.value; 
	if(answerstring==vocItem[1]){
		questioninterface.atext.value="richtig";
		questioninterface.atext.className="remark2";
		if(repetitions[vocNum]>0){
			repetitions[vocNum]--;
			stat_repetitions++;
		}else{
			stat_firstsuccesses++; 
		}
		
	}else{
		questioninterface.atext.value=vocItem[1]+", falsch war: ";
		questioninterface.atext.className="remark";
		if(repetitions[vocNum]>0){
			repetitions[vocNum]+=repetitionincrement;
			for(var i=0; i<=repetitionincrement; i++){
				var rpos = random(actualSize);
				permutation.splice(rpos,0,vocNum);
			}
		}else{
			repetitions[vocNum]=repetitioninit;
			for(var i=0; i<=repetitioninit; i++){
				var rpos = random(actualSize);
				permutation.splice(rpos,0,vocNum);
			}
		}
		
	}
	actualSize = permutation.length;
	var str="";
	for(var i=0; i<actualSize; i++){
		str+= vocabulary[permutation[i]];
		str+= "\n";
	}
	document.questioninterface.answertext.focus();
	return false;
}

function setContent(name, text){
	el(name).firstChild.nodeValue = text;
}
function getContent(name){
	return el(name).firstChild.nodeValue;
}
function setAnswerText(i, text){
	if(text.length == 0){
		text = '\u00A0';
	}
	el("answer"+i).firstChild.nodeValue = text;
}
function getAnswerText(i){
	return el("answer"+i).firstChild.nodeValue;
}
function getSummary(){
	document.summary.statistics.value="ENDE (Gewusst="+stat_firstsuccesses+", Repetitionen="+stat_repetitions+". Versuche="+stat_attempts+")";
}
var soundSrc;
function getSoundSource(){
	return "testsound.mp3";
}
function noSubmit(){
	return false;
}
</script>
<style type="text/css" media="screen">
body.general{background: #DDDDBB; }
hr.general{width:90%; /*noshade: noshade;*/ color:#AAAA88; }
.answerItem{border-width: 1px; margin:0%;color: black; background: #DDDDBB;}
.remark{border-width: 0px; margin:0%;color: red; display: none; }
.remark2{border-width: 0px; margin:0%;color: green}
.debug{ display: none; }
#questiontext{margin: 20; border: 2px solid #0000FF; padding: 10px; background: #CCCCCC; }

#theAnswers{border-width: 5px; margin:0%;color: black}
</style>
</head>
<body class="general">
<table>
<tr><td>
<!--<img src="images/nabulogo8xs.png">-->
<img src='data:image/png;base64,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'/>
</td><td><h1>Nabu ${vocName}</h1></td></tr>
</table>
<table><tr><td>

<form name="interface">
<table><tr><td>

<!-- Mode -->
<select name="modeSelect" value="0" onClick="mode = document.interface.modeSelect.value;">
<script language="JavaScript">
for(var i=0; i<modes.length; i++){
document.write('<option value="'+i+'">'+modes[i][0]+'</option>');
}
document.interface.modeSelect.value = mode;
</script>
</select>

<!-- Startsession -->
<input type=button name="start" value="Start session" onClick="startSession()"/>
</td>
</tr>

</form> <!-- end interface form -->
</table>
</td></tr></table>

<!-- Question -->
<div class="question" id="questiontext" onClick="answer(-1)">&nbsp;</div>
<!-- Text answer -->
<!--<form name="questioninterface" onSubmit="return textanswer()">
<tr><td><input id="remark" name="atext" type=text value=""/></td>
<td colspan="2">
<input type="text" name="answertext" id="answerfield" /></td></tr>
</table></form>-->

<!-- the Answers -->
<div id="theAnswers">
<script language="JavaScript"><!--
for(var i=0; i<numofchoices; i++){
document.write('<div id="answer'+i+'" class="answerItem" onClick="answer('+i+'); false">&nbsp;</div>');
}
--></script>
<div>

<form name="summary">
	<input type=text value="" name="statistics" size="50" id="summary"/>
</form>

<a href="nabuJsDataUrl.jsp?id=${param.id}">Zum Bookmarken hier klicken und dann speichern.</a>
</body>
</html>
