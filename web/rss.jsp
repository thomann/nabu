<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="application/rss+xml; charset=UTF-8"
	pageEncoding="ISO-8859-1" import="java.util.*,ch.unizh.ori.nabu.voc.*"%>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="input" uri="http://jakarta.apache.org/taglibs/input-1.0" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<rss xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd"
	version="2.0">
<c:choose>
<c:when test="${param.myVoc}"><c:set value="${myVoc}" var="voc"/></c:when>
<c:otherwise><c:set value="${central.vocs[param.id]}" var="voc"/></c:otherwise>
</c:choose>
<%

	long issued = Long.parseLong(request.getParameter("issued"));
	long duration = Math.max(System.currentTimeMillis() - issued, 0);
	int minute = 1000 * 60;
	int day = 1000 * 60 * 60 * 24;
	int week = 7 * day;
	int increment = minute;
	if("daily".equals(request.getParameter("period"))){
		increment = day;
	}else if("weekly".equals(request.getParameter("period"))){
		increment = week;
	}
	
	int count = (int)( duration / increment ) + 1;
	count = Math.min(count, 100);
	
	pageContext.setAttribute("increment", new Integer(increment));
	pageContext.setAttribute("count", new Integer(count));
	
	Vocabulary voc = (Vocabulary)pageContext.findAttribute("voc");
	
	boolean wOne = "one".equals(request.getParameter("which"));
	boolean wAll = "all".equals(request.getParameter("which"));
	
	List lessons = new ArrayList(count);
	List dates = new ArrayList(count);
	StringBuffer lessonUrl = new StringBuffer();
	List urls = new ArrayList(count);
	
	boolean all = (request.getParameter("l-all") != null);
	int lessonsCount=0;
	if(voc.getLections() != null){
		 int i=0;
		for (Iterator iterator = voc.getLections().iterator();
			iterator.hasNext() && i<count;
			) {
			FieldStream fs = (FieldStream) iterator.next();
			
			if(all || request.getParameter("l."+fs.getId()) != null){
				lessons.add(fs);
				lessonUrl.append("&l.").append(fs.getId()).append("=on");
				urls.add(lessonUrl.toString());
				i++;
			}
		}
		lessonsCount = i;
		for(; i<count; i++){
			lessons.add("");
			urls.add("");
		}
	}
	
	int version = 1;
	for(int i=0; i<count; i++){
		Date d = new Date(issued + i * increment);
		dates.add(new java.text.SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US).format(d));
		if(wAll || i>=lessonsCount){
			urls.set(i, lessonUrl);
			lessons.set(i, "All, version "+version++);
		}else{
			FieldStream fs = (FieldStream)lessons.get(i);
			if(wOne){
				urls.set(i, "&l."+fs.getId()+"=on");
				lessons.set(i, fs.getName());
			}else{
				String a = fs.getName();
				if(i>0)
					a = lessons.get(0)+" - "+a;
				lessons.set(i, a);
			}
		}
	}
	
	pageContext.setAttribute("lessons", lessons);
	pageContext.setAttribute("dates", dates);
	pageContext.setAttribute("urls", urls);
	pageContext.setAttribute("lessonUrl", lessonUrl);
	pageContext.setAttribute("nextDate", new Date(issued + increment*lessons.size()));


%>
<c:set var="prefix">${pageContext.request.scheme}://${pageContext.request.serverName}<%= 
	( (request.getScheme().equals("http") && (request.getServerPort() != 80))
			|| (request.getScheme().equals("https") && (request.getServerPort() != 443))
			) ? ":"+request.getServerPort() : ""
%>${pageContext.request.contextPath}${sub}</c:set>
<channel>

<title>Nabucast ${fn:escapeXml(voc.name)}</title>

<link>${prefix}/podcast.jsp?id=${voc.id}</link>

<language>en-us</language>

<copyright>&#x2117; &amp; &#xA9; 2008 Philipp Thomann &amp; Family</copyright>

<itunes:subtitle>A Podcast for vocabulary ${fn:escapeXml(voc.name)}</itunes:subtitle>

<itunes:author>Nabu</itunes:author>

<itunes:summary>A Podcast to learn the vocabulary in ${fn:escapeXml(voc.name)}: ${fn:escapeXml(voc.name)}</itunes:summary>
<description>A Podcast to learn the vocabulary in ${fn:escapeXml(voc.name)}:
${fn:escapeXml(voc.name)}</description>

<!--<itunes:owner>-->

<!--	<itunes:name>Philipp Thomann</itunes:name>-->
<!--	<itunes:email>john.doe@example.com</itunes:email>-->
<!--</itunes:owner>-->

<itunes:image href="${prefix}/images/nabulogo8xs.png" />

<itunes:category text="Education">
	<itunes:category text="Language Courses" />
</itunes:category>

<c:forEach items="${lessons}" var="l" varStatus="i">
<item>

<title>${l}</title>

<itunes:author>Philipp Thomann</itunes:author>
<itunes:subtitle>${l}</itunes:subtitle>
<itunes:summary></itunes:summary>

<c:url value="${prefix}/nabu-podcast.wav?id=${param.id}&mode=${param.mode}${urls[i.index]}" var="url"/>
<enclosure url="${fn:escapeXml(url)}" type="audio/x-wav" />

<guid>${fn:escapeXml(url)}&amp;date=${dates[i.index]}</guid>
<pubDate>${dates[i.index]}</pubDate>

<!--<enclosure-->
<!--	url="http://example.com/podcasts/everything/AllAboutEverythingEpisode3.m4a"-->
<!--	length="8727310" type="audio/x-m4a" />-->
<!--<itunes:duration>7:04</itunes:duration>-->
<!--<itunes:keywords>salt, pepper, shaker, exciting</itunes:keywords>-->

</item>
</c:forEach>

</channel>
</rss>
