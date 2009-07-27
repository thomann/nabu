package ch.unizh.ori.nabu.ui.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Vocabulary;

/*
 * Created on 20.02.2004
 *
 *(c) Johannes Thomann
 */

/**
 * @author johi
 * 
 * created first in project NabuttuHelper
 * 
 */
public class NabuttuCreator {

	private Map ret;

	public Map gatherData(String id, File vocFile, Vocabulary voc) throws Exception{
		ret = new HashMap();
		List types = new ArrayList();
		for (Iterator iter = voc.getModes().keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			Mode m = (Mode) voc.getModes().get(key);
			String[] s = new String[3];
			s[0] = m.getName();
			StringBuffer a = new StringBuffer();
			StringBuffer b = new StringBuffer();
			for (Iterator iterator = m.createModeFields().iterator(); iterator.hasNext();) {
				ModeField mf = (ModeField) iterator.next();
				StringBuffer c;
				c = (mf.isAsking())?b:a;
				if(c.length()>0)
					c.append(',');
				c.append(mf.getColumn().getColumn());
			}
			s[1] = a.toString();
			s[2] = b.toString();
			types.add(s);
		}
		outputToRet(types,"vocs/"+id+"/"+id+"_types.txt", 3);
		
		List names = new ArrayList();
		List vocs = new ArrayList();
		int maxFrameLen = splitIntoLists(new BufferedReader(new InputStreamReader(new FileInputStream(vocFile), "UTF-8")), vocs, true);
		for (int i = 0; i < vocs.size(); i++) {
			String name2 = id + "/" + id + "_" + (i + 1) + ".bvoc";
			outputToRet((List) vocs.get(i), "vocs/"+name2, maxFrameLen);
			names.add(name2);
		}
		
		for (int i = 0; i < names.size(); i++) {
			String lesson = String.valueOf(i + 1);
			lesson = ((FieldStream)voc.getLections().get(i)).getName();
			String[] s = { lesson, (String) names.get(i) };
			names.set(i, s);
		}
		outputToRet(names, "vocs/"+id + "/" + id + "_list.txt", 2);
		
		String name = "index.b";
		String[] header = {
				voc.getName(), id
			};
		outputToRet(Collections.singletonList(header), name, 2);
		return ret;
	}

	public void outputToRet(List list, String name, int maxFrameLen)
			throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeTable(list, new BufferedOutputStream(bout),
				maxFrameLen);
		bout.flush();
		ret.put(name, new ByteArrayInputStream(bout.toByteArray()));
	}

	public static int splitIntoLists(Reader in1, List vocs, boolean multi)
			throws IOException {
		BufferedReader in = new BufferedReader(in1);
		List voc = new ArrayList();
		vocs.add(voc);
		String line;
		int maxFrameLen = 0;
		while ((line = in.readLine()) != null) {
			if (multi && line.trim().length() == 0) {
				voc = new ArrayList();
				vocs.add(voc);
			} else {
				String[] fields = line.split("\\t");
				maxFrameLen = Math.max(maxFrameLen, fields.length);
				voc.add(fields);
			}
		}
		return maxFrameLen;
	}

	public static void writeTable(List list, OutputStream out1, int maxFrameLen) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(out1);
			out.writeInt(list.size());
			out.writeInt(maxFrameLen);
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				String[] element = (String[]) iter.next();
				for (int i = 0; i < element.length; i++) {
					out.writeUTF(element[i]);
				}
				for (int i = 0; i < maxFrameLen - element.length; i++) {
					out.writeUTF("");
				}
			}
			out.flush();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				// out.close();
			} catch (Throwable t) {
			}
		}
	}

}
