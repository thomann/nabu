/*
 * Created on 10.05.2004
 */
package ch.unizh.ori.nabu.voc;

/**
 * @author pht
 */
public class FileDictionarySource extends FileLectionSource {
	
	protected String lesson(String[] fields) {
		String lesson = super.lesson(fields);
		if(lesson != null && lesson.length() >= 1){
			lesson = lesson.substring(0,1).toLowerCase();
		}
		return lesson;
	}


}
